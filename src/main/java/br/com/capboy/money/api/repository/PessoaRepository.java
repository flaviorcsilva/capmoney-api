package br.com.capboy.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.capboy.money.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
