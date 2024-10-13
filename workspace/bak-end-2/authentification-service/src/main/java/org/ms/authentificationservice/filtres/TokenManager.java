package org.ms.authentificationservice.filtres;

import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    private static final List<String> validTokens = new ArrayList<>();

    public static void addValidToken(String token) {
        validTokens.add(token);
    }

    public static void removeValidToken(String token) {
        validTokens.remove(token);
    }

    public static boolean isValidToken(String token) {
        return validTokens.contains(token);
    }

    public static void clear() {
        validTokens.clear();
    }

    public static void iterateValidTokens() {
        for (String token : validTokens) {
          // afficher le token
            System.out.println(token);
        }
    }
}

