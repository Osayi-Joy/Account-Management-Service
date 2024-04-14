package com.interswitchgroup.accountmanagementsystem.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.interswitchgroup.accountmanagementsystem.authentication.dto.UserAuthProfileDTO;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Joy Osayi
 * @createdOn April-12(Fri)-2024
 */
public class CommonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? "SYSTEM" : auth.getName();
    }

    public static ObjectMapper getObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean validatePassword(String password) {
        // Password should have at least one capital letter, a number, and a special character
        // Password should be at least 8 characters long
        return password != null && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[@#$%^&+=].*");
    }



    public static Map<String, String> getClaims(String username, UserAuthProfileDTO userDetails) {
        return buildClaims(username, userDetails, null);
    }

    public static Map<String, String> getClaims(
            String username, UserAuthProfileDTO userDetails, String resetKey) {
        return buildClaims(username, userDetails, resetKey);
    }

    private static Map<String, String> buildClaims(
            String username, UserAuthProfileDTO userDetails, String resetKey) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", username);

        String authorities =
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));
        claims.put("permissions", authorities);
        claims.put("email", userDetails.getEmail());
        claims.put("name", userDetails.getFirstName().concat(" ").concat(userDetails.getLastName()));
        claims.put("role", userDetails.getAssignedRole());
        return claims;
    }

    public static <T, E> PaginatedResponseDTO<T> createPaginatedResponse(Page<E> page, List<T> content) {
        return PaginatedResponseDTO.<T>builder()
                .content(content)
                .currentPage(page.getNumber() + 1)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .build();
    }

    private CommonUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
