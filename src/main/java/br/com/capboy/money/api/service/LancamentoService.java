package br.com.capboy.money.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.capboy.money.api.exception.PessoaInexistenteOuInativaException;
import br.com.capboy.money.api.model.Lancamento;
import br.com.capboy.money.api.model.Pessoa;
import br.com.capboy.money.api.repository.LancamentoRepository;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaService pessoaService;

	public Lancamento salva(Lancamento lancamento) {
		validaPessoaAntesDeSalvar(lancamento.getPessoa().getCodigo());

		return lancamentoRepository.save(lancamento);
	}

	private void validaPessoaAntesDeSalvar(Long codigoPessoa) {
		Pessoa pessoa = null;
		if (pessoaService.existePessoa(codigoPessoa)) {
			pessoa = pessoaService.consultaPorCodigo(codigoPessoa);

			if (pessoa.isInativo()) {
				throw new PessoaInexistenteOuInativaException();
			}
		} else {
			throw new PessoaInexistenteOuInativaException();
		}
	}
}