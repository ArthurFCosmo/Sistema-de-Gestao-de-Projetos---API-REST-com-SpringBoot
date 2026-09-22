package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UsuarioRequestDTO(

        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        @NotBlank(message = "Nome não pode ser nulo ou em branco.")
        String nome,

        @Schema(description = "CPF do usuário", example = "123.456.789-00")
        @NotBlank @CPF(message = "CPF inválido.")
        String cpf,

        @Schema(description = "E-mail do usuário", example = "joao.silva@example.com")
        @NotBlank @Email(message = "E-mail inválido.")
        String email,

        @Schema(description = "Senha do usuário", example = "senha123")
        @NotBlank(message = "Senha não pode ser nula ou em branco.")
        String senha,

        @Schema(description = "Data de nascimento do usuário", example = "01/01/1990")
        @NotNull(message = "Data de nascimento não pode ser nula.")
        @PastOrPresent(message = "Data de nascimento não pode ser futura.")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @Schema(description = "Status do usuário", example = "ATIVO")
        @NotBlank(message = "Status não pode ser nulo ou em branco.")
        String status

) {
}
