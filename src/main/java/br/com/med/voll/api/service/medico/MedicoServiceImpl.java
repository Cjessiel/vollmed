package br.com.med.voll.api.service.medico;

import br.com.med.voll.api.dto.medico.DadosAtualizacaoMeditoDto;
import br.com.med.voll.api.dto.medico.DadosCadastroMedicoDto;
import br.com.med.voll.api.dto.medico.DadosDetalhamentoMedicoDto;
import br.com.med.voll.api.dto.medico.DadosListagemMedicoDto;
import br.com.med.voll.api.repository.MedicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements MedicoService{

    private final MedicoRepository repository;


    public MedicoServiceImpl(MedicoRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public ResponseEntity execute(DadosCadastroMedicoDto dados) {
        var model = DadosCadastroMedicoDto.construirModel(dados);

        repository.save(model);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalhamentoMedicoDto(model));
    }

    @Override
    public ResponseEntity<Page<DadosListagemMedicoDto>> execute(Pageable paginacao) {
        return ResponseEntity.ok().body(repository.findAll(paginacao).map(DadosListagemMedicoDto::new));
    }

    @Override
    @Transactional
    public ResponseEntity execute(DadosAtualizacaoMeditoDto dados) {
        var model = repository.getReferenceById(dados.id());

        model.atualizarInformacoes(dados);

        return ResponseEntity.ok().body(new DadosDetalhamentoMedicoDto(model));
    }
}