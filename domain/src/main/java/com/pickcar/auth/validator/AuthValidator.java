package com.pickcar.auth.validator;

import com.pickcar.auth.exception.AuthErrorCode;
import com.pickcar.auth.exception.AuthException;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.auth.presentation.dto.request.UserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AuthValidator {
    private final UserRepository userRepository;

    public void userRegisterRequest(UserInfoRequest request) {
        validateEmail(request.email());
        validatePassword(request.password());
        validateName(request.name());
        validatePhoneNumber(request.phoneNumber());
    }

    private void validateEmail(String email) {
        validateNotBlank(email);
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(regex, email)) {
            throw new AuthException(AuthErrorCode.INVALID_EMAIL_FORMAT);
        }

        if (userRepository.existsByInfoEmail(email)) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }
    }

    private void validatePassword(String password) {
        validateNotBlank(password);
        if (password.length() < 6 || password.length() > 15) {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }

    private void validateName(String name) {
        validateNotBlank(name);
        if (name.isEmpty() || name.length() > 15) {
            throw new AuthException(AuthErrorCode.INVALID_NAME_FORMAT);
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        validateNotBlank(phoneNumber);
        String regex = "^(01[0123456789])[-]?\\d{3,4}[-]?\\d{4}$";
        if (!Pattern.matches(regex, phoneNumber)) {
            throw new AuthException(AuthErrorCode.INVALID_PHONE_NUMBER);
        }
    }

    private void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new AuthException(AuthErrorCode.REQUIRED_FIELD_MISSING);
        }
    }
}
