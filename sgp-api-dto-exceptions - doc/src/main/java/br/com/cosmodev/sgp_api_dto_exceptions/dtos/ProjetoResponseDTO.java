package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ProjetoResponseDTO(

        @Schema(description = "ID do projeto", example = "1")
        Long id,

        @Schema(description = "Nome do projeto", example = "Projeto Alpha")
        String nome,

        @Schema(description = "Descrição do projeto", example = "Projeto para desenvolver nova funcionalidade")
        String descricao,

        @Schema(description = "Data de início do projeto", example = "01/07/2026")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataInicio,

        @Schema(description = "Data de conclusão do projeto", example = "31/12/2026")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataConclusao,

        @Schema(description = "Prioridade do projeto", example = "ALTA")
        String prioridade,

        @Schema(description = "Status do projeto", example = "ATIVO")
        String status,

        @Schema(description = "ID do usuário responsável pelo projeto", example = "1")
        Long usuarioResponsavelId,

        @Schema(description = "Nome do usuário responsável pelo projeto", example = "João da Silva")
        String usuarioResponsavelNome,

        @Schema(description = "Lista de tarefas associadas ao projeto")
        List<TarefaResponseDTO> tarefas

) {
}
