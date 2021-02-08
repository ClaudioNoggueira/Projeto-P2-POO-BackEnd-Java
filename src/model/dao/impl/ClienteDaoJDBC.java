package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ClienteDAO;
import model.entities.Cliente;
import model.entities.Contato;
import model.entities.Endereco;

public class ClienteDaoJDBC implements ClienteDAO {

	private Connection con;

	public ClienteDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Cliente obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO cliente(nome) VALUES(?)");
			st.setString(1, obj.getNome());

			System.out.println((st.executeUpdate() > 0) ? "Dados do cliente inseridos com sucesso."
					: "Não foi possível inserir cliente.");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(null);
		}
	}

	@Override
	public void update(Cliente obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE cliente SET "
					+ "nome = ? "
					+ "WHERE id = ?");
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());
			
			System.out.println((st.executeUpdate() > 0) ? "Dados do cliente alterados com sucesso."
					: "Não foi possível alterar dados do cliente.");
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void delete(Integer id) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM cliente WHERE id = ?");
			st.setInt(1, id);

			System.out.println((st.executeUpdate() > 0) ? "Cliente excluído com sucesso."
					: "Não foi possível excluir cliente.");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Cliente findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement(
					"select cliente.*, contato.*, endereco.* from cliente "
					+ "inner join cliente_contato as cli_cont "
					+ "inner join contato on cli_cont.idCliente = cliente.id and cli_cont.id = contato.id "
					+ "inner join cliente_endereco as cli_end "
					+ "inner join endereco on cli_end.idCliente = cliente.id and cli_end.id = endereco.id "
					+ "WHERE cliente.id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				return instanciarCliente(rs, instanciarContato(rs), instanciarEndereco(rs));
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Cliente> findByName(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Cliente> list = new ArrayList<>();
		Map<Integer, Contato> mapCont = new HashMap<>();
		Map<Integer, Endereco> mapEnd = new HashMap<>();
		try {
			st = con.prepareStatement("SELECT cliente.*, contato.*, endereco.* FROM cliente "
					+ "inner join cliente_contato as cli_cont "
				    + "inner join contato on cli_cont.idCliente = cliente.id and cli_cont.id = contato.id "
				    + "inner join cliente_endereco as cli_end "
				    + "inner join endereco on cli_end.idCliente = cliente.id and cli_end.id = endereco.id "
					+ "WHERE nome like ? "
					+ "ORDER BY nome");
			st.setString(1, "%" + nome + "%");

			rs = st.executeQuery();
			while (rs.next()) {
				Contato cont = mapCont.get(rs.getInt("contato.id"));
				if(cont == null) {
					cont = instanciarContato(rs);
					mapCont.put(rs.getInt("contato.id"), cont);
				}
				
				Endereco end = mapEnd.get(rs.getInt("endereco.id"));
				if(end == null) {
					end = instanciarEndereco(rs);
					mapEnd.put(rs.getInt("endereco.id"), end);
				}
				
				list.add(instanciarCliente(rs, cont, end));
				
			}
			return list;
		} catch (SQLException e) {

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	@Override
	public List<Cliente> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Cliente> list = new ArrayList<>();
		Map<Integer, Contato> mapCont = new HashMap<>();
		Map<Integer, Endereco> mapEnd = new HashMap<>();
		
		try {
			st = con.prepareStatement(
					"select cliente.*, contato.*, endereco.* from cliente "
					+ "inner join cliente_contato as cli_cont "
				    + "inner join contato on cli_cont.idCliente = cliente.id and cli_cont.id = contato.id "
				    + "inner join cliente_endereco as cli_end "
				    + "inner join endereco on cli_end.idCliente = cliente.id and cli_end.id = endereco.id "
				    + "order by nome");
			
			rs = st.executeQuery();
			
			while(rs.next()) {
				Contato cont = mapCont.get(rs.getInt("contato.id"));
				if(cont == null) {
					cont = instanciarContato(rs);
					mapCont.put(rs.getInt("contato.id"), cont);
				}
				
				Endereco end = mapEnd.get(rs.getInt("endereco.id"));
				if(end == null) {
					end = instanciarEndereco(rs);
					mapEnd.put(rs.getInt("endereco.id"), end);
				}
				
				list.add(instanciarCliente(rs, cont, end));
			}
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	protected static Contato instanciarContato(ResultSet rs) throws SQLException {
		return new Contato(
				rs.getInt("Id"),
				rs.getString("Email"),
				rs.getString("TelefoneFixo"),
				rs.getString("TelefoneCelular"));
	}
	
	protected static Endereco instanciarEndereco(ResultSet rs) throws SQLException{
		return new Endereco(
				rs.getInt("Id"),
				rs.getString("Logradouro"),
				rs.getString("Bairro"),
				rs.getString("Cidade"),
				rs.getString("Cep"),
				rs.getInt("Uf"));
	}
	
	private static Cliente instanciarCliente(ResultSet rs, Contato contato, Endereco endereco) throws SQLException {
		return new Cliente(
				rs.getInt("Id"),
				rs.getString("Nome"),
				contato,
				endereco);
	}
}
