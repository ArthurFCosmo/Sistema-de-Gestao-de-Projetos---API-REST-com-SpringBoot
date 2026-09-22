package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UsuarioResponseDTO(

        @Schema(description = "ID do usuário", example = "1")
        Long id,

        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        String nome,

        @Schema(description = "CPF mascarado do usuário", example = "123.***.***-00")
        String cpfMascarado,

        @Schema(description = "E-mail do usuário", example = "joao.silva@example.com")
        String email,

        @Schema(description = "Data de nascimento do usuário", example = "01/01/1990")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @Schema(description = "Idade do usuário", example = "36")
        int idade,

        @Schema(description = "Status do usuário", example = "ATIVO")
        String status

) {
}
