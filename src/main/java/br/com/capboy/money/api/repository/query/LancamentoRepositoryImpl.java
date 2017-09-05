package br.com.capboy.money.api.repository.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.capboy.money.api.model.Lancamento;
import br.com.capboy.money.api.repository.filter.LancamentoFilter;
import br.com.capboy.money.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Lancamento> consulta(LancamentoFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);

		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criaRestricoes(filter, builder, root);
		criteria.where(predicates);

		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adicionaRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}

	@Override
	public Page<ResumoLancamento> consultaResumo(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		criteria.select(builder.construct(ResumoLancamento.class, root.get("codigo"), root.get("descricao"),
				root.get("dataVencimento"), root.get("dataPagamento"), root.get("valor"), root.get("tipo"),
				root.get("categoria").get("nome"), root.get("pessoa").get("nome")));

		Predicate[] predicates = criaRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		adicionaRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Predicate[] criaRestricoes(LancamentoFilter filter, CriteriaBuilder builder, Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotEmpty(filter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get("descricao")),
					"%" + filter.getDescricao().toLowerCase() + "%"));
		}

		if (filter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"), filter.getDataVencimentoDe()));
		}

		if (filter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"), filter.getDataVencimentoAte()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionaRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Long total(LancamentoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criaRestricoes(filter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));

		return manager.createQuery(criteria).getSingleResult();
	}
}