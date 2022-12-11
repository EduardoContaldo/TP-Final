package javafxmvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.dao.FuncaoDAO;
import javafxmvc.model.dao.FuncionarioDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Funcao;
import javafxmvc.model.domain.Funcionario;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFormularioFuncionario implements Initializable {

    @FXML
    private TextField textFieldNomeFuncionario;
    @FXML
    private TextField textFieldSobreNomeFuncionario;
    @FXML
    private TextField textFieldUsuarioFuncionario;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private ComboBox<Funcao> comboBoxFuncoes;

    private List<Funcao> listFuncoes = new ArrayList<>();
    private ObservableList<Funcao> observableListFuncoes;

    private Stage dialogStage;
    private boolean confirmar = false;
    private Funcionario funcionario;
    private Funcao funcao;

    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final FuncaoDAO funcaoDAO = new FuncaoDAO();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();


    public Funcionario getFuncao() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.textFieldNomeFuncionario.setText(this.funcionario.getNome());
        this.textFieldSobreNomeFuncionario.setText(this.funcionario.getSobrenome());
        this.textFieldUsuarioFuncionario.setText(this.funcionario.getUsuario());
    }

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
        carregarComboBoxFuncoes();
    }

    public void carregarComboBoxFuncoes() {
        this.funcaoDAO.setConnection(this.connection);
        this.listFuncoes = this.funcaoDAO.listar();
        this.observableListFuncoes = FXCollections.observableArrayList(this.listFuncoes);
        this.comboBoxFuncoes.setItems(this.observableListFuncoes);
    }

    @FXML
    public void selecionarItemComboBoxFuncoes() {
        this.funcao = this.comboBoxFuncoes.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void confirmarAlteracao(){
        if (validarEntradaDeDados()) {
            this.funcionario.setNome(this.textFieldNomeFuncionario.getText());
            this.funcionario.setSobrenome(this.textFieldSobreNomeFuncionario.getText());
            this.funcionario.setUsuario(this.textFieldUsuarioFuncionario.getText());
            this.funcionario.setIdFuncao(this.funcao.getIdFuncao());

            this.confirmar = true;
            this.dialogStage.close();
        }
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        this.funcionario.setUsuario(this.textFieldUsuarioFuncionario.getText());
        this.funcionarioDAO.setConnection(this.connection);
        if(this.funcionarioDAO.verificarUsuario(this.funcionario)){
            errorMessage += "Usuário já existe!\n";
        }
        if (this.textFieldNomeFuncionario.getText() == null || this.textFieldNomeFuncionario.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (this.textFieldSobreNomeFuncionario.getText() == null || this.textFieldSobreNomeFuncionario.getText().length() == 0) {
            errorMessage += "Sobrenome inválido!\n";
        }
        if (this.textFieldUsuarioFuncionario.getText() == null || this.textFieldUsuarioFuncionario.getText().length() == 0) {
            errorMessage += "Usuário inválido!\n";
        }
        if (this.funcao == null) {
            errorMessage += "Função inválida!\n";
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