package io.phasetwo.service.passwordpolicy;

import io.phasetwo.service.representation.Organization;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;

public interface OrgAwarePasswordPolicyProvider extends PasswordPolicyProvider {

    PolicyError validate(PasswordPolicy passwordPolicy, UserModel user, String password);

    @Override
    default PolicyError validate(RealmModel realm, UserModel user, String password) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    default PolicyError validate(String user, String password) {
        throw new UnsupportedOperationException("TODO");
    }
}
