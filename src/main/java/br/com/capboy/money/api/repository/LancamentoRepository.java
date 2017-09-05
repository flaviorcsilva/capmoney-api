package br.com.capboy.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.capboy.money.api.model.Lancamento;
import br.com.capboy.money.api.repository.query.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
