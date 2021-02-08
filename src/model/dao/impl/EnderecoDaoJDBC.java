package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.EnderecoDAO;
import model.entities.Endereco;

public class EnderecoDaoJDBC implements EnderecoDAO {

	private Connection con;

	public EnderecoDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Endereco obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(
					"INSERT INTO endereco(logradouro, bairro, cidade, cep, uf) VALUES(?, ?, ?, ?, ?)");
			st.setString(1, obj.getLogradouro());
			st.setString(2, obj.getBairro());
			st.setString(3, obj.getCidade());
			st.setString(4, obj.getCep());
			st.setInt(5, obj.getUf().getCodigo());

			if (st.executeUpdate() > 0) {
				System.out.println("Dados de endereço adicionados com sucesso.");
			} else {
				System.out.println("Não foi possível adicionar dados de endereço.");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Endereco obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE endereco SET " + "logradouro = ?, " + "bairro = ?, " + "cidade = ?, "
					+ "cep = ?, " + "uf = ? " + "WHERE id = ?");
			st.setString(1, obj.getLogradouro());
			st.setString(2, obj.getBairro());
			st.setString(3, obj.getCidade());
			st.setString(4, obj.getCep());
			st.setInt(5, obj.getUf().getCodigo());
			st.setInt(6, obj.getId());

			if (st.executeUpdate() > 0) {
				System.out.println("Dados de endereço atualizados com sucesso.");
			} else {
				System.out.println("Não foi possível atualizar os dados de endereço.");
			}
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
			st = con.prepareStatement("DELETE FROM endereco WHERE id = ?");
			st.setInt(1, id);

			if (st.executeUpdate() > 0) {
				System.out.println("Dados de endereço excluídos com sucesso.");
			} else {
				System.out.println("Não foi possível excluír os dados de endereço.");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}
}
