package io.phasetwo.service.passwordpolicy;

import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;

public class OrgAwareLengthPasswordPolicyProvider implements PasswordPolicyProvider {

    private int minimumLength;

    @Override
    public PolicyError validate(RealmModel realm, UserModel user, String password) {
        return validate(user.getUsername(), password);
    }

    @Override
    public PolicyError validate(String user, String password) {
        if (password.length() < minimumLength) {
            return new PolicyError("Password must be at least %d characters long", minimumLength);
        }
        return null;
    }

    @Override
    public Object parseConfig(String value) {
        this.minimumLength = parseInteger(value, 8);
        return null;
    }

    @Override
    public void close() {

    }
}
