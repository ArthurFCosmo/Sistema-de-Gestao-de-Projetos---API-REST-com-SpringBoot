package br.com.cosmodev.sgp_api_dto_exceptions.model;

import br.com.cosmodev.sgp_api_dto_exceptions.enums.StatusTarefa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "TB_TAREFAS")
public class Tarefa {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    @ManyToOne
    private Usuario responsavel;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Projeto projeto;
}
