package br.com.cosmodev.sgp_api_dto_exceptions.controller;

import br.com.cosmodev.sgp_api_dto_exceptions.dtos.TarefaRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.TarefaResponseDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.service.TarefaService;
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

@Tag(name = "Tarefa", description = "Gerenciamento de tarefas do sistema.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    // INJEÇÃO DE DEPENDÊNCIAS -----------------------------------------------------------------------------------------

    private final TarefaService tarefaService;

    // MÉTODOS PADRÃO DO CRUD ------------------------------------------------------------------------------------------

    @Operation(summary = "Cadastrar tarefa", description = "Cadastra uma nova tarefa no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa cadastrada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping
    public ResponseEntity<TarefaResponseDTO> salvarTarefa(@RequestBody @Valid TarefaRequestDTO tarefa){
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaService.salvarTarefa(tarefa));
    }

    @Operation(summary = "Listar tarefas", description = "Lista todas as tarefas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> listarTarefas(){
        return ResponseEntity.ok(tarefaService.listarTarefas());
    }

    @Operation(summary = "Buscar tarefa por ID", description = "Busca uma tarefa específica pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COLABORADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> buscarTarefaPorId(@PathVariable Long id){
        return ResponseEntity.ok(tarefaService.buscarTarefaPorId(id));
    }

    @Operation(summary = "Deletar tarefa", description = "Deleta uma tarefa específica pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefaPorId(@PathVariable Long id){
        tarefaService.deletarTarefaPorId(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa específica pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(@PathVariable Long id, @RequestBody @Valid TarefaRequestDTO tarefa){
        return ResponseEntity.ok(tarefaService.atualizarTarefa(id, tarefa));
    }

}
