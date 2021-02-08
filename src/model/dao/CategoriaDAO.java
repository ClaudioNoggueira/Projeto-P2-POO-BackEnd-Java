package model.dao;

import java.util.List;

import model.entities.Categoria;

public interface CategoriaDAO {

	void insert(Categoria obj);

	void update(Categoria obj);

	void delete(Integer id);

	Categoria findById(Integer id);

	List<Categoria> findByName(String nome);

	List<Categoria> findAll();
}
