package javafxmvc.model.dao;

import javafxmvc.model.domain.Funcionario;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Funcionario autenticar(Funcionario funcionario) {
        String query = "select id_usuario, l.id_funcionario, id_funcao, nome " +
        "from login as l inner join funcionario as f on l.id_funcionario = f.id_funcionario " +
        "where login_usuario = ? and senha = ?";
        Funcionario retorno = new Funcionario();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, funcionario.getUsuario());
            stmt.setString(2, funcionario.getSenha());

            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                funcionario.setIdUsuario(resultado.getInt("id_usuario"));
                funcionario.setNome(resultado.getString("nome"));
                funcionario.setIdFuncionario(resultado.getInt("id_funcionario"));
                funcionario.setIdFuncao(resultado.getInt("id_funcao"));
                retorno = funcionario;
            }
        } catch (SQLException ex) {
            System.out.println("Usuario não encontrado");
        }
        return retorno;
    }

    public boolean inserir(Funcionario funcionario){
        String query = "insert into funcionario(nome, sobrenome, id_funcao)value(?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getSobrenome());
            stmt.setInt(3, funcionario.getIdFuncao());
            stmt.execute();

            query = "select id_funcionario from funcionario order by id_funcionario desc limit 1";
            stmt = connection.prepareStatement(query);
            ResultSet resultado = stmt.executeQuery();

            if(resultado.next()){
                int idFuncionario = resultado.getInt("id_funcionario");

                funcionario.setSenha(getHashMd5("123456"));

                query = "insert into login(login_usuario, senha, id_funcionario) value (?, ?, ?)";
                stmt = this.connection.prepareStatement(query);
                stmt.setString(1, funcionario.getUsuario());
                stmt.setString(2, funcionario.getSenha());
                stmt.setInt(3, idFuncionario);

                stmt.execute();
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean alterar(Funcionario funcionario) {
        String query = "UPDATE funcionario SET nome=?, sobrenome=?, id_funcao=? WHERE id_funcionario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getSobrenome());
            stmt.setInt(3, funcionario.getIdFuncao());
            stmt.setInt(4, funcionario.getIdFuncionario());
            stmt.execute();

            query = "UPDATE login SET login_usuario=? WHERE id_usuario=?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, funcionario.getUsuario());
            stmt.setInt(2, funcionario.getIdUsuario());
            stmt.execute();

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean alterarUsuario(Funcionario funcionario) {
        String query = "UPDATE login SET login_usuario=? WHERE id_usuario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, funcionario.getUsuario());
            stmt.setInt(2, funcionario.getIdUsuario());
            stmt.execute();

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean alterarSenha(Funcionario funcionario) {
        String query = "UPDATE login SET senha=? WHERE id_usuario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, funcionario.getSenha());
            stmt.setInt(2, funcionario.getIdUsuario());
            stmt.execute();

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean remover(Funcionario funcionario) {
        String query = "DELETE FROM login WHERE id_usuario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, funcionario.getIdUsuario());
            stmt.execute();

            query = "DELETE FROM funcionario WHERE id_funcionario=?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, funcionario.getIdFuncionario());
            stmt.execute();

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public List<Funcionario> listar() {
        String query = "SELECT f.id_funcionario, nome, sobrenome, f.id_funcao, l.login_usuario, l.id_usuario, fc.nome_funcao FROM funcionario as f inner join login as l on f.id_funcionario = l.id_funcionario inner join funcao as fc on f.id_funcao = fc.id_funcao ORDER BY nome ASC";
        List<Funcionario> retorno = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(resultado.getInt("id_funcionario"));
                funcionario.setNome(resultado.getString("nome"));
                funcionario.setSobrenome(resultado.getString("sobrenome"));
                funcionario.setIdFuncao(resultado.getInt("id_funcao"));
                funcionario.setUsuario(resultado.getString("login_usuario"));
                funcionario.setIdUsuario(resultado.getInt("id_usuario"));
                funcionario.setFuncao(resultado.getString("nome_funcao"));
                retorno.add(funcionario);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao listar funcionarios");
        }
        return retorno;
    }

    public boolean verificarUsuario(Funcionario funcionario){
        String query = "SELECT COUNT(login_usuario) AS confirmar FROM login WHERE login_usuario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, funcionario.getUsuario());
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

    public static String getHashMd5(String value) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        BigInteger hash = new BigInteger(1, md.digest(value.getBytes()));
        return hash.toString(16);
    }


}
