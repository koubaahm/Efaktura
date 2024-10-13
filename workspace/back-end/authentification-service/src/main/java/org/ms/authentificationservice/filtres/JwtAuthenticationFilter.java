package org.ms.authentificationservice.filtres;
//
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import org.ms.authentificationservice.entities.Abonnement;
//import org.ms.authentificationservice.entities.AppUser;
//import org.ms.authentificationservice.entities.Societe;
//import org.ms.authentificationservice.repositories.AbonnementRepository;
//import org.ms.authentificationservice.repositories.AppUserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//    private AuthenticationManager authenticationManager;
//    private AppUserRepository appUserRepository;
//
//    private AbonnementRepository abonnementRepository;
//
//
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository appUserRepository,AbonnementRepository abonnementRepository) {
//        this.authenticationManager = authenticationManager;
//        this.appUserRepository = appUserRepository;
//        this.abonnementRepository=abonnementRepository;
//
//
//        setFilterProcessesUrl("/login"); // Définir l'URL de connexion
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        // Construire un objet UsernamePasswordAuthenticationToken pour l'authentification
//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
//
//        // Authentifier l'utilisateur en appelant l'AuthenticationManager
//        return authenticationManager.authenticate(authRequest);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        // Récupérer l'utilisateur authentifié
//        User user = (User) authResult.getPrincipal();
//        LocalDateTime dateFinAbonnement = obtenirDateFinAbonnement(user.getUsername()); // À remplacer avec la logique appropriée
//
//        try {
//            AppUser appUser = appUserRepository.findByNom(user.getUsername());
//            Abonnement abonnement = null;
//            if (appUser.getSociete() != null) {
//                abonnement = appUser.getSociete().getAbonnement();
//            }
//
//            if (dateFinAbonnement != null && estAbonnementExpire(dateFinAbonnement) || (abonnement != null && !abonnement.getActive())) {
//                if (abonnement != null) {
//                    abonnement.setActive(false);
//                    abonnementRepository.save(abonnement);
//                }
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.setContentType("application/json");
//                response.getWriter().write("{\"message\": \"Abonnement expire\", \"societeId\": \"" + (appUser.getSociete() != null ? appUser.getSociete().getId() : "") + "\", \"abonnementId\": \"" + (abonnement != null ? abonnement.getId() : "") + "\"}");
//                return;
//            }
//        } catch (JWTVerificationException e) {
//            response.setHeader("error-message", e.getMessage());
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//            logger.error("JWT Verification Exception: {}", e.getMessage(), e);
//        }
//
//
//
//        // Choisir un algorithme de cryptage
//        Algorithm algorithm = Algorithm.HMAC256("MaClé");
//
//        // Construire le JWT
//        String jwtAccessToken = JWT.create()
//                .withSubject(user.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
//                .withIssuer(request.getRequestURL().toString())
//                .withClaim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
//                .sign(algorithm);
//
//        // Envoyer le JWT dans l'en-tête de la réponse
//        response.setHeader("Authorization", "Bearer " + jwtAccessToken);
//    }
//    private LocalDateTime obtenirDateFinAbonnement(String username) {
//        // Implémentez la logique pour obtenir la date de fin d'abonnement de l'utilisateur
//        AppUser appUser = appUserRepository.findByNom(username);
//        if (appUser != null) {
//            Societe societe = appUser.getSociete();
//            if (societe != null) {
//                Abonnement abonnement = societe.getAbonnement();
//                if (abonnement != null) {
//                    return abonnement.getDateFin();
//                }
//            }
//        }
//
//        return null;
//    }
//
//
//    private boolean estAbonnementExpire(LocalDateTime dateFinAbonnement) {
//        // Vérifier si l'abonnement de l'utilisateur a expiré en comparant la date de fin d'abonnement
//        // avec la date actuelle
//        // Retournez true si l'abonnement a expiré, sinon retournez false
//        LocalDateTime dateActuelle = LocalDateTime.now();
//        return dateFinAbonnement.isBefore(dateActuelle);
//    }

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.ms.authentificationservice.entities.Abonnement;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.entities.Societe;
import org.ms.authentificationservice.repositories.AbonnementRepository;
import org.ms.authentificationservice.repositories.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private AuthenticationManager authenticationManager;
    private AppUserRepository appUserRepository;
    private AbonnementRepository abonnementRepository;
    private TokenManager tokenManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, AbonnementRepository abonnementRepository, TokenManager tokenManager) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.abonnementRepository = abonnementRepository;
        this.tokenManager = tokenManager;

        setFilterProcessesUrl("/login"); // Définir l'URL de connexion
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Construire un objet UsernamePasswordAuthenticationToken pour l'authentification
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Authentifier l'utilisateur en appelant l'AuthenticationManager
        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Récupérer l'utilisateur authentifié
        User user = (User) authResult.getPrincipal();
        LocalDateTime dateFinAbonnement = obtenirDateFinAbonnement(user.getUsername()); // À remplacer avec la logique appropriée

        try {
            AppUser appUser = appUserRepository.findByNom(user.getUsername());
            Abonnement abonnement = null;
            if (appUser.getSociete() != null) {
                abonnement = appUser.getSociete().getAbonnement();
            }

            if (dateFinAbonnement != null && estAbonnementExpire(dateFinAbonnement) || (abonnement != null && !abonnement.getActive())) {
                if (abonnement != null) {
                    abonnement.setActive(false);
                    abonnementRepository.save(abonnement);
                }
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Abonnement expire\", \"societeId\": \"" + (appUser.getSociete() != null ? appUser.getSociete().getId() : "") + "\", \"abonnementId\": \"" + (abonnement != null ? abonnement.getId() : "") + "\"}");
                return;
            }
        } catch (JWTVerificationException e) {
            response.setHeader("error-message", e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            logger.error("JWT Verification Exception: {}", e.getMessage(), e);
        }

        // Choisir un algorithme de cryptage
        Algorithm algorithm = Algorithm.HMAC256("MaClé");

        // Construire le JWT
        String jwtAccessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
                .sign(algorithm);

        // Ajouter le token à la liste des tokens valides
        tokenManager.addValidToken(jwtAccessToken);
        tokenManager.iterateValidTokens();
        System.out.println("***************************");
        // Envoyer le JWT dans l'en-tête de la réponse
        response.setHeader("Authorization", "Bearer " + jwtAccessToken);
    }

    private LocalDateTime obtenirDateFinAbonnement(String username) {
        // Implémentez la logique pour obtenir la date de fin d'abonnement de l'utilisateur
        AppUser appUser = appUserRepository.findByNom(username);
        if (appUser != null) {
            Societe societe = appUser.getSociete();
            if (societe != null) {
                Abonnement abonnement = societe.getAbonnement();
                if (abonnement != null) {
                    return abonnement.getDateFin();
                }
            }
        }

        return null;
    }

    private boolean estAbonnementExpire(LocalDateTime dateFinAbonnement) {
        // Vérifier si l'abonnement de l'utilisateur a expiré en comparant la date de fin d'abonnement
        // avec la date actuelle
        // Retournez true si l'abonnement a expiré, sinon retournez false
        LocalDateTime dateActuelle = LocalDateTime.now();
        return dateFinAbonnement.isBefore(dateActuelle);
    }
}



//    private boolean estAbonnementExpire(LocalDateTime dateFinAbonnement) {
//        // Vérifier si l'abonnement de l'utilisateur a expiré en comparant la date de fin d'abonnement
//        // avec la date actuelle
//        // Retournez true si l'abonnement a expiré, sinon retournez false
//
//        // Exemple :
//        Date dateActuelle = new Date();
//        Date dateFin = Date.from(dateFinAbonnement.atZone(ZoneId.systemDefault()).toInstant());
//        return dateFin.before(dateActuelle);
//    }



//package org.ms.authentificationservice.filtres;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import org.ms.authentificationservice.entities.Abonnement;
//import org.ms.authentificationservice.entities.AppUser;
//import org.ms.authentificationservice.entities.Societe;
//import org.ms.authentificationservice.repositories.AbonnementRepository;
//import org.ms.authentificationservice.repositories.AppUserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//    private AuthenticationManager authenticationManager;
//    private AppUserRepository appUserRepository;
//    private AbonnementRepository abonnementRepository;
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, AbonnementRepository abonnementRepository) {
//        this.authenticationManager = authenticationManager;
//        this.appUserRepository = appUserRepository;
//        this.abonnementRepository = abonnementRepository;
//
//        setFilterProcessesUrl("/login"); // Définir l'URL de connexion
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        // Construire un objet UsernamePasswordAuthenticationToken pour l'authentification
//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
//
//        // Authentifier l'utilisateur en appelant l'AuthenticationManager
//        return authenticationManager.authenticate(authRequest);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        // Récupérer l'utilisateur authentifié
//        User user = (User) authResult.getPrincipal();
//        LocalDateTime dateFinAbonnement = obtenirDateFinAbonnement(user.getUsername()); // À remplacer avec la logique appropriée
//
//        // Vérifier si l'abonnement de l'utilisateur a expiré
//        try {
//            AppUser appUser = appUserRepository.findByNom(user.getUsername());
//            Abonnement abonnement = null;
//            if (appUser.getSociete() != null) {
//                abonnement = appUser.getSociete().getAbonnement();
//            }
//
//            if (dateFinAbonnement != null && estAbonnementExpire(dateFinAbonnement) || (abonnement != null && !abonnement.getActive())) {
//                if (abonnement != null) {
//                    abonnement.setActive(false);
//                    abonnementRepository.save(abonnement);
//                }
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.setContentType("application/json");
//                response.getWriter().write("{\"message\": \"Abonnement expire\", \"societeId\": \"" + (appUser.getSociete() != null ? appUser.getSociete().getId() : "") + "\", \"abonnementId\": \"" + (abonnement != null ? abonnement.getId() : "") + "\"}");
//                return;
//            }
//        } catch (JWTVerificationException e) {
//            response.setHeader("error-message", e.getMessage());
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//            logger.error("JWT Verification Exception: {}", e.getMessage(), e);
//        }
//
//        // Choisir un algorithme de cryptage
//        Algorithm algorithm = Algorithm.HMAC256("MaClé");
//
//        // Construire le JWT
//        String jwtAccessToken = JWT.create()
//                .withSubject(user.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
//                .withIssuer(request.getRequestURL().toString())
//                .withClaim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
//                .sign(algorithm);
//
//        // Envoyer le JWT dans l'en-tête de la réponse
//        response.setHeader("Authorization", "Bearer " + jwtAccessToken);
//    }
//
//    private LocalDateTime obtenirDateFinAbonnement(String username) {
//        // Implémentez la logique pour obtenir la date de fin d'abonnement de l'utilisateur
//        AppUser appUser = appUserRepository.findByNom(username);
//        if (appUser != null) {
//            Societe societe = appUser.getSociete();
//            if (societe != null) {
//                Abonnement abonnement = societe.getAbonnement();
//                if (abonnement != null) {
//                    return abonnement.getDateFin();
//                }
//            }
//        }
//
//        return null;
//    }
//
//    private boolean estAbonnementExpire(LocalDateTime dateFinAbonnement) {
//        // Vérifier si l'abonnement de l'utilisateur a expiré en comparant la date de fin d'abonnement
//        // avec la date actuelle
//        // Retournez true si l'abonnement a expiré, sinon retournez false
//
//        // Exemple :
//        Date dateActuelle = new Date();
//        Date dateFin = Date.from(dateFinAbonnement.atZone(ZoneId.systemDefault()).toInstant());
//        return dateFin.before(dateActuelle);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        super.unsuccessfulAuthentication(request, response, failed);
//    }
//
//
//
//
//}