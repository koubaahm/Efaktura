package org.ms.authentificationservice.filtres;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ms.authentificationservice.controllers.AppUserRestController;
import org.ms.authentificationservice.dtos.AbonnementResponseDTO;
import org.ms.authentificationservice.dtos.AppUserResponseDTO;
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
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final AbonnementRepository abonnementRepository;

    private final AppUserRestController appUserRestController;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, AbonnementRepository abonnementRepository,AppUserRestController appUserRestController) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.abonnementRepository = abonnementRepository;
        this.appUserRestController=appUserRestController;


        setFilterProcessesUrl("/login"); // Définir l'URL de connexion
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Construire un objet UsernamePasswordAuthenticationToken pour l'authentification
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        // Authentifier l'utilisateur en appelant l'AuthenticationManager
        return authenticationManager.authenticate(authRequest);
    }


@Override
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    // Récupérer l'utilisateur authentifié
    User user = (User) authResult.getPrincipal();
    LocalDateTime dateFinAbonnement = obtenirDateFinAbonnement(user.getUsername());

    try {
        AppUser appUser = appUserRepository.findByEmail(user.getUsername());
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
    TokenManager.addValidToken(jwtAccessToken);
    TokenManager.iterateValidTokens();
     AppUserResponseDTO appUser=appUserRestController.getUserByMail(user.getUsername());
    // Ajouter le JWT dans l'en-tête de la réponse avec le préfixe "Bearer"
    response.setHeader("Authorization", "Bearer " + jwtAccessToken);

//    // Envoyer le JWT dans le corps de la réponse
//    response.setStatus(HttpServletResponse.SC_OK);
//    response.setContentType("application/json");
//    response.getWriter().write("{\"token\": \"Bearer " + jwtAccessToken + "\"}");
////    response.setStatus(HttpServletResponse.SC_OK);
////    response.setContentType("application/json");
//
    Gson gson = new Gson();
//    String jsonAppUser = gson.toJson(appUser);
//    response.getWriter().write(jsonAppUser);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");

    JsonObject tokenObject = new JsonObject();
    tokenObject.addProperty("Bearer", jwtAccessToken);

    JsonObject userObject = gson.toJsonTree(appUser).getAsJsonObject();

    JsonArray responseArray = new JsonArray();
    responseArray.add(tokenObject);
    responseArray.add(userObject);

    String jsonResponse = responseArray.toString();

    response.getWriter().write(jsonResponse);




}



    private LocalDateTime obtenirDateFinAbonnement(String email) {
        // Implémentez la logique pour obtenir la date de fin d'abonnement de l'utilisateur
        AppUser appUser = appUserRepository.findByEmail(email);
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
        LocalDateTime dateActuelle = LocalDateTime.now();
        return dateFinAbonnement.isBefore(dateActuelle);
    }
}

