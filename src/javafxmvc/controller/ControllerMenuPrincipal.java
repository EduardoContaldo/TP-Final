package javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.MainApplication;
import javafxmvc.model.dao.FuncionarioDAO;
import javafxmvc.model.dao.MarcaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Funcionario;
import javafxmvc.model.domain.Marca;
import javafxmvc.model.domain.Produto;

import java.io.IOException;
import java.sql.Connection;

public class ControllerMenuPrincipal {
    @FXML
    private MenuItem menuCadastroFuncionario;
    @FXML
    private MenuItem menuCadastroProduto;
    @FXML
    private MenuItem menuCadastroMarca;
    @FXML
    private MenuItem menuAlteracaoProduto;
    @FXML
    private MenuItem menuAlteracaoMarca;
    @FXML
    private MenuItem menuRelatoriosEstoque;
    @FXML
    private MenuItem alterarUsuario;
    @FXML
    private MenuItem logoff;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label labelNomeUsuario;

    static Funcionario usuario;

    private Parent parent;
    private Scene scene;
    private Stage stage;

    //Atributos para manipulação de Banco de Dados
    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    public ControllerMenuPrincipal(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/javafxmvc/view/FXMLMenuPrincipal.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = fxmlLoader.load();
            scene = new Scene(parent, 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectMenu(Stage stage, Funcionario usuario){
        this.stage = stage;
        this.stage.setTitle("Menu");
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.hide();
        this.stage.show();

        ControllerMenuPrincipal.usuario = usuario;
        this.labelNomeUsuario.setText("Bem vindo, " + ControllerMenuPrincipal.usuario.getNome());
    }

    @FXML
    public void exibirMenuProduto() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLMenuProduto.fxml"));
        this.anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void exibirMenuMarca() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLMenuMarca.fxml"));
        this.anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void exibirMenuFuncionario() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLMenuFuncionario.fxml"));
        this.anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void exibirRelatorioEstoque() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLRelatorioEstoque.fxml"));
        this.anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void exibirRelatorioFluxoProduto() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLRelatorioFluxoProduto.fxml"));
        this.anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void exibirMenuUsuario() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLMenuUsuario.fxml"));
        this.anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void cadastrarProduto() throws IOException {
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1 || ControllerMenuPrincipal.usuario.getIdFuncao() == 2) {
            Produto produto = new Produto();
            String path = "/javafxmvc/view/FXMLFormularioProduto.fxml";
            String titulo = "Cadastrar Produto";

            boolean confirmacao = MainApplication.showArquivoXML(path, titulo, produto, "cadastrar");
            if (confirmacao) {
                this.produtoDAO.setConnection(this.connection);
                this.produtoDAO.inserir(produto);
            }
        }
    }

    @FXML
    public void cadastrarMarca() throws IOException {
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1 || ControllerMenuPrincipal.usuario.getIdFuncao() == 2) {
            Marca marca = new Marca();
            String path = "/javafxmvc/view/FXMLFormularioMarca.fxml";
            String titulo = "Cadastrar Marca";

            boolean confirmacao = MainApplication.showArquivoXML(path, titulo, marca, "cadastrar");
            if (confirmacao) {
                this.marcaDAO.setConnection(this.connection);
                this.marcaDAO.inserir(marca);
            }
        }
    }

    @FXML
    public void cadastrarFuncionario() throws IOException {
        if(ControllerMenuPrincipal.usuario.getIdFuncao() == 1) {
            Funcionario funcionario = new Funcionario();
            String path = "/javafxmvc/view/FXMLFormularioFuncionario.fxml";
            String titulo = "Cadastrar Funcionário";

            boolean confirmacao = MainApplication.showArquivoXML(path, titulo, funcionario, "cadastrar");
            if (confirmacao) {
                this.funcionarioDAO.setConnection(this.connection);
                this.funcionarioDAO.inserir(funcionario);
            }
        }
    }

}
