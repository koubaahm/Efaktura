
package org.ms.authentificationservice.securite;

import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.filtres.JwtAuthenticationFilter;
import org.ms.authentificationservice.filtres.JwtAuthorizationFilter;
import org.ms.authentificationservice.filtres.JwtLogoutFilter;
import org.ms.authentificationservice.filtres.TokenManager;
import org.ms.authentificationservice.repositories.AbonnementRepository;
import org.ms.authentificationservice.repositories.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private AppUserRepository userRepository;
    private AbonnementRepository abonnementRepository;

    private TokenManager tokenManager;

    public SecurityConfig(AppUserRepository userRepository, AbonnementRepository abonnementRepository) {
        this.userRepository = userRepository;
        this.abonnementRepository = abonnementRepository;
        this.tokenManager = new TokenManager();
    }

    // Méthode pour l'authentification
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            AppUser appUser = userRepository.findByNom(username);
            Collection<GrantedAuthority> permissions = new ArrayList<>();
            appUser.getRoles().forEach(r -> permissions.add(new SimpleGrantedAuthority(r.getRolename())));
            return new User(appUser.getNom(), appUser.getPassword(), permissions);
        });
    }

    // Définir les règles d'accès
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                //.antMatchers("/users/**").permitAll()
                .antMatchers("/abonnements/**").permitAll()
                .antMatchers("/roles/**").permitAll()
                .antMatchers("/societes/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) // Utiliser une requête POST pour la déconnexion
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)) // Gérer le succès de la déconnexion avec un code de statut 200 OK
                .and()
                .addFilterAfter(jwtLogoutFilter(), LogoutFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManagerBean(), userRepository, abonnementRepository, tokenManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(userRepository, abonnementRepository, tokenManager), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtLogoutFilter jwtLogoutFilter() {
        return new JwtLogoutFilter(tokenManager);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}


//
//package org.ms.authentificationservice.securite;
//
//import org.ms.authentificationservice.entities.AppUser;
//import org.ms.authentificationservice.filtres.JwtAuthenticationFilter;
//import org.ms.authentificationservice.filtres.JwtAuthorizationFilter;
//import org.ms.authentificationservice.repositories.AbonnementRepository;
//import org.ms.authentificationservice.repositories.AppUserRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    private final AppUserRepository userRepository;
//    private final AbonnementRepository abonnementRepository;
//
//    private final LogoutHandler logoutHandler;
//
//    public SecurityConfig(AppUserRepository userRepository, AbonnementRepository abonnementRepository,LogoutHandler logoutHandler) {
//        this.userRepository = userRepository;
//        this.abonnementRepository = abonnementRepository;
//        this.logoutHandler=logoutHandler;
//
//    }
//
//    // Méthode pour l'authentification
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // Utiliser les données de la BD
//        // Cette méthode est appelée suite à la validation du formulaire d'authentification
//        auth.userDetailsService(username -> {
//            // Récupérer la valeur de "username" pour récupérer un objet AppUser de la BD
//            AppUser appUser = userRepository.findByNom(username);
//            // Construire une collection des rôles (permissions) selon le format de Spring Security
//            Collection<GrantedAuthority> permissions = new ArrayList<>();
//            // Parcourir la liste des rôles de l'utilisateur pour remplir la collection des permissions
//            appUser.getRoles().forEach(r -> {
//                permissions.add(new SimpleGrantedAuthority(r.getRolename()));
//            });
//            // Retourner un objet "User" de Spring Framework qui contient : "username", "password" et les permissions
//            return new User(appUser.getNom(), appUser.getPassword(), permissions);
//        });
//    }
//
//    //Définir les règles d'accès
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable(); //désactiver la protection contre CSRF
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.headers().frameOptions().disable();
//        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
//
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
//        http.authorizeRequests().antMatchers("/abonnements/**").permitAll();
//        http.authorizeRequests().antMatchers("/roles/**").permitAll();
//        http.authorizeRequests().antMatchers("/societes/**").permitAll();
//        http.authorizeRequests().antMatchers("/logout/**").permitAll();
//
////        // Ajouter cette ligne pour la déconnexion
////        http.logout()
////                .logoutUrl("/logout")
////                .logoutSuccessHandler(new CustomLogoutSuccessHandler()) // Utiliser le gestionnaire de déconnexion personnalisé
////                .invalidateHttpSession(true)
////                .permitAll();
//
//        http.authorizeRequests().anyRequest().authenticated();
//        //Ajouter le filtre
//        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean(), userRepository, abonnementRepository));
//        http.addFilterBefore(new JwtAuthorizationFilter(userRepository, abonnementRepository), UsernamePasswordAuthenticationFilter.class);
//        http.logout().addLogoutHandler(logoutHandler).logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()));
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//}
