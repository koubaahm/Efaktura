//package org.ms.authentificationservice.filtres;
//
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.ms.authentificationservice.entities.Abonnement;
//import org.ms.authentificationservice.entities.AppUser;
//import org.ms.authentificationservice.entities.Societe;
//import org.ms.authentificationservice.repositories.AbonnementRepository;
//import org.ms.authentificationservice.repositories.AppUserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collection;
//
//
//
//public class JwtAuthorizationFilter extends OncePerRequestFilter {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
//
//    private final AppUserRepository appUserRepository;
//    private final AbonnementRepository abonnementRepository;
//
//
//
//    public JwtAuthorizationFilter(AppUserRepository appUserRepository ,AbonnementRepository abonnementRepository) {
//        this.appUserRepository = appUserRepository;
//        this.abonnementRepository=abonnementRepository;
//
//    }
//
//    public final String PREFIXE_JWT = "Bearer ";
//    public final String CLE_SIGNATURE = "MaClé";
//
//    private boolean estAbonnementExpire(LocalDateTime dateFinAbonnement) {
//        // Vérifier si l'abonnement de l'utilisateur a expiré en comparant la date de fin d'abonnement
//        // avec la date actuelle
//        // Retournez true si l'abonnement a expiré, sinon retournez false
//        LocalDateTime dateActuelle = LocalDateTime.now();
//        return dateFinAbonnement.isBefore(dateActuelle);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authorizationToken = request.getHeader("Authorization");
//
//        if (authorizationToken != null && authorizationToken.startsWith(PREFIXE_JWT)) {
//            try {
//                String jwt = authorizationToken.substring(PREFIXE_JWT.length());
//                Algorithm algo = Algorithm.HMAC256(CLE_SIGNATURE);
//                JWTVerifier jwtVerifier = JWT.require(algo).build();
//                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
//                String username = decodedJWT.getSubject();
//                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
//                Collection<GrantedAuthority> permissions = new ArrayList<>();
//                for (String r : roles) {
//                    permissions.add(new SimpleGrantedAuthority(r));
//                }
//
//                LocalDateTime dateFinAbonnement = obtenirDateFinAbonnement(username);
//
//                AppUser appUser = appUserRepository.findByNom(username);
//                Abonnement abonnement = null;
//                if (appUser.getSociete() != null) {
//                    abonnement = appUser.getSociete().getAbonnement();
//                }
//
//                if (dateFinAbonnement != null && estAbonnementExpire(dateFinAbonnement) || (abonnement != null && !abonnement.getActive())) {
//                    if (abonnement != null) {
//                        abonnement.setActive(false);
//                        abonnementRepository.save(abonnement);
//                    }
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    response.setContentType("application/json");
//                    response.getWriter().write("{\"message\": \"Abonnement expire\", \"societeId\": \"" + (appUser.getSociete() != null ? appUser.getSociete().getId() : "") + "\", \"abonnementId\": \"" + (abonnement != null ? abonnement.getId() : "") + "\"}");
//                    return;
//                }
//
//
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, permissions);
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                filterChain.doFilter(request, response);
//            } catch (JWTVerificationException e) {
//                response.setHeader("error-message", e.getMessage());
//                response.sendError(HttpServletResponse.SC_FORBIDDEN);
//                logger.error("JWT Verification Exception: {}", e.getMessage(), e);
//            }
//        } else {
//            filterChain.doFilter(request, response);
//        }
//
//
//
//    }
//
//    private LocalDateTime obtenirDateFinAbonnement(String username) {
//        AppUser appUser = appUserRepository.findByNom(username);
//        if (appUser != null) {
//            Societe societe = appUser.getSociete();
//            if (societe != null) {
//                Abonnement abonnement = societe.getAbonnement();
//                if (abonnement != null && abonnement.getActive()) {
//                    return abonnement.getDateFin();
//                }
//            }
//        }
//        return null;
//    }
//}


//package org.ms.authentificationservice.filtres;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.ms.authentificationservice.entities.Abonnement;
//import org.ms.authentificationservice.entities.AppUser;
//import org.ms.authentificationservice.entities.Societe;
//import org.ms.authentificationservice.repositories.AbonnementRepository;
//import org.ms.authentificationservice.repositories.AppUserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//
//public class JwtAuthorizationFilter extends OncePerRequestFilter {
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
//
//    private final AppUserRepository appUserRepository;
//    private final AbonnementRepository abonnementRepository;
//
//    public JwtAuthorizationFilter( AppUserRepository appUserRepository, AbonnementRepository abonnementRepository) {
//
//        this.appUserRepository = appUserRepository;
//        this.abonnementRepository = abonnementRepository;
//    }
//
//    public final String PREFIXE_JWT = "Bearer ";
//    public final String CLE_SIGNATURE = "MaClé";
//
//    private boolean estAbonnementExpire(LocalDateTime dateFinAbonnement) {
//        Date dateActuelle = new Date();
//        Date dateFin = Date.from(dateFinAbonnement.atZone(ZoneId.systemDefault()).toInstant());
//        return dateFin.before(dateActuelle);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authorizationToken = request.getHeader("Authorization");
//
//        if (authorizationToken != null && authorizationToken.startsWith(PREFIXE_JWT)) {
//            try {
//                String jwt = authorizationToken.substring(PREFIXE_JWT.length());
//                Algorithm algo = Algorithm.HMAC256(CLE_SIGNATURE);
//                JWTVerifier jwtVerifier = JWT.require(algo).build();
//                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
//                String username = decodedJWT.getSubject();
//                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
//                Collection<GrantedAuthority> permissions = new ArrayList<>();
//                for (String r : roles) {
//                    permissions.add(new SimpleGrantedAuthority(r));
//                }
//
//                LocalDateTime dateFinAbonnement = obtenirDateFinAbonnement(username);
//
//                AppUser appUser = appUserRepository.findByNom(username);
//                Abonnement abonnement = null;
//                if (appUser.getSociete() != null) {
//                    abonnement = appUser.getSociete().getAbonnement();
//                }
//
//                if (dateFinAbonnement != null && estAbonnementExpire(dateFinAbonnement) || (abonnement != null && !abonnement.getActive())) {
//                    if (abonnement != null) {
//                        abonnement.setActive(false);
//                        abonnementRepository.save(abonnement);
//                    }
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    response.setContentType("application/json");
//                    response.getWriter().write("{\"message\": \"Abonnement expire\", \"societeId\": \"" + (appUser.getSociete() != null ? appUser.getSociete().getId() : "") + "\", \"abonnementId\": \"" + (abonnement != null ? abonnement.getId() : "") + "\"}");
//                    return;
//                }
//
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, permissions);
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                filterChain.doFilter(request, response);
//            } catch (JWTVerificationException e) {
//                response.setHeader("error-message", e.getMessage());
//                response.sendError(HttpServletResponse.SC_FORBIDDEN);
//                logger.error("JWT Verification Exception: {}", e.getMessage(), e);
//            }
//        } else {
//            filterChain.doFilter(request, response);
//        }
//
//        if (request.getRequestURI().equals("/logout")) {
//            logout(request, response);
//        }
//    }
//
//    private LocalDateTime obtenirDateFinAbonnement(String username) {
//        AppUser appUser = appUserRepository.findByNom(username);
//        if (appUser != null) {
//            Societe societe = appUser.getSociete();
//            if (societe != null) {
//                Abonnement abonnement = societe.getAbonnement();
//                if (abonnement != null && abonnement.getActive()) {
//                    return abonnement.getDateFin();
//                }
//            }
//        }
//        return null;
//    }
//
//    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // Effacer le jeton d'authentification du client
//        response.setHeader("Authorization", "");
//
//        // Effacer le jeton d'authentification de la session
//        request.getSession().invalidate();
//
//        // Déconnecter l'utilisateur actuel
//        SecurityContextHolder.clearContext();
//
//        // Rediriger l'utilisateur vers la page de connexion ou une autre page appropriée
//        response.sendRedirect("/login");
//    }
//
//}
//


//package org.ms.authentificationservice.filtres;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.ms.authentificationservice.entities.Abonnement;
//import org.ms.authentificationservice.entities.AppUser;
//import org.ms.authentificationservice.entities.Societe;
//import org.ms.authentificationservice.repositories.AbonnementRepository;
//import org.ms.authentificationservice.repositories.AppUserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class JwtAuthorizationFilter extends OncePerRequestFilter {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
//
//    private final AppUserRepository appUserRepository;
//    private final AbonnementRepository abonnementRepository;
//
//    public JwtAuthorizationFilter(AppUserRepository appUserRepository, AbonnementRepository abonnementRepository) {
//        this.appUserRepository = appUserRepository;
//        this.abonnementRepository = abonnementRepository;
//    }
//
//    public final String PREFIXE_JWT = "Bearer ";
//    public final String CLE_SIGNATURE = "MaClé";
//
//    private final List<String> revocationList=new ArrayList<>();
//
//    private boolean estAbonnementExpire(LocalDateTime dateFinAbonnement) {
//        // Vérifier si l'abonnement de l'utilisateur a expiré en comparant la date de fin d'abonnement
//        // avec la date actuelle
//        // Retournez true si l'abonnement a expiré, sinon retournez false
//        LocalDateTime dateActuelle = LocalDateTime.now();
//        return dateFinAbonnement.isBefore(dateActuelle);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//            String authorizationToken = request.getHeader("Authorization");
//
//            if (authorizationToken != null && authorizationToken.startsWith(PREFIXE_JWT)) {
//                try {
//                    String jwt = authorizationToken.substring(7);
//                    Algorithm algo = Algorithm.HMAC256(CLE_SIGNATURE);
//                    JWTVerifier jwtVerifier = JWT.require(algo).build();
//                    DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
//                    String username = decodedJWT.getSubject();
//                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
//                    Collection<GrantedAuthority> permissions = new ArrayList<>();
//                    for (String r : roles) {
//                        permissions.add(new SimpleGrantedAuthority(r));
//                    }
//
//                    LocalDateTime dateFinAbonnement = obtenirDateFinAbonnement(username);
//
//                    AppUser appUser = appUserRepository.findByNom(username);
//                    Abonnement abonnement = null;
//                    if (appUser.getSociete() != null) {
//                        abonnement = appUser.getSociete().getAbonnement();
//                    }
//
//                    if (dateFinAbonnement != null && estAbonnementExpire(dateFinAbonnement) || (abonnement != null && !abonnement.getActive())) {
//                        if (abonnement != null) {
//                            abonnement.setActive(false);
//                            abonnementRepository.save(abonnement);
//                        }
//                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                        response.setContentType("application/json");
//                        response.getWriter().write("{\"message\": \"Abonnement expire\", \"societeId\": \"" + (appUser.getSociete() != null ? appUser.getSociete().getId() : "") + "\", \"abonnementId\": \"" + (abonnement != null ? abonnement.getId() : "") + "\"}");
//                        return;
//                    }
//
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, permissions);
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    filterChain.doFilter(request, response);
//                } catch (JWTVerificationException e) {
//                    response.setHeader("error-message", e.getMessage());
//                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
//                    logger.error("JWT Verification Exception: {}", e.getMessage(), e);
//                }
//            } else {
//                filterChain.doFilter(request, response);
//            }
//        }
//
//
//    private LocalDateTime obtenirDateFinAbonnement(String username) {
//        AppUser appUser = appUserRepository.findByNom(username);
//        if (appUser != null) {
//            Societe societe = appUser.getSociete();
//            if (societe != null) {
//                Abonnement abonnement = societe.getAbonnement();
//                if (abonnement != null && abonnement.getActive()) {
//                    return abonnement.getDateFin();
//                }
//            }
//        }
//        return null;
//    }
//}
package org.ms.authentificationservice.filtres;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.ms.authentificationservice.entities.Abonnement;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.entities.Societe;
import org.ms.authentificationservice.repositories.AbonnementRepository;
import org.ms.authentificationservice.repositories.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final AppUserRepository appUserRepository;
    private final AbonnementRepository abonnementRepository;
    private final TokenManager tokenManager;

    public JwtAuthorizationFilter(AppUserRepository appUserRepository, AbonnementRepository abonnementRepository, TokenManager tokenManager) {
        this.appUserRepository = appUserRepository;
        this.abonnementRepository = abonnementRepository;
        this.tokenManager = tokenManager;
    }

    public final String PREFIXE_JWT = "Bearer ";
    public final String CLE_SIGNATURE = "MaClé";


    private boolean estAbonnementExpire(LocalDateTime dateFinAbonnement) {
        // Vérifier si l'abonnement de l'utilisateur a expiré en comparant la date de fin d'abonnement
        // avec la date actuelle
        // Retournez true si l'abonnement a expiré, sinon retournez false
        LocalDateTime dateActuelle = LocalDateTime.now();
        return dateFinAbonnement.isBefore(dateActuelle);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationToken = request.getHeader("Authorization");

        if (authorizationToken != null && authorizationToken.startsWith(PREFIXE_JWT)) {
            try {
                String jwt = authorizationToken.substring(7);
                if (!tokenManager.isValidToken(jwt)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                tokenManager.iterateValidTokens();
                System.out.println("----------------------------");

                Algorithm algo = Algorithm.HMAC256(CLE_SIGNATURE);
                JWTVerifier jwtVerifier = JWT.require(algo).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<GrantedAuthority> permissions = new ArrayList<>();
                for (String r : roles) {
                    permissions.add(new SimpleGrantedAuthority(r));
                }

                LocalDateTime dateFinAbonnement = obtenirDateFinAbonnement(username);

                AppUser appUser = appUserRepository.findByNom(username);
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

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, permissions);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } catch (JWTVerificationException e) {
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                logger.error("JWT Verification Exception: {}", e.getMessage(), e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }


    private LocalDateTime obtenirDateFinAbonnement(String username) {
        AppUser appUser = appUserRepository.findByNom(username);
        if (appUser != null) {
            Societe societe = appUser.getSociete();
            if (societe != null) {
                Abonnement abonnement = societe.getAbonnement();
                if (abonnement != null && abonnement.getActive()) {
                    return abonnement.getDateFin();
                }
            }
        }
        return null;
    }
}
