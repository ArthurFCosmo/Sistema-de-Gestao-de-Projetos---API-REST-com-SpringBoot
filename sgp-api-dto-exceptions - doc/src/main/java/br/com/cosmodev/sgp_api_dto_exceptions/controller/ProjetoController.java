package br.com.cosmodev.sgp_api_dto_exceptions.controller;

import br.com.cosmodev.sgp_api_dto_exceptions.dtos.ProjetoRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.ProjetoResponseDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.service.ProjetoService;
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

@Tag(name = "Projeto", description = "Gerenciamento de projetos do sistema.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    // INJEÇÃO DE DEPENDÊNCIAS -----------------------------------------------------------------------------------------

    private final ProjetoService projetoService;

    // CONTROLLERS PADRÃO DO CRUD --------------------------------------------------------------------------------------

    @Operation(summary = "Cadastrar projeto", description = "Cadastra um novo projeto no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Projeto cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> salvarProjeto(@RequestBody @Valid ProjetoRequestDTO projeto){
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoService.salvarProjeto(projeto));
    }

    @Operation(summary = "Listar projetos", description = "Lista todos os projetos cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetos(){
        return ResponseEntity.ok(projetoService.listarProjetos());
    }

    @Operation(summary = "Buscar projeto por ID", description = "Busca um projeto específico pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COLABORADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarProjetoPorId(@PathVariable Long id){
        return ResponseEntity.ok(projetoService.buscarProjetoPorId(id));
    }

    @Operation(summary = "Deletar projeto", description = "Deleta um projeto específico pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Projeto deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProjetoPorId(@PathVariable Long id){
        projetoService.deletarProjetoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar projeto", description = "Atualiza os dados de um projeto específico pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> atualizarProjeto(@PathVariable Long id, @RequestBody @Valid ProjetoRequestDTO projeto){
        return ResponseEntity.ok(projetoService.atualizarProjeto(id, projeto));
    }


    // MÉTODOS DE CONSULTA PERSONALIZADOS

    @Operation(summary = "Listar projetos por responsável", description = "Lista os projetos filtrados pelo ID do responsável.")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COLABORADOR')")
    @GetMapping("/responsavel/{id}")
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetosPorResponsavelId(@PathVariable Long id){
        return ResponseEntity.ok(projetoService.listarProjetosPorResponsavelId(id));
    }
}
