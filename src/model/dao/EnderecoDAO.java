package model.dao;

import model.entities.Endereco;

public interface EnderecoDAO {
	
	void insert(Endereco obj);

	void update(Endereco obj);

	void delete(Integer id);
}
