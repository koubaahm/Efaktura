package org.ms.Facturationservice.security;

import org.ms.Facturationservice.filtre.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.ws.rs.HttpMethod;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Autres configurations

    @Bean
    public JwtFilter jwtFilter() throws Exception {
        return new JwtFilter(authenticationManagerBean());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/factures").hasAuthority("ADMIN")
                .antMatchers("/**").permitAll()
                .antMatchers("/clients/**").permitAll()
                .antMatchers("/fournisseurs/**").permitAll()
                .antMatchers("/ligneAchats/**").permitAll()
                .antMatchers("/ligneFactures/**").permitAll()
                .antMatchers("/produits/**").permitAll()
                .antMatchers("/tvas/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}


