package br.com.capboy.money.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.capboy.money.api.model.Pessoa;
import br.com.capboy.money.api.repository.PessoaRepository;
import br.com.capboy.money.api.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaService service;

	@Autowired
	private PessoaRepository repository;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public List<Pessoa> lista() {
		return repository.findAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> insere(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa novaPessoa = repository.save(pessoa);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(novaPessoa.getCodigo()).toUri();

		response.setHeader("Location", uri.toASCIIString());

		return ResponseEntity.created(uri).body(novaPessoa);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Pessoa> consultaPorCodigo(@PathVariable Long codigo) {
		Pessoa pessoa = repository.findOne(codigo);

		return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public void remove(@PathVariable Long codigo) {
		repository.delete(codigo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> atualiza(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaSalva = service.atualiza(codigo, pessoa);

		return ResponseEntity.ok(pessoaSalva);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public void atualizaEstado(@PathVariable Long codigo, @Valid @RequestBody Boolean ativo) {
		service.atualizaEstado(codigo, ativo);
	}
}