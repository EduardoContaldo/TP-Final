package javafxmvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.MainApplication;
import javafxmvc.model.dao.MarcaDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Marca;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerMenuMarca implements Initializable {

    @FXML
    private TableView<Marca> tableViewMarcas;
    @FXML
    private TableColumn<Marca, String> tableColumnMarcaNome;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelMarcaNome;


    private List<Marca> listMarcas;
    private ObservableList<Marca> observableListMarcas;

    //Atributos para manipulação de Banco de Dados
    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.marcaDAO.setConnection(this.connection);
        carregarTableViewMarca();

        tableViewMarcas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableMarcas(newValue));
    }

    public void carregarTableViewMarca() {
        this.tableColumnMarcaNome.setCellValueFactory(new PropertyValueFactory<>("nomeMarca"));

        this.listMarcas = this.marcaDAO.listar();

        this.observableListMarcas = FXCollections.observableArrayList(this.listMarcas);
        this.tableViewMarcas.setItems(this.observableListMarcas);
    }

    public void selecionarItemTableMarcas(Marca marca) {
        if (marca != null) {
            this.labelMarcaNome.setText(String.valueOf(marca.getNomeMarca()));
        }
    }

    @FXML
    public void deletarMarca(){
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1 || ControllerMenuPrincipal.usuario.getIdFuncao() == 2) {
            Marca marca = this.tableViewMarcas.getSelectionModel().getSelectedItem();
            if (marca != null) {
                Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacao.setTitle("Confirmar Exclusão");
                confirmacao.setContentText("Deseja confirmar a exclusão dessa marca?");
                Optional<ButtonType> resultado = confirmacao.showAndWait();

                if(resultado.get() == ButtonType.OK) {
                    if(!marcaDAO.remover(marca)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Problema de Exclusão");
                        alert.setContentText("Não foi possível excluir a marca pois existem produtos pertencentes a ela no sistema!");
                        alert.show();
                    }
                    carregarTableViewMarca();
                }
            }
        }
    }

    @FXML
    public void alterarMarca() throws IOException {
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1 || ControllerMenuPrincipal.usuario.getIdFuncao() == 2) {
            Marca marca = this.tableViewMarcas.getSelectionModel().getSelectedItem();
            String path = "/javafxmvc/view/FXMLFormularioMarca.fxml";
            String titulo = "Alterar Marca";

            boolean confirmacao = MainApplication.showArquivoXML(path, titulo, marca, "alterar");
            if (confirmacao) {
                this.marcaDAO.setConnection(this.connection);
                this.marcaDAO.alterar(marca);
                carregarTableViewMarca();
            }
        }
    }
}