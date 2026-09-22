package br.com.cosmodev.sgp_api_dto_exceptions.service;

import br.com.cosmodev.sgp_api_dto_exceptions.exceptions.ElementoNaoEncontradoException;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.TarefaRequestDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.dtos.TarefaResponseDTO;
import br.com.cosmodev.sgp_api_dto_exceptions.enums.StatusTarefa;
import br.com.cosmodev.sgp_api_dto_exceptions.exceptions.RegraDeNegocioVioladaException;
import br.com.cosmodev.sgp_api_dto_exceptions.model.Tarefa;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.ProjetoRepository;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.TarefaRepository;
import br.com.cosmodev.sgp_api_dto_exceptions.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TarefaService {

    // INJEÇÃO DE DEPENDÊNCIAS -----------------------------------------------------------------------------------------

    private final TarefaRepository tarefaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProjetoRepository projetoRepository;

    // MÉTODOS PADRÃO DO CRUD  -----------------------------------------------------------------------------------------

    public TarefaResponseDTO salvarTarefa(TarefaRequestDTO tarefa) {

        projetoRepository.findById(tarefa.projetoId()).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado um Projeto com o ProjetoId solicitado.")
        );

        if (Objects.nonNull(tarefa.usuarioResponsavelId())) {
            usuarioRepository.findById(tarefa.usuarioResponsavelId()).orElseThrow(
                    () -> new ElementoNaoEncontradoException("Não foi encontrado um UsuárioResponsavel com o Id solicitado.")
            );
        }


        return converterModelParaResponseDTO(tarefaRepository.save(converterRequestDTOparaModel(tarefa)));
    }

    public TarefaResponseDTO buscarTarefaPorId(Long id) {

        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrada Tarefa com o Id solicitado.")
        );

        return converterModelParaResponseDTO(tarefa);
    }

    public List<TarefaResponseDTO> listarTarefas() {
        return tarefaRepository.findAll().stream().map(this::converterModelParaResponseDTO).toList();
    }

    public void deletarTarefaPorId(Long id) {

        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrada Tarefa com o Id solicitado.")
        );

         tarefaRepository.delete(tarefa);

    }

    public TarefaResponseDTO atualizarTarefa(Long id, TarefaRequestDTO tarefa) {

        tarefaRepository.findById(id).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrada Tarefa com o Id solicitado.")
        );

        projetoRepository.findById(tarefa.projetoId()).orElseThrow(
                () -> new ElementoNaoEncontradoException("Não foi encontrado um Projeto com o ProjetoId solicitado.")
        );

        if (Objects.nonNull(tarefa.usuarioResponsavelId())) {
            usuarioRepository.findById(tarefa.usuarioResponsavelId()).orElseThrow(
                    () -> new ElementoNaoEncontradoException("Não foi encontrado um UsuárioResponsavel com o Id solicitado.")
            );
        }

        return converterModelParaResponseDTO(tarefaRepository.save(converterRequestDTOparaModel(id, tarefa)));

    }

    // MÉTODOS DE CONSULTA PERSONALIZADOS  -----------------------------------------------------------------------------

    public List<TarefaResponseDTO> listarTarefasPorProjeto(Long projetoId) {
        return tarefaRepository.findByProjeto_Id(projetoId).stream().map(this::converterModelParaResponseDTO).toList();
    }

    // MÉTODOS DE CONVERSÃO DE ENTIDADES -------------------------------------------------------------------------------

    private TarefaResponseDTO converterModelParaResponseDTO(Tarefa tarefa){
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getNome(),
                tarefa.getDescricao(),
                tarefa.getDataInicio(),
                tarefa.getDataConclusao(),
                tarefa.getStatus().toString(),
                Objects.isNull(tarefa.getResponsavel()) ? null : tarefa.getResponsavel().getId(),
                Objects.isNull(tarefa.getResponsavel()) ? null : tarefa.getResponsavel().getNome(),
                tarefa.getProjeto().getId(),
                tarefa.getProjeto().getNome()
        );
    }

    private Tarefa converterRequestDTOparaModel(TarefaRequestDTO tarefa){
        return new Tarefa(
                null,
                tarefa.nome(),
                tarefa.descricao(),
                tarefa.dataInicio(),
                tarefa.dataConclusao(),
                converterParaEnumStatus(tarefa.status()),
                usuarioRepository.findById(tarefa.usuarioResponsavelId()).orElse(null),
                projetoRepository.findById(tarefa.projetoId()).orElse(null)
        );
    }

    private Tarefa converterRequestDTOparaModel(Long id, TarefaRequestDTO tarefa){
        return new Tarefa(
                id,
                tarefa.nome(),
                tarefa.descricao(),
                tarefa.dataInicio(),
                tarefa.dataConclusao(),
                converterParaEnumStatus(tarefa.status()),
                usuarioRepository.findById(tarefa.usuarioResponsavelId()).orElse(null),
                projetoRepository.findById(tarefa.projetoId()).orElse(null)
        );
    }

    private StatusTarefa converterParaEnumStatus (String status){
        try {
            return StatusTarefa.valueOf(status.toUpperCase().strip());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioVioladaException("Valor de STATUS inválido. Valores aceitos: PENDENTE, FAZENDO, CONCLUIDA.");
        }
    }

    // MÉTODOS FERRAMENTAS ---------------------------------------------------------------------------------------------

}
