package javafxmvc.model.dao;

import javafxmvc.model.domain.Marca;
import javafxmvc.model.domain.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Marca marca) {
        String query = "insert into marca(nome_marca)value(?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, marca.getNomeMarca());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean alterar(Marca marca) {
        String sql = "UPDATE marca SET nome_marca=? WHERE id_marca=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca.getNomeMarca());
            stmt.setDouble(2, marca.getIdMarca());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean remover(Marca marca) {
        String sql = "DELETE FROM marca WHERE id_marca=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, marca.getIdMarca());
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Marca> listar() {
        String query = "SELECT id_marca, nome_marca FROM marca ORDER BY nome_marca ASC";
        List<Marca> retorno = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Marca marca = new Marca();
                marca.setIdMarca(resultado.getInt("id_marca"));
                marca.setNomeMarca(resultado.getString("nome_marca"));
                retorno.add(marca);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos");
        }
        return retorno;
    }

    public Marca buscar(Marca marca) {
        String query = "SELECT nome_marca FROM marca WHERE id_marca=?";
        Marca retorno = new Marca();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, marca.getIdMarca());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                marca.setNomeMarca(resultado.getString("id_produto"));
                retorno = marca;
            }
        } catch (SQLException e) {
            System.out.println("Marca não encontrado");
        }
        return retorno;
    }

    public boolean verificarMarca(Marca marca){
        String query = "SELECT COUNT(nome_marca) AS confirmar FROM marca WHERE nome_marca=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, marca.getNomeMarca());
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