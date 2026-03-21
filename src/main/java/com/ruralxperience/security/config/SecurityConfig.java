package com.ruralxperience.security.config;

import com.ruralxperience.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ==========================================
                        // 1. PUBLIC ENDPOINTS (Auth, Swagger, Actuator)
                        // ==========================================
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/files/**").permitAll()
                        .requestMatchers("/ws/**").permitAll() // WebSockets

                        // ==========================================
                        // 2. ADMIN ENDPOINTS
                        // ==========================================
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/actuator/**").hasRole("ADMIN")

                        // ==========================================
                        // 3. EXPERIENCE CONTROLLER
                        // ==========================================
                        // Specific Host GETs (MUST come before the public wildcard GET)
                        .requestMatchers(HttpMethod.GET, "/api/v1/experiences/host-experiences").hasRole("HOST")
                        // Experience Modifications (Host & Admin)
                        .requestMatchers(HttpMethod.POST, "/api/v1/experiences/**").hasRole("HOST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/experiences/**").hasRole("HOST")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/experiences/**").hasAnyRole("HOST", "ADMIN")
                        // Public Experience GETs
                        .requestMatchers(HttpMethod.GET, "/api/v1/experiences/**").permitAll()

                        // ==========================================
                        // 4. CATEGORY CONTROLLER
                        // ==========================================
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()

                        // ==========================================
                        // 5. REVIEW CONTROLLER
                        // ==========================================
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/experience/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews/*/reply").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews").hasRole("EXPLORER")

                        // ==========================================
                        // 6. WISHLIST CONTROLLER
                        // ==========================================
                        .requestMatchers("/api/v1/wishlist/**").hasRole("EXPLORER")

                        // ==========================================
                        // 7. BOOKING CONTROLLER
                        // ==========================================
                        // Specific Role Views
                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings/host").hasRole("HOST")
                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings/my").hasRole("EXPLORER")

                        // Booking Actions
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings/*/confirm").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings/*/decline").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings/*/cancel").hasAnyRole("EXPLORER", "HOST")
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings").hasRole("EXPLORER")

                        // View specific booking
                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings/*").hasAnyRole("EXPLORER", "HOST", "ADMIN")

                        // ==========================================
                        // 8. NOTIFICATION CONTROLLER
                        // ==========================================
                        // Any authenticated user can access their own notifications
                        .requestMatchers("/api/v1/notifications/**").authenticated()

                        // ==========================================
                        // 9. CATCH-ALL
                        // ==========================================
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}