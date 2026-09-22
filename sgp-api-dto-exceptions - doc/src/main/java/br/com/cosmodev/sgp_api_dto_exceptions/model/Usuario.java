package br.com.cosmodev.sgp_api_dto_exceptions.model;

import br.com.cosmodev.sgp_api_dto_exceptions.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "TB_USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String senha;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUsuario status;
}
