package br.com.cosmodev.sgp_api_dto_exceptions.model;

import br.com.cosmodev.sgp_api_dto_exceptions.enums.Prioridade;
import br.com.cosmodev.sgp_api_dto_exceptions.enums.StatusProjeto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "TB_PROJETOS")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataConclusao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusProjeto status;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario responsavel;
}
