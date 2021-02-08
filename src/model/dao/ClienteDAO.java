package model.dao;

import java.util.List;

import model.entities.Cliente;

public interface ClienteDAO {
	
	void insert(Cliente obj);

	void update(Cliente obj);

	void delete(Integer id);

	Cliente findById(Integer id);
	
	List<Cliente> findByName(String nome);

	List<Cliente> findAll();
}
