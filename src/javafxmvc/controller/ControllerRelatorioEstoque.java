package javafxmvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Produto;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerRelatorioEstoque implements Initializable {

    @FXML
    private TableView<Produto> tableViewEstoque;
    @FXML
    private TableColumn<Produto, String> tableColumnProduto;
    @FXML
    private TableColumn<Produto, String> tableColumnMarca;
    @FXML
    private TableColumn<Produto, String> tableColumnQuantidade;
    @FXML
    private TableColumn<Produto, String> tableColumnPreco;

    private List<Produto> listProdutos;
    private ObservableList<Produto> observableListProdutos;

    //Atributos para manipulação de Banco de Dados
    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.produtoDAO.setConnection(this.connection);
        carregarTableViewProduto();
    }

    public void carregarTableViewProduto(){
        this.tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        this.tableColumnMarca.setCellValueFactory(new PropertyValueFactory<>("nomeMarca"));
        this.tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        this.tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("valor"));

        this.listProdutos = this.produtoDAO.listar();

        this.observableListProdutos = FXCollections.observableArrayList(this.listProdutos);
        this.tableViewEstoque.setItems(this.observableListProdutos);
    }
}
