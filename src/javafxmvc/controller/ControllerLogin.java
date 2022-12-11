package javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafxmvc.model.dao.FuncionarioDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Funcionario;

import java.io.IOException;
import java.sql.Connection;

public class ControllerLogin {

    @FXML
    private TextField textFieldUsuario;

    @FXML
    private PasswordField textFieldSenha;

    @FXML
    private Button buttonEntrar;

    @FXML
    private Label labelResultado;

    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    private Funcionario loginFuncionario = new Funcionario();
    private Funcionario funcionarioLogado = new Funcionario();


    private Parent parent;
    private Scene scene;
    private Stage stage;
    private ControllerMenuPrincipal menuController;

    public ControllerLogin(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/javafxmvc/view/FXMLLogin.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = fxmlLoader.load();
            scene = new Scene(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchLoginController(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Login");
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.hide();
        this.stage.show();
    }

    @FXML
    void autenticarLogin(){
        this.funcionarioDAO.setConnection(this.connection);
        this.loginFuncionario.setUsuario(this.textFieldUsuario.getText());
        this.loginFuncionario.setSenha(this.funcionarioDAO.getHashMd5(this.textFieldSenha.getText()));

        this.funcionarioLogado = this.funcionarioDAO.autenticar(this.loginFuncionario);
        if(this.loginFuncionario.getNome() == null){
            this.labelResultado.setText("Usuário e/ou senha incorreto(s)");
        }
        else{
            this.menuController = new ControllerMenuPrincipal();
            this.menuController.redirectMenu(stage, funcionarioLogado);
        }
    }
}
