package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.CategoriaDAO;
import model.entities.Categoria;

public class CategoriaDaoJDBC implements CategoriaDAO {

	private Connection con;

	public CategoriaDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Categoria obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO categoria(nome) VALUES(?)");
			st.setString(1, obj.getNome());
			System.out.println((st.executeUpdate() > 0) ? "Categoria inserida com sucesso."
					: "Não foi possível inserir categoria.");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Categoria obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE categoria SET nome = ? WHERE id = ?");
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());

			System.out.println((st.executeUpdate() > 0) ? "Categoria alterada com sucesso."
					: "Não foi possível alterar categoria");
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
			st = con.prepareStatement("DELETE FROM categoria WHERE id = ?");
			st.setInt(1, id);

			System.out.println((st.executeUpdate() > 0) ? "Categoria excluída com sucesso."
					: "Não foi possível excluir categoria");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Categoria findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM categoria WHERE id = ?");
			st.setInt(1, id);

			rs = st.executeQuery();
			return (rs.next()) ? new Categoria(rs.getInt("Id"), rs.getString("Nome")) : null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Categoria> findByName(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Categoria> list = new ArrayList<>();
		try {
			st = con.prepareStatement("SELECT * FROM categoria WHERE nome like ?");
			st.setString(1, "%" + nome + "%");

			rs = st.executeQuery();
			while (rs.next()) {
				list.add(new Categoria(rs.getInt("Id"), rs.getString("Nome")));
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
	public List<Categoria> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Categoria> list = new ArrayList<>();
		try {
			st = con.prepareStatement("SELECT * FROM categoria");
			rs = st.executeQuery();
			while (rs.next()) {
				list.add(new Categoria(rs.getInt("Id"), rs.getString("Nome")));
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
