package javafxmvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.MainApplication;
import javafxmvc.model.dao.FuncionarioDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Funcionario;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerMenuFuncionario implements Initializable {

    @FXML
    private TableView<Funcionario> tableViewFuncionarios;
    @FXML
    private TableColumn<Funcionario, String> tableColumnNomeFuncionario;
    @FXML
    private TableColumn<Funcionario, String> tableColumnSobreNomeFuncionario;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelNomeFuncionario;
    @FXML
    private Label labelSobreNomeFuncionario;
    @FXML
    private Label labelFuncaoFuncionario;
    @FXML
    private Label labelUsuarioFuncionario;

    private List<Funcionario> listFuncionarios;
    private ObservableList<Funcionario> observableListFuncionarios;

    //Atributos para manipulação de Banco de Dados
    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.funcionarioDAO.setConnection(this.connection);
        carregarTableViewFuncionario();

        tableViewFuncionarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableProdutos(newValue));
    }

    public void carregarTableViewFuncionario(){
        this.tableColumnNomeFuncionario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.tableColumnSobreNomeFuncionario.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));

        this.listFuncionarios = this.funcionarioDAO.listar();

        this.observableListFuncionarios = FXCollections.observableArrayList(this.listFuncionarios);
        this.tableViewFuncionarios.setItems(this.observableListFuncionarios);
    }

    public void selecionarItemTableProdutos(Funcionario funcionario){
        if(funcionario != null) {
            this.labelNomeFuncionario.setText(String.valueOf(funcionario.getNome()));
            this.labelSobreNomeFuncionario.setText(String.valueOf(funcionario.getSobrenome()));
            this.labelFuncaoFuncionario.setText(String.valueOf(funcionario.getFuncao()));
            this.labelUsuarioFuncionario.setText(String.valueOf(funcionario.getUsuario()));
        }
    }

    @FXML
    public void deletarFuncionario(){
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1) {
            Funcionario funcionario = this.tableViewFuncionarios.getSelectionModel().getSelectedItem();

            if (funcionario != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar Exclusão");
                alert.setContentText("Deseja confirmar a exclusão desse funcionário?");
                Optional<ButtonType> resultado = alert.showAndWait();

                if(resultado.get() == ButtonType.OK) {
                    if(!funcionarioDAO.remover(funcionario)){
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Não foi possível remover o funcionário!");
                        alert.showAndWait();
                    }
                    carregarTableViewFuncionario();
                }
            }
        }
    }

    @FXML
    public void alterarFuncionario() throws IOException {
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1) {
            Funcionario funcionario = this.tableViewFuncionarios.getSelectionModel().getSelectedItem();
            String path = "/javafxmvc/view/FXMLFormularioFuncionario.fxml";
            String titulo = "Alterar Funcionário";

            boolean confirmacao = MainApplication.showArquivoXML(path, titulo, funcionario, "alterar");
            if (confirmacao) {
                this.funcionarioDAO.setConnection(this.connection);
                if(!this.funcionarioDAO.alterar(funcionario)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Não foi possível alterar o funcionário!");
                    alert.showAndWait();
                }
                carregarTableViewFuncionario();
            }
        }
    }

}