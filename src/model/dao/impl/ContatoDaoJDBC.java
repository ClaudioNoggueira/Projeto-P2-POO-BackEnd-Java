package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.ContatoDAO;
import model.entities.Contato;

public class ContatoDaoJDBC implements ContatoDAO {

	private Connection con;

	public ContatoDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Contato obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO contato(email, telefoneFixo, telefoneCelular) VALUES(?, ?, ?)");
			st.setString(1, obj.getEmail());
			st.setString(2, obj.getTelefoneFixo());
			st.setString(3, obj.getTelefoneCelular());

			if (st.executeUpdate() > 0) {
				System.out.println("Contato inserido com sucesso");
			} else {
				System.out.println("Erro ao inserir contato.");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Contato obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE contato SET "
					+ "email = ?, "
					+ "telefoneFixo = ?, "
					+ "telefoneCelular = ? "
					+ "WHERE id = ?");
			st.setString(1, obj.getEmail());
			st.setString(2, obj.getTelefoneFixo());
			st.setString(3, obj.getTelefoneCelular());
			st.setInt(4, obj.getId());
			
			if(st.executeUpdate() > 0) {
				System.out.println("Dados de contato alterados com sucesso.");
			}else {
				System.out.println("Não foi possível alterar os dados de contato.");
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
			st = con.prepareStatement("DELETE FROM cliente WHERE id = ?");
			st.setInt(1, id);
			
			if(st.executeUpdate() > 0) {
				System.out.println("Dados de contato excluídos com sucesso.");
			}else {
				System.out.println("Não foi possível excluír dados de contato.");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}
}
