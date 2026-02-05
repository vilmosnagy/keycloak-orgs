package io.phasetwo.service.passwordpolicy;

import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;


public class OrgAwareSpecialCharsPasswordPolicyProvider implements PasswordPolicyProvider {

    private int minimumSpecialChars;

    @Override
    public PolicyError validate(RealmModel realm, UserModel user, String password) {
        return validate(user.getUsername(), password);
    }

    @Override
    public PolicyError validate(String user, String password) {
        final var specialChars = password
                .chars()
                .filter(c -> !Character.isLetterOrDigit(c))
                .count();
        if (specialChars < minimumSpecialChars) {
            return new PolicyError("Password must contain at least %d special characters", minimumSpecialChars);
        }
        return null;
    }

    @Override
    public Object parseConfig(String value) {
        this.minimumSpecialChars = parseInteger(value, 1);
        return null;
    }

    @Override
    public void close() {

    }
}
