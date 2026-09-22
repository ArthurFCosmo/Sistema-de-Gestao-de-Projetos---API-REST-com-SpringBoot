package br.com.cosmodev.sgp_api_dto_exceptions.repository;

import br.com.cosmodev.sgp_api_dto_exceptions.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
