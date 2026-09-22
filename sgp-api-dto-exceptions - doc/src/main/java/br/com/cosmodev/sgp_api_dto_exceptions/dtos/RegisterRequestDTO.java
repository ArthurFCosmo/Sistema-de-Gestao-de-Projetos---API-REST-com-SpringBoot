package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import br.com.cosmodev.sgp_api_dto_exceptions.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(

        @NotBlank
        String email,

        @NotBlank
        String senha,

        @NotNull
        Role role

) {
}
