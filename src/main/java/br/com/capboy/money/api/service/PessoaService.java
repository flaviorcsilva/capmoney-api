package br.com.capboy.money.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.capboy.money.api.model.Pessoa;
import br.com.capboy.money.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa atualiza(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = consultaPorCodigo(codigo);

		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");

		return pessoaRepository.save(pessoaSalva);
	}
	
	public void atualizaEstado(Long codigo, Boolean ativo) {
		Pessoa pessoa = consultaPorCodigo(codigo);
		pessoa.setAtivo(ativo);
		
		pessoaRepository.save(pessoa);
	}
	
	public Pessoa consultaPorCodigo(Long codigo) {
		Pessoa pessoa = pessoaRepository.findOne(codigo);

		if (pessoa == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return pessoa;
	}
	
	public boolean existePessoa(Long codigo) {
		return pessoaRepository.exists(codigo);
	}
}