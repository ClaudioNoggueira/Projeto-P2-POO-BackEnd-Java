package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Pedido;

public interface PedidoDAO {

	void insert(Pedido obj);

	void update(Pedido obj);

	void delete(Integer id);

	Pedido findById(Integer id);

	List<Pedido> findByDate(Date data);

	List<Pedido> findAll();
}
