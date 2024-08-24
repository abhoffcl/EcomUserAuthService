package dev.Abhishek.EcomUserAuthService.util;

import dev.Abhishek.EcomUserAuthService.exception.InvalidInputException;

import java.util.regex.Pattern;

public class ValidationHelper {

    public static void validateName(String name) {
        final String NAME_PATTERN = "^[a-zA-Z\\s]{2,}$";
        if (name == null || !Pattern.matches(NAME_PATTERN, name)) {
            throw new InvalidInputException("Invalid name format. Name should contain only letters and spaces, and be at least 2 characters long.");
        }
    }
    public static void validateEmail(String email) {
        final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (email == null || !Pattern.matches(EMAIL_PATTERN, email)) {
            throw new InvalidInputException("Invalid email format.");
        }
    }
    public static void validatePassword(String password) {
        final String PASSWORD_PATTERN =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (password == null || !Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new InvalidInputException("Invalid password format. Password should have at least 8 characters, including a lowercase letter, an uppercase letter, a digit, and a special character.");
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        final String PHONE_PATTERN = "^[0-9]{10}$";
        if (phoneNumber == null || !Pattern.matches(PHONE_PATTERN, phoneNumber)) {
            throw new InvalidInputException("Invalid phone number format. Phone number should be exactly 10 digits long.");
        }
    }
}
