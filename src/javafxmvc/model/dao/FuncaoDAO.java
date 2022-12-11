package javafxmvc.model.dao;

import javafxmvc.model.domain.Funcao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncaoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Funcao funcao) {
        String query = "insert into funcao(nome_funcao)value(?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, funcao.getNomeFuncao());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean alterar(Funcao funcao) {
        String sql = "UPDATE funcao SET nome_funcao=? WHERE id_funcao=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcao.getNomeFuncao());
            stmt.setDouble(2, funcao.getIdFuncao());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean remover(Funcao funcao) {
        String sql = "DELETE FROM funcao WHERE id_funcao=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, funcao.getIdFuncao());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Funcao> listar() {
        String query = "SELECT id_funcao, nome_funcao FROM funcao ORDER BY nome_funcao ASC";
        List<Funcao> retorno = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Funcao funcao = new Funcao();
                funcao.setIdFuncao(resultado.getInt("id_funcao"));
                funcao.setNomeFuncao(resultado.getString("nome_funcao"));
                retorno.add(funcao);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar movimentação");
        }
        return retorno;
    }
}
