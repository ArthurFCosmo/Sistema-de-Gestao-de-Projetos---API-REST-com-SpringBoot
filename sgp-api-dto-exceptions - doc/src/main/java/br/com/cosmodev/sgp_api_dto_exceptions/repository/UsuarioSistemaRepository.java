package br.com.cosmodev.sgp_api_dto_exceptions.repository;

import br.com.cosmodev.sgp_api_dto_exceptions.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// O único metodo que precisamos no repository do UsuarioSistema, pois usamos o e-mail tanto para buscar o usuario no
// login, quanto no filtro de segurança para buscar o usuário pelo e-mail dentro do token

@Repository
public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {

     Optional<UsuarioSistema> findByEmail(String email);

}
