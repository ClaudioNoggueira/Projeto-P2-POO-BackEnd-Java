package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.PedidoDAO;
import model.entities.Cliente;
import model.entities.Pedido;

public class PedidoDaoJDBC implements PedidoDAO {

	private Connection con;

	public PedidoDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Pedido obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO pedido(dataPedido, statusPedido, idCliente) VALUES(?, ?, ?)");
			st.setDate(1, new java.sql.Date(obj.getDataPedido().getTime()));
			st.setInt(2, obj.getStatusPedido().getCodigo());
			st.setInt(3, obj.getCliente().getId());

			System.out.println(
					(st.executeUpdate() > 0) ? "Pedido efetuado com sucesso." : "Não foi possível efetuar pedido.");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Pedido obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE pedido SET " + "dataPedido = ?, " + "statusPedido = ?, "
					+ "idCliente = ? " + "WHERE id = ?");

			st.setDate(1, new java.sql.Date(obj.getDataPedido().getTime()));
			st.setInt(2, obj.getStatusPedido().getCodigo());
			st.setInt(3, obj.getCliente().getId());
			st.setInt(4, obj.getId());

			System.out.println(
					(st.executeUpdate() > 0) ? "Pedido atualizado com sucesso." : "Não foi possível atualizar pedido.");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void delete(Integer id) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM pedido WHERE id = ?");
			st.setInt(1, id);
			System.out.println(
					(st.executeUpdate() > 0) ? "Pedido excluído com sucesso." : "Não foi possível excluir pedido.");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Pedido findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("select pedido.*, cliente.* from pedido "
					+ "inner join cliente on pedido.idCliente = cliente.id " + "WHERE pedido.id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();

			return (rs.next()) ? instanciarPedido(rs, instanciarCliente(rs)) : null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Pedido> findByDate(Date data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Pedido> list = new ArrayList<>();
		Map<Integer, Cliente> map = new HashMap<>();
		try {
			st = con.prepareStatement("select pedido.*, cliente.*, contato.*, endereco.* from pedido "
					+ "inner join cliente on cliente.id = pedido.idCliente " + "inner join cliente_contato as cli_cont "
					+ "inner join contato on cli_cont.idCliente = cliente.id and cli_cont.idContato = contato.id "
					+ "inner join cliente_endereco as cli_end "
					+ "inner join endereco on cli_end.idCliente = cliente.id and cli_end.idEndereco = endereco.id "
					+ "where dataPedido between ? and now() " + "order by dataPedido");
			st.setDate(1, new java.sql.Date(data.getTime()));
			rs = st.executeQuery();
			while (rs.next()) {
				Cliente cliente = map.get(rs.getInt("cliente.id"));
				if (cliente == null) {
					cliente = instanciarCliente(rs);
					map.put(rs.getInt("cliente.id"), cliente);
				}
				list.add(instanciarPedido(rs, cliente));
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Pedido> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Pedido> list = new ArrayList<>();
		Map<Integer, Cliente> map = new HashMap<>();

		try {
			st = con.prepareStatement(
					"select pedido.*, cliente.* from pedido " + "inner join cliente on pedido.idCliente = cliente.id");

			rs = st.executeQuery();
			while (rs.next()) {
				Cliente cliente = map.get(rs.getInt("cliente.id"));
				if (cliente == null) {
					cliente = instanciarCliente(rs);
					map.put(rs.getInt("cliente.id"), cliente);
				}
				list.add(instanciarPedido(rs, cliente));
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private static Pedido instanciarPedido(ResultSet rs, Cliente cliente) throws SQLException {
		return new Pedido(rs.getInt("id"), rs.getInt("statusPedido"),
				new java.util.Date(rs.getTimestamp("dataPedido").getTime()), cliente);
	}

	private static Cliente instanciarCliente(ResultSet rs) throws SQLException {
		return new Cliente(rs.getInt("cliente.id"), rs.getString("cliente.nome"), ClienteDaoJDBC.instanciarContato(rs),
				ClienteDaoJDBC.instanciarEndereco(rs));
	}
}
