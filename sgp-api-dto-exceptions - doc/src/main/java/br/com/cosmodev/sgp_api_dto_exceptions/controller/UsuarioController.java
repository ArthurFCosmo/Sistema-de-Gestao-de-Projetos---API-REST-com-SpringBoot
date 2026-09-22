package br.com.cosmodev.sgp_api_dto_exceptions.controller;

import br.com.cosmodev.sgp_api_dto_exceptions.dtos.UsuarioRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.UsuarioResponseDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuário", description = "Gerenciamento de usuarios do sistema.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    // INJEÇÃO DE DEPENDÊNCIAS -----------------------------------------------------------------------------------------

    private final UsuarioService usuarioService;

    // MÉTODOS PADRÃO DO CRUD ------------------------------------------------------------------------------------------

    @Operation(summary = "Cadastrar usuário", description = "Cadastra um novo usuário no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "409", description = "CPF ou E-mail já cadastrados.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody @Valid UsuarioRequestDTO usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarUsuario(usuario));
    }


    @Operation(summary = "Listar usuários", description = "Lista todos os usuários cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }


    @Operation(summary = "Buscar usuário por ID", description = "Busca um usuário específico pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COLABORADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }


    @Operation(summary = "Deletar usuário", description = "Deleta um usuário específico pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuarioPorId(@PathVariable Long id){
        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário específico pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "409", description = "CPF ou E-mail já cadastrados.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO usuario){
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuario));
    }

}
