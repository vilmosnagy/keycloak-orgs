package io.phasetwo.service.passwordpolicy;

import io.phasetwo.service.model.OrganizationProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.policy.DefaultPasswordPolicyManagerProvider;
import org.keycloak.policy.PasswordPolicyManagerProvider;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class OrganizationalPasswordPolicyManagerProvider implements PasswordPolicyManagerProvider {
    private final KeycloakSession session;
    private final DefaultPasswordPolicyManagerProvider defaultPasswordPolicyManagerProvider;

    public OrganizationalPasswordPolicyManagerProvider(KeycloakSession session) {
        this.session = session;
        this.defaultPasswordPolicyManagerProvider = new DefaultPasswordPolicyManagerProvider(session);
    }

    @Override
    public PolicyError validate(RealmModel realm, UserModel user, String password) {
        final var realmPolicyError = defaultPasswordPolicyManagerProvider.validate(realm, user, password);
        if (realmPolicyError != null) {
            return realmPolicyError;
        }

        OrganizationProvider orgs = session.getProvider(OrganizationProvider.class);
        return orgs
                .getUserOrganizationsStream(realm, user)
                .filter(org -> org.getAttributes().containsKey("passwordPolicy"))
                .flatMap(org -> org.getAttributes().getOrDefault("passwordPolicy", List.of()).stream())
                .filter(Objects::nonNull)
                .flatMap(passwordPolicy -> getPasswordPolicyProviders(passwordPolicy).stream())
                .map(provider -> provider.validate(realm, user, password))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private Set<PasswordPolicyProvider> getPasswordPolicyProviders(String passwordPolicy) {
        PasswordPolicy policy = PasswordPolicy.parse(session, passwordPolicy);
        return policy
                .getPolicies()
                .stream()
                .map(policyId -> session.getProvider(PasswordPolicyProvider.class, policyId))
                .collect(Collectors.toSet());
    }

    @Override
    public PolicyError validate(String user, String password) {
        final var realmPolicyError = defaultPasswordPolicyManagerProvider.validate(user, password);
        if (realmPolicyError != null) {
            return realmPolicyError;
        }
        return null;
    }

    @Override
    public void close() {

    }
}
