package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ProjetoRequestDTO(

    @Schema(description = "Nome do projeto", example = "Projeto Alpha")
    @NotBlank(message = "Nome do projeto não pode ser nulo ou em branco.")
    String nome,

    @Schema(description = "Descrição do projeto", example = "Projeto para desenvolver nova funcionalidade")
    String descricao,

    @Schema(description = "Data de início do projeto", example = "01/07/2026")
    @NotNull(message = "Data de início do projeto não pode ser nula.")
    LocalDate dataInicio,

    @Schema(description = "Data de conclusão prevista do projeto", example = "31/12/2026")
    LocalDate dataConclusao,

    @Schema(description = "Prioridade do projeto", example = "ALTA")
    @NotBlank(message = "Prioridade do projeto não pode ser nula ou em branco.")
    String prioridade,

    @Schema(description = "Status do projeto", example = "EM_ANDAMENTO")
    @NotBlank(message = "Status do projeto não pode ser nulo ou em branco.")
    String status,

    @Schema(description = "ID do usuário responsável pelo projeto", example = "1")
    @NotNull(message = "ID do usuário responsável pelo projeto não pode ser nulo.")
    Long usuarioResponsavelId

) {
}
