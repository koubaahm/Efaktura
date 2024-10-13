package org.ms.authentificationservice.filtres;

import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    private List<String> validTokens;

    public TokenManager() {
        this.validTokens = new ArrayList<>();
    }

    public void addValidToken(String token) {
        validTokens.add(token);
    }

    public void removeValidToken(String token) {
        validTokens.remove(token);
    }

    public boolean isValidToken(String token) {
        return validTokens.contains(token);
    }

    public void clear(){
        validTokens.clear();
    }

    public void iterateValidTokens() {
        for (String token : validTokens) {
            // Faites quelque chose avec chaque token, par exemple l'afficher
            System.out.println(token);
        }
    }
}

