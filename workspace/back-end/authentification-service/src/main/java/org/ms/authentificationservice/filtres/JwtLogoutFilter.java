//
//
package org.ms.authentificationservice.filtres;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class JwtLogoutFilter implements LogoutHandler {
//
//    private final RequestMatcher logoutRequestMatcher;
//    private final LogoutSuccessHandler logoutSuccessHandler;
//
//    public JwtLogoutFilter(String logoutUrl, LogoutSuccessHandler logoutSuccessHandler) {
//        this.logoutRequestMatcher = new AntPathRequestMatcher(logoutUrl, "POST");
//        this.logoutSuccessHandler = logoutSuccessHandler;
//    }
//
//    @Override
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        // Supprimer le jeton en cours
//        SecurityContextHolder.clearContext();
//        try {
//            try {
//                logoutSuccessHandler.onLogoutSuccess(request, response, authentication);
//            } catch (ServletException e) {
//                throw new RuntimeException(e);
//            }
//        } catch (IOException e) {
//            // Gérer l'exception en conséquence
//            e.printStackTrace();
//        }
//    }
//
//    public RequestMatcher getLogoutRequestMatcher() {
//        return logoutRequestMatcher;
//    }
//
//    public LogoutSuccessHandler getLogoutSuccessHandler() {
//        return logoutSuccessHandler;
//    }
//}



import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtLogoutFilter extends OncePerRequestFilter {

    private TokenManager tokenManager;

    public JwtLogoutFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isLogoutRequest(request)) {
            String token = extractToken(request);
            if (token != null) {
                tokenManager.removeValidToken(token);
            }
            // Autres opérations de déconnexion...
            redirectLogoutSuccessUrl(request, response);
        } else {
            filterChain.doFilter(request, response);
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

    private void redirectLogoutSuccessUrl(HttpServletRequest request, HttpServletResponse response) {
        // Logique pour rediriger vers l'URL de succès de déconnexion (à adapter selon votre cas)
    }
}
