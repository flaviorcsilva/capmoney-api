package br.com.capboy.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.capboy.money.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
