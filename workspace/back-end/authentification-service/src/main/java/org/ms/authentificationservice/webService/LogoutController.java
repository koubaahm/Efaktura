//package org.ms.authentificationservice.webService;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class LogoutController {
//
//    private List<String> revocationList = new ArrayList<>();
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        // Récupérez le jeton d'authentification de la requête
//        String jwtToken = extractJwtToken(request);
//
//        // Ajoutez le jeton révoqué à la liste de révocation
//        addToRevocationList(jwtToken);
//
//        // Répondez avec un code de statut 200 OK pour indiquer la déconnexion réussie
//        return ResponseEntity.ok("Déconnexion réussie");
//    }
//
//    private String extractJwtToken(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            return authorizationHeader.substring(7);
//        }
//        return null;
//    }
//
//    private void addToRevocationList(String jwtToken) {
//        revocationList.add(jwtToken);
//    }
//}

package org.ms.authentificationservice.webService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;



@RestController
public class LogoutController {



    @GetMapping("/logout-success")
    public ResponseEntity<String> handleLogoutSuccess() {
        return ResponseEntity.ok("Déconnexion réussie");
    }


}
