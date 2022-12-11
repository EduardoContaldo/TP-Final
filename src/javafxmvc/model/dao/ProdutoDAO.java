package javafxmvc.model.dao;

import javafxmvc.model.domain.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Produto produto){
        String query = "insert into produto(nome_produto, valor, id_marca, quantidade)value(?, ?, ?, 0)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, produto.getNomeProduto());
            stmt.setDouble(2, produto.getValor());
            stmt.setInt(3, produto.getIdMarca());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean alterar(Produto produto) {
        String sql = "UPDATE produto SET nome_produto=?, valor=?, id_marca=? WHERE id_produto=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getNomeProduto());
            stmt.setDouble(2, produto.getValor());
            stmt.setInt(3, produto.getIdMarca());
            stmt.setInt(4, produto.getIdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean alterarQuantidade(Produto produto) {
        String sql = "UPDATE produto SET quantidade=? WHERE id_produto=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, produto.getQuantidade());
            stmt.setDouble(2, produto.getIdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean remover(Produto produto) {
        String sql = "DELETE FROM produto WHERE id_produto=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, produto.getIdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public List<Produto> listar() {
        String query = "SELECT id_produto, nome_produto, p.id_marca, valor, quantidade, nome_marca FROM produto as p inner join marca as m on p.id_marca = m.id_marca ORDER BY nome_produto ASC";
        List<Produto> retorno = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Produto produto = new Produto();
                produto.setIdProduto(resultado.getInt("id_produto"));
                produto.setNomeProduto(resultado.getString("nome_produto"));
                produto.setIdMarca(resultado.getInt("id_marca"));
                produto.setValor(resultado.getDouble("valor"));
                produto.setQuantidade(resultado.getInt("quantidade"));
                produto.setNomeMarca(resultado.getString("nome_marca"));
                retorno.add(produto);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao listar produtos");
        }
        return retorno;
    }

    public Produto buscar(Produto produto) {
        String query = "SELECT id_produto, nome_produto, p.id_marca, valor, quantidade, nome_marca FROM produto as p inner join marca as m on p.id_marca = m.id_marca WHERE id_produto=?";
        Produto retorno = new Produto();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, produto.getIdProduto());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                produto.setIdProduto(resultado.getInt("id_produto"));
                produto.setNomeProduto(resultado.getString("nome_produto"));
                produto.setIdMarca(resultado.getInt("id_marca"));
                produto.setValor(resultado.getDouble("valor"));
                produto.setQuantidade(resultado.getInt("quantidade"));
                produto.setNomeMarca(resultado.getString("nome_marca"));
                retorno = produto;
            }
        } catch (SQLException ex) {
            System.out.println("Produto não encontrado");
        }
        return retorno;
    }

    public boolean verificarProduto(Produto produto){
        String query = "SELECT COUNT(nome_produto) AS confirmar FROM produto WHERE nome_produto=? AND id_marca=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, produto.getNomeProduto());
            stmt.setInt(2, produto.getIdMarca());
            ResultSet resultado = stmt.executeQuery();
            int retorno = 1;
            if (resultado.next()) {
                retorno = resultado.getInt("confirmar");
            }

            if(retorno == 0)
                return false;
            else
                return true;
        }
        catch (SQLException ex) {
            return false;
        }
    }
}
