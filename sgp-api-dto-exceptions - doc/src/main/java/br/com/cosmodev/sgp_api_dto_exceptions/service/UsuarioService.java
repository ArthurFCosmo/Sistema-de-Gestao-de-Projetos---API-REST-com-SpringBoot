package br.com.cosmodev.sgp_api_dto_exceptions.service;

import br.com.cosmodev.sgp_api_dto_exceptions.exceptions.ElementoNaoEncontradoException;
import br.com.cosmodev.sgp_api_dto_exceptions.exceptions.RegraDeNegocioVioladaException;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.UsuarioRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.UsuarioResponseDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.enums.StatusUsuario;
import br.com.cosmodev.sgp_api_dto_exceptions.model.Usuario;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.ProjetoRepository;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    // INJEÇÃO DE DEPENDÊNCIAS -----------------------------------------------------------------------------------------

    private final UsuarioRepository usuarioRepository;

    private final ProjetoRepository projetoRepository;

    // MÉTODOS PADRÃO DO CRUD  -----------------------------------------------------------------------------------------

    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO usuario) {

        return converterModelParaResponseDTO(usuarioRepository.save(converterRequestDTOParaModel(usuario)));

    }

    public UsuarioResponseDTO buscarUsuarioPorId(Long id){

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado Usuario com o Id solicitado.")
        );

        return converterModelParaResponseDTO(usuario);

    }

    public List<UsuarioResponseDTO> listarUsuarios(){

        return usuarioRepository.findAll().stream().map(this::converterModelParaResponseDTO).toList();

    }

    public void deletarUsuarioPorId(Long id){

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado Usuario com o Id solicitado.")
        );

        if (projetoRepository.existsByResponsavel_Id(id)) {
            throw new RegraDeNegocioVioladaException(
                    "O usuário não pôde ser deletado pois existem projetos em que ele é o responsável."
            );
        }

        usuarioRepository.delete(usuario);

    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO usuario) {

        usuarioRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado Usuario com o Id solicitado.")
        );

        return converterModelParaResponseDTO(usuarioRepository.save(converterRequestDTOParaModel(id, usuario)));

    }

    // MÉTODOS DE CONVERSÃO DE ENTIDADES -------------------------------------------------------------------------------

    private UsuarioResponseDTO converterModelParaResponseDTO(Usuario usuario){

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                mascararCPF(usuario.getCpf()),
                usuario.getEmail(),
                usuario.getDataNascimento(),
                calcularIdade(usuario.getDataNascimento()),
                usuario.getStatus().toString()
        );

    }

    private Usuario converterRequestDTOParaModel(UsuarioRequestDTO usuario){
        return new Usuario(
                null,
                usuario.nome(),
                usuario.cpf(),
                usuario.email(),
                usuario.senha(),
                usuario.dataNascimento(),
                converterParaEnumStatus(usuario.status())
        );
    }

    private Usuario converterRequestDTOParaModel(Long id, UsuarioRequestDTO usuario){
        return new Usuario(
                id,
                usuario.nome(),
                usuario.cpf(),
                usuario.email(),
                usuario.senha(),
                usuario.dataNascimento(),
                converterParaEnumStatus(usuario.status())
        );
    }

    private StatusUsuario converterParaEnumStatus (String status){
        try {
            return StatusUsuario.valueOf(status.toUpperCase().strip());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioVioladaException("Valor de STATUS inválido. Valores aceitos: ATIVO, INATIVO, BLOQUEADO.");
        }
    }

    // MÉTODOS FERRAMENTAS ---------------------------------------------------------------------------------------------

    private String mascararCPF(String cpf){
        return cpf.substring(0, 3) + ".***.***-**";
    }

    private int calcularIdade(LocalDate dataNascimento){
        LocalDate dataAtual = LocalDate.now();
        Period intervalo = Period.between(dataNascimento, dataAtual);

        return intervalo.getYears();
    }
}
