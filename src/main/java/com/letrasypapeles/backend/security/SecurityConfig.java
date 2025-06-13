package com.letrasypapeles.backend.security;

import com.letrasypapeles.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Importaciones de Spring Security
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// Importar SessionCreationPolicy
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JWTAuthFilter jwtAuthFilter;


    // Configuración del PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración del AuthenticationManager

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    

    // Configuración del DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Configuración del filtro de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/clientes/**").hasRole("CLIENTE")
                .requestMatchers("/api/empleado/**").hasRole("EMPLEADO")
                .requestMatchers("/api/gerente/**").hasRole("GERENTE")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            );
    
        // Añadir el filtro JWT si lo has implementado
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
    
    // @Autowired
    // public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth
    //         .ldapAuthentication()
    //         .userDnPatterns("uid={0},ou=people")
    //         .contextSource()
    //         .url("ldap://localhost:8389/dc=empresaZ,dc=com");
    // }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     return http
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/publico").permitAll()
    //             .anyRequest().authenticated()
    //         )
    //         .httpBasic(Customizer.withDefaults())
    //         .formLogin(Customizer.withDefaults())
    //         .build();
    // }
}
