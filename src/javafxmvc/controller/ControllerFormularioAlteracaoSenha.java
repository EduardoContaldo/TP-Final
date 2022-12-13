package javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafxmvc.model.dao.FuncionarioDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Funcionario;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ControllerFormularioAlteracaoSenha implements Initializable {

    @FXML
    private PasswordField passwordFieldSenhaAtual;
    @FXML
    private PasswordField passwordFieldNovaSenha;
    @FXML
    private Button buttonConfirmar;

    private Stage dialogStage;
    private boolean confirmar = false;

    private Funcionario usuario;

    public Funcionario getUsuario() {
        return usuario;
    }

    public void setUsuario(Funcionario usuario) {
        this.usuario = usuario;
    }

    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();


    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isConfirmar() {
        return confirmar;
    }

    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void confirmarAlteracao(){
        if (validarEntradaDeDados()) {
            this.usuario.setSenha(this.funcionarioDAO.getHashMd5(this.passwordFieldNovaSenha.getText()));
            this.confirmar = true;

            this.dialogStage.close();
        }
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        this.usuario.setSenha(this.funcionarioDAO.getHashMd5(this.passwordFieldSenhaAtual.getText()));
        this.funcionarioDAO.setConnection(this.connection);
        if(this.funcionarioDAO.autenticar(this.usuario).getNome() == null){
            errorMessage += "Senha incorreta!\n";
        }
        if (this.passwordFieldNovaSenha.getText() == null || this.passwordFieldNovaSenha.getText().length() == 0) {
            errorMessage += "Nova senha inválida!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro na alteração");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}