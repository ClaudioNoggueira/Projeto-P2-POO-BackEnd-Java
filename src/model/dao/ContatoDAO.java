package model.dao;

import model.entities.Contato;

public interface ContatoDAO {

	void insert(Contato obj);

	void update(Contato obj);

	void delete(Integer id);
}
