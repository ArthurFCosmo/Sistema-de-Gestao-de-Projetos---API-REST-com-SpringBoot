package br.com.cosmodev.sgp_api_dto_exceptions.repository;

import br.com.cosmodev.sgp_api_dto_exceptions.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findByResponsavel_Id(Long id);

    boolean existsByResponsavel_Id(Long id);

}
