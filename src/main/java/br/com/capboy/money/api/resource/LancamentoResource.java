package br.com.capboy.money.api.resource;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.capboy.money.api.exception.PessoaInexistenteOuInativaException;
import br.com.capboy.money.api.exception.CapMoneyApiExceptionHandler.Erro;
import br.com.capboy.money.api.model.Lancamento;
import br.com.capboy.money.api.repository.LancamentoRepository;
import br.com.capboy.money.api.repository.filter.LancamentoFilter;
import br.com.capboy.money.api.repository.projection.ResumoLancamento;
import br.com.capboy.money.api.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LancamentoService service;

	@Autowired
	private LancamentoRepository repository;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<Lancamento> lista() {
		return repository.findAll();
	}

	@GetMapping("/consulta")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> consulta(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return repository.consulta(lancamentoFilter, pageable);
	}

	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento> consultaResumo(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return repository.consultaResumo(lancamentoFilter, pageable);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> consultaPorCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = repository.findOne(codigo);

		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> insere(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento novo = service.salva(lancamento);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(novo.getCodigo())
				.toUri();

		response.setHeader("Location", uri.toASCIIString());

		return ResponseEntity.created(uri).body(novo);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	public void remove(@PathVariable Long codigo) {
		repository.delete(codigo);
	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return ResponseEntity.badRequest().body(erros);
	}
}