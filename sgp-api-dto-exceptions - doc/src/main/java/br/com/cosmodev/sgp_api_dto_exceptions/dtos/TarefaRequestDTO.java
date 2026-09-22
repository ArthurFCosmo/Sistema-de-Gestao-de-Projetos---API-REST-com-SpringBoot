package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TarefaRequestDTO(

        @Schema(description = "Nome da tarefa", example = "Implementar login")
        @NotBlank(message = "Nome da tarefa não pode ser nulo ou vazio")
        String nome,

        @Schema(description = "Descrição da tarefa", example = "Criar endpoint e interface de login")
        String descricao,

        @Schema(description = "Data de início da tarefa", example = "01/07/2026")
        @NotNull(message = "Data de início da tarefa não pode ser nula")
        LocalDate dataInicio,

        @Schema(description = "Data de conclusão da tarefa", example = "10/07/2026")
        LocalDate dataConclusao,

        @Schema(description = "Status da tarefa", example = "PENDENTE")
        @NotNull(message = "Status da tarefa não pode ser nulo")
        String status,

        @Schema(description = "ID do usuário responsável pela tarefa", example = "2")
        Long usuarioResponsavelId,

        @Schema(description = "ID do projeto ao qual a tarefa pertence", example = "1")
        @NotNull(message = "ID do projeto ao qual a tarefa pertence não pode ser nulo")
        Long projetoId
) {
}
