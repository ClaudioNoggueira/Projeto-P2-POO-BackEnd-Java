package model.dao;

import java.util.List;

import model.entities.Item;

public interface ItemDAO {

	void insert(Item obj);
	
	void update(Item obj);
	
	void delete(Integer id);
	
	Item findById(Integer id);
	
	List<Item> findAll();
}
