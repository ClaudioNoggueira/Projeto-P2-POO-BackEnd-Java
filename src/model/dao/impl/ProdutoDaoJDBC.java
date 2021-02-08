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
import model.dao.ProdutoDAO;
import model.entities.Categoria;
import model.entities.Produto;

public class ProdutoDaoJDBC implements ProdutoDAO {

	private Connection con;

	public ProdutoDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Produto obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO produto(nome, descricao, preco, dataAdicao) VALUES(?, ?, ?, ?)");
			st.setString(1, obj.getNome());
			st.setString(2, obj.getDescricao());
			st.setDouble(3, obj.getPreco());
			st.setDate(4, new java.sql.Date(obj.getDataAdicao().getTime()));

			System.out.println(
					(st.executeUpdate() > 0) ? "Produto inserido com sucesso." : "Não foi possível inserir produto.");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Produto obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE produto SET " + "nome = ?, " + "descricao = ?, " + "preco = ?, "
					+ "dataAdicao = ? " + "WHERE id = ? ");

			st.setString(1, obj.getNome());
			st.setString(2, obj.getDescricao());
			st.setDouble(3, obj.getPreco());
			st.setDate(4, new java.sql.Date(obj.getDataAdicao().getTime()));
			st.setInt(5, obj.getId());

			System.out.println(
					(st.executeUpdate() > 0) ? "Produto alterado com sucesso." : "Não foi possível alterar produto.");
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
			st = con.prepareStatement("DELETE FROM produto WHERE id = ?");

			st.setInt(1, id);

			System.out.println(
					(st.executeUpdate() > 0) ? "Produto excluído com sucesso." : "Não foi possível excluir produto.");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Produto findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement(
					"select produto.*, categoria.* from produto "
					+ "inner join produto_categoria as prod_cat on prod_cat.idProduto = produto.id "
				    + "inner join categoria on prod_cat.idCategoria = categoria.id "
				    + "where produto.id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			return (rs.next()) ? instanciarProduto(rs, instanciarCategoria(rs)) : null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Produto> findByName(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Produto> list = new ArrayList<>();
		Map<Integer, Categoria> map = new HashMap<>();
		try {
			st = con.prepareStatement(
					"select produto.*, categoria.* from produto "
					+ "inner join produto_categoria as prod_cat on prod_cat.idProduto = produto.id "
				    + "inner join categoria on prod_cat.idCategoria = categoria.id "
				    + "where produto.nome like ? "
				    + "order by produto.nome");
			st.setString(1, "%" + nome + "%");
			
			rs = st.executeQuery();
			while(rs.next()) {
				Categoria cat = map.get(rs.getInt("categoria.id"));
				if(cat == null) {
					cat = instanciarCategoria(rs);
					map.put(rs.getInt("categoria.id"), cat);
				}
				list.add(instanciarProduto(rs, cat));
			}
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Produto> findByCategory(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Produto> list = new ArrayList<>();
		Map<Integer, Categoria> map = new HashMap<>();
		try {
			st = con.prepareStatement(
					"select produto.*, categoria.* from produto "
					+ "inner join produto_categoria as prod_cat on prod_cat.idProduto = produto.id "
				    + "inner join categoria on prod_cat.idCategoria = categoria.id "
				    + "where categoria.nome like ? "
				    + "order by categoria.nome");
			
			st.setString(1, "%" + nome + "%");
			
			rs = st.executeQuery();
			
			while(rs.next()) {
				Categoria cat = map.get(rs.getInt("categoria.id"));
				if(cat == null) {
					cat = instanciarCategoria(rs);
					map.put(rs.getInt("categoria.id"), cat);
				}
				list.add(instanciarProduto(rs, cat));
			}
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Produto> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Produto> list = new ArrayList<>();
		Map<Integer, Categoria> map = new HashMap<>();
		try {
			st = con.prepareStatement(
					"select produto.*, categoria.* from produto "
					+ "inner join produto_categoria as prod_cat on prod_cat.idProduto = produto.id "
				    + "inner join categoria on prod_cat.idCategoria = categoria.id");
			
			rs = st.executeQuery();
			while(rs.next()) {
				Categoria cat = map.get(rs.getInt("categoria.id"));
				if(cat == null) {
					cat = instanciarCategoria(rs);
					map.put(rs.getInt("categoria.id"), cat);
				}
				list.add(instanciarProduto(rs, cat));
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private static Categoria instanciarCategoria(ResultSet rs) throws SQLException {
		return new Categoria(
				rs.getInt("categoria.Id"),
				rs.getString("categoria.nome"));
	}
	
	private static Produto instanciarProduto(ResultSet rs, Categoria categoria) throws SQLException {
		return new Produto(
				rs.getInt("produto.id"),
				rs.getString("produto.nome"),
				rs.getString("produto.descricao"),
				rs.getDouble("produto.preco"),
				rs.getDate("produto.dataAdicao"),
				categoria);
	}
}
