package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

public record LoginRequestDTO(

        @NotBlank
        String email,

        @NotBlank
        String senha

) {
}
