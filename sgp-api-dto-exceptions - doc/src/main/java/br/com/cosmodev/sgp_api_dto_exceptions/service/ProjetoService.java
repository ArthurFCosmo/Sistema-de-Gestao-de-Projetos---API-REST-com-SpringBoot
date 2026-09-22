package br.com.cosmodev.sgp_api_dto_exceptions.service;

import br.com.cosmodev.sgp_api_dto_exceptions.exceptions.ElementoNaoEncontradoException;
import br.com.cosmodev.sgp_api_dto_exceptions.exceptions.RegraDeNegocioVioladaException;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.ProjetoRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.ProjetoResponseDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.enums.Prioridade;
import br.com.cosmodev.sgp_api_dto_exceptions.enums.StatusProjeto;
import br.com.cosmodev.sgp_api_dto_exceptions.model.Projeto;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.ProjetoRepository;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.TarefaRepository;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjetoService {

    // INJEÇÃO DE DEPENDÊNCIAS -----------------------------------------------------------------------------------------

    private final ProjetoRepository projetoRepository;

    private final UsuarioRepository usuarioRepository;

    private final TarefaService tarefaService;

    private final TarefaRepository tarefaRepository;

    // MÉTODOS PADRÃO DO CRUD  -----------------------------------------------------------------------------------------

    public ProjetoResponseDTO salvarProjeto(ProjetoRequestDTO projeto){

        usuarioRepository.findById(projeto.usuarioResponsavelId()).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado Usuario com o ResponsavelId solicitado.")
        );

        return converterModelParaResponseDTO(projetoRepository.save(converterRequestDTOparaModel(projeto)));
    }

    public ProjetoResponseDTO buscarProjetoPorId(Long id){

        Projeto projeto =  projetoRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado Projeto com o Id solicitado.")
        );

        return converterModelParaResponseDTO(projeto);

    }

    public List<ProjetoResponseDTO> listarProjetos() {
        return projetoRepository.findAll().stream().map(this::converterModelParaResponseDTO).toList();
    }

    public void deletarProjetoPorId(Long id){

        Projeto projeto =  projetoRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado Projeto com o Id solicitado.")
        );

        if (tarefaRepository.existsByProjeto_Id(id)) {
            throw new RegraDeNegocioVioladaException(
                    "O Projeto não pôde ser deletado pois existem tarefas atreladas a ele."
            );
        }

        projetoRepository.delete(projeto);
    }

    public ProjetoResponseDTO atualizarProjeto(Long id, ProjetoRequestDTO projeto){

        projetoRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado Projeto com o Id solicitado.")
        );

        return converterModelParaResponseDTO(projetoRepository.save(converterRequestDTOparaModel(id, projeto)));

    }
    // MÉTODOS DE CONSULTA PERSONALIZADOS  -----------------------------------------------------------------------------

    public List<ProjetoResponseDTO> listarProjetosPorResponsavelId(Long id){
        return projetoRepository.findByResponsavel_Id(id).stream().map(this::converterModelParaResponseDTO).toList();
    }

    // MÉTODOS DE CONVERSÃO DE ENTIDADES -------------------------------------------------------------------------------

    private ProjetoResponseDTO converterModelParaResponseDTO(Projeto projeto){
        return new ProjetoResponseDTO(
            projeto.getId(),
            projeto.getNome(),
            projeto.getDescricao(),
            projeto.getDataInicio(),
            projeto.getDataConclusao(),
            projeto.getPrioridade().toString(),
            projeto.getStatus().toString(),
            projeto.getResponsavel().getId(),
            projeto.getResponsavel().getNome(),
            tarefaService.listarTarefasPorProjeto(projeto.getId())
        );
    }

    private Projeto converterRequestDTOparaModel(ProjetoRequestDTO projeto){
        return new Projeto(
                null,
                projeto.nome(),
                projeto.descricao(),
                projeto.dataInicio(),
                projeto.dataConclusao(),
                converterParaEnumPrioridade(projeto.prioridade()),
                converterParaEnumStatus(projeto.status()),
                usuarioRepository.findById(projeto.usuarioResponsavelId()).orElse(null)
        );
    }

    private Projeto converterRequestDTOparaModel(Long id, ProjetoRequestDTO projeto){
        return new Projeto(
                id,
                projeto.nome(),
                projeto.descricao(),
                projeto.dataInicio(),
                projeto.dataConclusao(),
                converterParaEnumPrioridade(projeto.prioridade()),
                converterParaEnumStatus(projeto.status()),
                usuarioRepository.findById(projeto.usuarioResponsavelId()).orElse(null)
        );
    }

    private StatusProjeto converterParaEnumStatus (String status){
        try {
            return StatusProjeto.valueOf(status.toUpperCase().strip());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioVioladaException("Valor de STATUS inválido. Valores aceitos: ATIVO, CONCLUIDO, CANCELADO.");
        }
    }

    private Prioridade converterParaEnumPrioridade (String prioridade){
        try {
            return Prioridade.valueOf(prioridade.toUpperCase().strip());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioVioladaException("Valor de PRIORIDADE inválido. Valores aceitos: BAIXA, MEDIA, ALTA.");
        }
    }
}
