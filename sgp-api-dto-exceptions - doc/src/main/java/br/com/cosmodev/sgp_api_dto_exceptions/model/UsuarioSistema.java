package br.com.cosmodev.sgp_api_dto_exceptions.model;

import br.com.cosmodev.sgp_api_dto_exceptions.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


// Entidade criada exclusivamente para carregar dados de autenticação dos usuários do nosso sistema. Mesmo que eles
// se cadastrem depois como usuario dentro do ecossistema da aplicação, essa classe modelo é essencial para deixar salvo
// no nosso banco de dados as informações de autenticação fora do ecossistema.


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_USUARIOS_SISTEMA")

// Essa classe implementa UserDetails, que é necessário para transformá-la em uma entidade que o SpringSecurity
// entende que ele pode usar para autenticação. Tem métodos obrigatórios que devemos dar override.
public class UsuarioSistema implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Implementações do UserDetails

    // Retorna uma lista de entidades que obrigatoriamente extendem GrantedAuthority, que serão as ROLES do nosso
    // sistema de segurança, os cargos com poderes diferentes.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    // Precisamos definir para o SpringSecurity saber qual atributo representa a senha, pois o nome é variável. Ele
    // espera que seja "Password", mas nós usamos "senha" justamente por que ele tem essa flexibilidade.
    @Override
    public String getPassword() {
        return senha;
    }

    // A mesma coisa aqui, estamos explicando para ele que o equivalente de username é email na nossa API.
    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
