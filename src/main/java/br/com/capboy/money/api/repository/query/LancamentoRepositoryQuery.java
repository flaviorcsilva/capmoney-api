package br.com.capboy.money.api.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.capboy.money.api.model.Lancamento;
import br.com.capboy.money.api.repository.filter.LancamentoFilter;
import br.com.capboy.money.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> consulta(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> consultaResumo(LancamentoFilter lancamentoFilter, Pageable pageable);
}
