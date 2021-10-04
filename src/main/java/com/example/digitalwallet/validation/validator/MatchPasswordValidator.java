package com.example.digitalwallet.validation.validator;
import com.example.digitalwallet.dto.security.PasswordResetRequest;
import com.example.digitalwallet.validation.annotation.MatchPassword;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchPasswordValidator implements ConstraintValidator<MatchPassword, PasswordResetRequest> {

    private Boolean allowNull;

    @Override
    public void initialize(MatchPassword constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(PasswordResetRequest value, ConstraintValidatorContext context) {
        String password = value.getPassword();
        String confirmPassword = value.getConfirmPassword();
        if (allowNull) {
            return null == password && null == confirmPassword;
        }
        return password.equals(confirmPassword);
    }
}
