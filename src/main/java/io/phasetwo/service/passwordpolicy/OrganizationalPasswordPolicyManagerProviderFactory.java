package io.phasetwo.service.passwordpolicy;

import com.google.auto.service.AutoService;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.policy.PasswordPolicyManagerProvider;
import org.keycloak.policy.PasswordPolicyManagerProviderFactory;

@AutoService(PasswordPolicyManagerProviderFactory.class)
public class OrganizationalPasswordPolicyManagerProviderFactory implements PasswordPolicyManagerProviderFactory {

    @Override
    public PasswordPolicyManagerProvider create(KeycloakSession session) {
        return new OrganizationalPasswordPolicyManagerProvider(session);
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "organizational-password-policy";
    }

    @Override
    public int order() {
        return 1;
    }
}
