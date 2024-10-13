package org.ms.authentificationservice.filtres;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class JwtLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {


            // Afficher le contenu de la demande
            System.out.println("Contenu de la demande : ");
            System.out.println(" - Méthode : " + request.getMethod());
            System.out.println(" - URI : " + request.getRequestURI());
            System.out.println(" - En-têtes : ");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                System.out.println("   " + headerName + " : " + headerValue);
            }
            // Autres informations que vous souhaitez afficher

            // Récupérer le jeton de la demande
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                TokenManager.removeValidToken(extractToken(request));

            }

    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        // Implémentez la logique pour déterminer si la requête est une demande de déconnexion
        // Par exemple, vérifiez si l'URL ou le paramètre de requête correspond à une demande de déconnexion
        return request.getServletPath().equals("/logout");
    }

    private String extractToken(HttpServletRequest request) {
        // Implémentez la logique pour extraire le token de la requête
        // Par exemple, en vérifiant l'en-tête "Authorization"
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Supprimer le préfixe "Bearer " pour obtenir le token
        }
        return null; // Aucun token trouvé
    }

    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            try {
                response.getWriter().println("Vous avez été déconnecté. Veuillez vous authentifier");
            } catch (IOException e) {
                System.out.println("Erreur lors de la redirection vers la page de connexion : " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

}

