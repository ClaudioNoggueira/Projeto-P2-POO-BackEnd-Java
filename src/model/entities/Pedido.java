package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import model.entities.enums.StatusPedido;

public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer statusPedido;
	private Date dataPedido;
	private Cliente cliente;
	private Set<Item> items = new HashSet<>();

	public Pedido(Integer id, Integer statusPedido, Date dataPedido, Cliente cliente) {
		this.id = id;
		this.statusPedido = statusPedido;
		this.dataPedido = dataPedido;
		this.cliente = cliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StatusPedido getStatusPedido() {
		return StatusPedido.valueOf(statusPedido);
	}

	public void setStatusPedido(StatusPedido statusPedido) {
		if (statusPedido != null) {
			this.statusPedido = statusPedido.getCodigo();
		}
	}

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", statusPedido=" + statusPedido + ", dataPedido=" + dataPedido + ", cliente="
				+ cliente + ", items=" + items + "]";
	}
}
