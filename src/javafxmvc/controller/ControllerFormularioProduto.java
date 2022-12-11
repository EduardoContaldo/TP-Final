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
import javafxmvc.model.dao.MarcaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Funcionario;
import javafxmvc.model.domain.Marca;
import javafxmvc.model.domain.Produto;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFormularioProduto implements Initializable {

    @FXML
    private TextField textFieldNomeProduto;
    @FXML
    private TextField textFieldPrecoProduto;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private ComboBox<Marca> comboBoxMarcaProduto;

    private List<Marca> listMarcas = new ArrayList<>();
    private ObservableList<Marca> observableListMarcas;

    private Stage dialogStage;
    private boolean confirmar = false;
    private Produto produto;
    private Marca marcaProduto;

    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final MarcaDAO marcaDAO = new MarcaDAO();


    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        this.textFieldNomeProduto.setText(this.produto.getNomeProduto());
        this.textFieldPrecoProduto.setText(String.valueOf(this.produto.getValor()));
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
        carregarComboBoxMarca();
    }

    public void carregarComboBoxMarca() {
        this.marcaDAO.setConnection(this.connection);
        this.listMarcas = this.marcaDAO.listar();
        this.observableListMarcas = FXCollections.observableArrayList(this.listMarcas);
        this.comboBoxMarcaProduto.setItems(this.observableListMarcas);
    }

    @FXML
    public void selecionarItemComboBoxMarcas() {
        this.marcaProduto = this.comboBoxMarcaProduto.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void confirmarAlteracao(){
        if (validarEntradaDeDados()) {
            this.produto.setNomeProduto(this.textFieldNomeProduto.getText());
            this.produto.setValor(Double.parseDouble(this.textFieldPrecoProduto.getText()));
            this.produto.setIdMarca(this.marcaProduto.getIdMarca());

            this.confirmar = true;
            this.dialogStage.close();
        }
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        this.produto.setNomeProduto(this.textFieldNomeProduto.getText());
        this.produto.setIdMarca(this.marcaProduto.getIdMarca());
        this.produtoDAO.setConnection(this.connection);
        if(this.produtoDAO.verificarProduto(this.produto)){
            errorMessage += "Produto já existe!\n";
        }
        if (this.textFieldNomeProduto.getText() == null || this.textFieldNomeProduto.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (this.marcaProduto == null) {
            errorMessage += "Marca inválida!\n";
        }
        try{
            Double.parseDouble(this.textFieldPrecoProduto.getText());
        }
        catch(Exception e){
            errorMessage += "Preço inválido!\n";
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