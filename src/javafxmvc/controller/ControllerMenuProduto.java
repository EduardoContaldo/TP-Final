package javafxmvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.MainApplication;
import javafxmvc.model.dao.MovimentacaoDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Movimentacao;
import javafxmvc.model.domain.Produto;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerMenuProduto implements Initializable {

    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoNome;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoMarca;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelProdutoNome;
    @FXML
    private Label labelProdutoQuantidade;
    @FXML
    private Label labelProdutoValor;
    @FXML
    private Label labelProdutoMarca;

    private List<Produto> listProdutos;
    private ObservableList<Produto> observableListProdutos;

    //Atributos para manipulação de Banco de Dados
    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.produtoDAO.setConnection(this.connection);
        carregarTableViewProduto();

        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableProdutos(newValue));
    }

    public void carregarTableViewProduto(){
        this.tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        this.tableColumnProdutoMarca.setCellValueFactory(new PropertyValueFactory<>("nomeMarca"));

        this.listProdutos = this.produtoDAO.listar();

        this.observableListProdutos = FXCollections.observableArrayList(this.listProdutos);
        this.tableViewProdutos.setItems(this.observableListProdutos);
    }

    public void selecionarItemTableProdutos(Produto produto){
        if(produto != null) {
            this.labelProdutoNome.setText(String.valueOf(produto.getNomeProduto()));
            this.labelProdutoQuantidade.setText(String.valueOf(produto.getQuantidade()));
            this.labelProdutoValor.setText(String.valueOf(produto.getValor()));
            this.labelProdutoMarca.setText(String.valueOf(produto.getNomeMarca()));
        }
    }

    public void deletarProduto(){
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1 || ControllerMenuPrincipal.usuario.getIdFuncao() == 2) {
            Produto produto = this.tableViewProdutos.getSelectionModel().getSelectedItem();

            if (produto != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar Exclusão");
                alert.setContentText("Deseja confirmar a exclusão desse produto?");
                Optional<ButtonType> resultado = alert.showAndWait();

                if(resultado.get() == ButtonType.OK) {
                    this.movimentacaoDAO.setConnection(this.connection);
                    if(this.movimentacaoDAO.remover(produto)) {
                        if (!this.produtoDAO.remover(produto)) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erro na Exclusão");
                            alert.setContentText("Não foi possível excluir o produto!");
                            alert.show();
                        }
                    }
                    carregarTableViewProduto();
                }
            }
        }
    }

    @FXML
    public void alterarProduto() throws IOException {
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1 || ControllerMenuPrincipal.usuario.getIdFuncao() == 2) {
            Produto produto = this.tableViewProdutos.getSelectionModel().getSelectedItem();
            String path = "/javafxmvc/view/FXMLFormularioProduto.fxml";
            String titulo = "Alterar Produto";

            boolean confirmacao = MainApplication.showArquivoXML(path, titulo, produto, "alterar");
            if (confirmacao) {
                this.produtoDAO.setConnection(this.connection);
                if(!this.produtoDAO.alterar(produto)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Não foi possível alterar o produto!");
                    alert.showAndWait();
                }
                carregarTableViewProduto();
            }
        }
    }

    @FXML
    public void inserirProduto() throws IOException {
        Produto produto = this.tableViewProdutos.getSelectionModel().getSelectedItem();
        String path = "/javafxmvc/view/FXMLInsercaoProduto.fxml";
        String titulo = "Inserir Produto";

        boolean confirmacao = MainApplication.showArquivoXML(path, titulo, produto, "inserir");
        if (confirmacao) {
            int tipoMovimentacao;
            if(produto.getNovaQuantidade() < 0)
                tipoMovimentacao = 0;
            else
                tipoMovimentacao = 1;

            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setIdProduto(produto.getIdProduto());
            movimentacao.setQuantidade(produto.getNovaQuantidade());
            movimentacao.setTipoMovimentacao(tipoMovimentacao);

            this.produtoDAO.setConnection(this.connection);
            this.movimentacaoDAO.setConnection(this.connection);

            this.produtoDAO.alterarQuantidade(produto);
            if(!this.movimentacaoDAO.inserir(movimentacao)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Não foi possível inserir o produto!");
                alert.showAndWait();
            }

            carregarTableViewProduto();
        }
    }
}