package br.com.cosmodev.sgp_api_dto_exceptions.filter;

import br.com.cosmodev.sgp_api_dto_exceptions.service.JwtService;
import br.com.cosmodev.sgp_api_dto_exceptions.service.UsuarioSistemaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Essa classe contém o filtro de segurança em si. É ela quem intercepta as requisiçoes, extrai os tokens, valida, e
// autentica o usuario.

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioSistemaService usuarioSistemaService;

    // Esse metodo sobrescrito da classe mae OncePerRequestFilter é quem faz o filtro. Ele é sobrescrito pois essa
    // classe mãe garante que o filtro seja passado apenas uma vez por requisição, sem isso o filtro poderia passar
    // varias vezes na requisição. Ele é obrigatório.
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Pega o cabeçalho da requisição onde deve ter o token
        String authHeader = request.getHeader("Authorization");

        // Se a requisição não vir com token, ou com ele incorreto, ela deixa passar sem autenticar, essa requisição
        // será barrada em qualquer endopint protegido, que precise de role.
        if (authHeader == null || !authHeader.startsWith("Bearer")) {

            filterChain.doFilter(request, response); // deixar a requisição passar para o controller.
            return;

        }

        // Extrair o token sem o prefixo Bearer
        String token = authHeader.substring(7);
        String email = jwtService.extrairEmail(token);

        // Esse if deixa a requisição passar caso ela já tenha sido autenticada. A autenticação fica no
        // SecurityContextHolder da requisição. Se não tiver, inicia o processo de autenticação.
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carrega do banco de dados o UserDetails do usuario a ser aunteticado
            UserDetails userDetails = usuarioSistemaService.loadUserByUsername(email);

            // Valida se o token é legítimo e não expirou
            if (jwtService.tokenValido(token,userDetails)) {

                // Cria um objeto de autenticação com o usuário e seus roles.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,                            // O usuario
                                null,                                   // Credenciais nulas, pois ja validamos
                                userDetails.getAuthorities()            // roles do usuario
                        );

                // Cria os detalhes do objeto de autenticação com informações da requisição.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Registra o objeto de autenticação no SecurityContextHolder da requisição. A partir daqui, o Spring
                // Security sabe quem é o usuário e o que ele pode fazer, e libera a passagem.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        // Deixa a requisição passar para o controller.
        filterChain.doFilter(request, response);

    }
}
