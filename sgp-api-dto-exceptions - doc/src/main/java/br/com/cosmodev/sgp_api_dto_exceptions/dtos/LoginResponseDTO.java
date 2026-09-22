package br.com.cosmodev.sgp_api_dto_exceptions.dtos;

import br.com.cosmodev.sgp_api_dto_exceptions.enums.Role;

public record LoginResponseDTO(

        String token,
        String email,
        String role
) {
}
