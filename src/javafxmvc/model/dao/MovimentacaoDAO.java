package javafxmvc.model.dao;

import javafxmvc.model.domain.Movimentacao;
import javafxmvc.model.domain.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Movimentacao movimentacao) {
        String query = "insert into movimentacao_produto (id_produto, quantidade, tipo_movimentacao)" +
        "value (?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, movimentacao.getIdProduto());
            stmt.setInt(2, movimentacao.getQuantidade());
            stmt.setInt(3, movimentacao.getTipoMovimentacao());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean remover(Produto produto) {
        String sql = "DELETE FROM movimentacao_produto WHERE id_produto=?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, produto.getIdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {

            return false;
        }
    }

    public List<Movimentacao> listar() {
        String query = "select nome_produto, nome_marca, mp.quantidade, DATE_FORMAT(data, '%d/%m/%Y %H:%i') as data " +
                "from movimentacao_produto as mp inner join produto as p on mp.id_produto = p.id_produto inner join " +
                "marca as m on m.id_marca = p.id_marca order by data desc";
        List<Movimentacao> retorno = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Movimentacao movimentacao = new Movimentacao();
                movimentacao.setNomeProduto(resultado.getString("nome_produto"));
                movimentacao.setNomeMarca(resultado.getString("nome_marca"));
                movimentacao.setQuantidade(resultado.getInt("quantidade"));
                movimentacao.setData(resultado.getString("data"));

                retorno.add(movimentacao);
            }
        } catch (SQLException e) {
        }
        return retorno;
    }
}
