package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TarefaResponseDTO(

        @Schema(description = "ID da tarefa", example = "1")
        Long id,

        @Schema(description = "Nome da tarefa", example = "Implementar login")
        String nome,

        @Schema(description = "Descrição da tarefa", example = "Criar endpoint e interface de login")
        String descricao,

        @Schema(description = "Data de início da tarefa", example = "01/07/2026")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataInicio,

        @Schema(description = "Data de conclusão da tarefa", example = "10/07/2026")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataConclusao,

        @Schema(description = "Status da tarefa", example = "CONCLUIDA")
        String status,

        @Schema(description = "ID do responsável pela tarefa", example = "2")
        Long responsavelId,

        @Schema(description = "Nome do responsável pela tarefa", example = "Maria Souza")
        String responsavelNome,

        @Schema(description = "ID do projeto associado", example = "1")
        Long projetoId,

        @Schema(description = "Nome do projeto associado", example = "Projeto Alpha")
        String projetoNome

) {
}
