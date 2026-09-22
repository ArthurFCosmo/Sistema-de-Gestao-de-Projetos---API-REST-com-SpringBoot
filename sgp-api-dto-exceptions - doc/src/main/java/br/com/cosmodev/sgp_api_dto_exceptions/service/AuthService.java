package br.com.cosmodev.sgp_api_dto_exceptions.service;

import br.com.cosmodev.sgp_api_dto_exceptions.dtos.LoginRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.LoginResponseDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.RegisterRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.exceptions.ElementoNaoEncontradoException;
import br.com.cosmodev.sgp_api_dto_exceptions.exceptions.RegraDeNegocioVioladaException;
import br.com.cosmodev.sgp_api_dto_exceptions.model.UsuarioSistema;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.UsuarioSistemaRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Essa classe serve parar abrigar a lógica de autenticação, que servirá de ponte entre o repository e o controller,
// fazendo verificações de regra e lançando exceptions.
@Tag(name = "Autenticação", description = "Endpoints públicos de registro e login.")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Esse aqui é responsável por tentar registrar um novo UsuarioSistema. Durante o processo e valida, cria o usuario
    // cria o token, guarda a senha com hash e devolve tudo no DTO.
    public LoginResponseDTO registrar(RegisterRequestDTO dto) {

        if (usuarioSistemaRepository.findByEmail(dto.email()).isPresent()) {
            throw new RegraDeNegocioVioladaException(
                    "Já existe usuário registrado com o e-mail:" + dto.email()
            );
        }

        UsuarioSistema novoUsuario = new UsuarioSistema(
                null,
                dto.email(),
                passwordEncoder.encode(dto.senha()),
                dto.role()
        );

        UsuarioSistema usuarioSalvo = usuarioSistemaRepository.save(novoUsuario);

        String token = jwtService.gerarToken(usuarioSalvo);

        return new LoginResponseDTO(token, usuarioSalvo.getEmail(), usuarioSalvo.getRole().name());

    }

    // Esse é responsável por logar em um UsuarioSistema existente. Inicialmente ele verifica se e-mail e senha batem,
    // se não, já lança uma exception. Depois ele verifica se existe UsuarioSistema no bd com o e-mail solicitado,
    // caso esteja tudo certo, ele cria o token, e devolve o token, o email e a role.
    public LoginResponseDTO login(LoginRequestDTO dto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));

        UsuarioSistema usuario = usuarioSistemaRepository.findByEmail(dto.email()).orElseThrow(
                () -> new ElementoNaoEncontradoException("Usuário não encontrado com o e-mail: " + dto.email())
        );

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(token, usuario.getEmail(), usuario.getRole().name());

    }

}
