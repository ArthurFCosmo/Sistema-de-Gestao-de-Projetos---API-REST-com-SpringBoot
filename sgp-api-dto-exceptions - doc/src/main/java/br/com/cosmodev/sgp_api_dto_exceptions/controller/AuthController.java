package br.com.cosmodev.sgp_api_dto_exceptions.controller;


import br.com.cosmodev.sgp_api_dto_exceptions.dtos.LoginRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.LoginResponseDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.RegisterRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints públicos de registro e login.")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar usuário", description = "Cadastra um novo usuário do sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "E-mail já cadastrado.")
    })
    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> registrar(@RequestBody @Valid RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(dto));
    }


    @Operation(summary = "Login", description = "Autentica o usuário e retorna um token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok().body(authService.login(dto));
    }

}
