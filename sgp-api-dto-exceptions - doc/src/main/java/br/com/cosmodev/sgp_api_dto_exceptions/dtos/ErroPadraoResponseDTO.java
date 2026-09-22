package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato padrão de erro da API.")
public record ErroPadraoResponseDTO(
    @Schema(description = "Data e hora do erro", example = "2026-07-16T12:34:56")
    LocalDateTime timestamp,

    @Schema(description = "Código HTTP de status", example = "404")
    int status,

    @Schema(description = "Tipo do erro", example = "Not Found")
    String error,

    @Schema(description = "Mensagem detalhada do erro", example = "Recurso não encontrado")
    String message,

    @Schema(description = "Caminho da requisição que causou o erro", example = "/projetos/1")
    String path
) {
}
