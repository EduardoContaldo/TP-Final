package javafxmvc.model.dao;

import javafx.scene.control.Alert;
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

        }
        return retorno;
    }
}
