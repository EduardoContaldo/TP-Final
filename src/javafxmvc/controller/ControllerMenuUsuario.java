package javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafxmvc.MainApplication;
import javafxmvc.model.dao.FuncionarioDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Funcionario;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ControllerMenuUsuario implements Initializable {
    @FXML
    private Label labelUsuario;
    @FXML
    private Label labelNome;
    @FXML
    private Button buttonAlterarUsuario;
    @FXML
    private Button buttonAlterarSenha;

    Funcionario usuario;

    public Funcionario getFuncionario() {
        return usuario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.usuario = funcionario;
    }

    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setFuncionario(ControllerMenuPrincipal.usuario);
        this.labelUsuario.setText(this.usuario.getUsuario());
        this.labelNome.setText(this.usuario.getNome());
    }

    @FXML
    public void alterarUsuario() throws IOException {
        String path = "/javafxmvc/view/FXMLFormularioAlteracaoUsuario.fxml";
        String titulo = "Alterar Funcionário";

        boolean confirmacao = MainApplication.showArquivoXML(path, titulo, this.usuario, "alterarUsuario");
        if (confirmacao) {
            this.funcionarioDAO.setConnection(this.connection);
            this.funcionarioDAO.alterarUsuario(this.usuario);
        }
    }

    @FXML
    public void alterarSenha() throws IOException {
        String path = "/javafxmvc/view/FXMLFormularioAlteracaoSenha.fxml";
        String titulo = "Alterar Funcionário";

        boolean confirmacao = MainApplication.showArquivoXML(path, titulo, this.usuario, "alterarSenha");
        if (confirmacao) {
            this.funcionarioDAO.setConnection(this.connection);
            this.funcionarioDAO.alterarSenha(this.usuario);
            this.usuario.setSenha(null);
        }

    }

}
