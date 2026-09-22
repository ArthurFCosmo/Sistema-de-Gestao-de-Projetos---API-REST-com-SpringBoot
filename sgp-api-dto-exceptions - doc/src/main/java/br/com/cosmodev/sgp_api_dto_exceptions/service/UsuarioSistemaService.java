package br.com.cosmodev.sgp_api_dto_exceptions.service;

import br.com.cosmodev.sgp_api_dto_exceptions.repository.UsuarioSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Aqui no service o SpringSecurity precisa de um metodo que traga o UserDetails do usuario pelo username, que no nosso
// caso é o e-mail. Para isso, vamos implementar a interface UserDetailsService, e sobrescrever o metodo
// loadUserByUsername(), que retornará o UserDetails do usuario alvo.

@RequiredArgsConstructor
@Service
public class UsuarioSistemaService implements UserDetailsService {

    private final UsuarioSistemaRepository usuarioSistemaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioSistemaRepository.findByEmail(email).orElseThrow(
                () ->new UsernameNotFoundException("Não foi encontrado usuário com o e-mail " + email)
        );
    }
}
