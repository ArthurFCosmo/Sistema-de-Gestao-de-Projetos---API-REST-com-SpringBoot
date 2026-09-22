package br.com.cosmodev.sgp_api_dto_exceptions.config;

import br.com.cosmodev.sgp_api_dto_exceptions.filter.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//No security config, devemos colocar as configurações de segurança que queremos usar com o SpringSecurity, visto que
//ele comporta mais de uma forma de estruturar a segurança de uma API


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // Aqui devemos criar nosso security filter chain, ele carrega configurações do modo o nosso sistema de segurança
    // deve funcionar, e também quais serão os nossos endpoints públicos, que não terão nenhum tipo de verificação
    // de segurança.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }


    // Uma estrutura que permite verificar se login e senha estão corretos. É instanciado como Bean para ser usado
    // por outras classes.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Instanciando o ByCript para ser nosso algoritmo de mascarar a senhas com hash. Outras classes usarão essa
    // estrutura sempre que precisarem verificar uma senha.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
