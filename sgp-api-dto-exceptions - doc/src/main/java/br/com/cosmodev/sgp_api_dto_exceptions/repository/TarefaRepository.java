package br.com.cosmodev.sgp_api_dto_exceptions.repository;

import br.com.cosmodev.sgp_api_dto_exceptions.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByProjeto_Id(Long id);

    boolean existsByProjeto_Id(Long id);

}
