package model.dao;

import java.util.List;

import model.entities.Produto;

public interface ProdutoDAO {
	
	void insert(Produto obj);

	void update(Produto obj);

	void delete(Integer id);

	Produto findById(Integer id);
	
	List<Produto> findByName(String nome);
	
	List<Produto> findByCategory(String nome);

	List<Produto> findAll();
}
