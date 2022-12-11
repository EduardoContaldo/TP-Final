package javafxmvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.MovimentacaoDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Movimentacao;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerRelatorioFluxoProduto implements Initializable {

    @FXML
    private TableView<Movimentacao> tableViewFluxoProduto;
    @FXML
    private TableColumn<Movimentacao, String> tableColumnProduto;
    @FXML
    private TableColumn<Movimentacao, String> tableColumnMarca;
    @FXML
    private TableColumn<Movimentacao, String> tableColumnQuantidade;
    @FXML
    private TableColumn<Movimentacao, String> tableColumnFuncionario;
    @FXML
    private TableColumn<Movimentacao, String> tableColumnData;


    private List<Movimentacao> listProdutos;
    private ObservableList<Movimentacao> observableListProdutos;

    //Atributos para manipulação de Banco de Dados
    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.movimentacaoDAO.setConnection(this.connection);
        carregarTableViewFluxoProduto();
    }

    public void carregarTableViewFluxoProduto(){
        this.tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        this.tableColumnMarca.setCellValueFactory(new PropertyValueFactory<>("nomeMarca"));
        this.tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        this.tableColumnFuncionario.setCellValueFactory(new PropertyValueFactory<>("nomeFuncionario"));
        this.tableColumnData.setCellValueFactory(new PropertyValueFactory<>("data"));

        this.listProdutos = this.movimentacaoDAO.listar();

        this.observableListProdutos = FXCollections.observableArrayList(this.listProdutos);
        this.tableViewFluxoProduto.setItems(this.observableListProdutos);
    }
}
