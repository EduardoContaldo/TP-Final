package javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafxmvc.model.dao.MarcaDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Produto;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
public class ControllerInsercaoProduto implements Initializable {

    @FXML
    private Label labelNomeProduto;
    @FXML
    private Label labelMarcaProduto;
    @FXML
    private Label labelQuantidadeProduto;
    @FXML
    private TextField textFieldQtdInserida;
    @FXML
    private Button buttonConfirmar;


    private Stage dialogStage;
    private boolean confirmar = false;
    private Produto produto;

    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();


    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        this.labelNomeProduto.setText(this.produto.getNomeProduto());
        this.labelMarcaProduto.setText(this.produto.getNomeMarca());
        this.labelQuantidadeProduto.setText(String.valueOf(this.produto.getQuantidade()));
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
    public void initialize(URL url, ResourceBundle rb) { }

    @FXML
    public void confirmarInsercao() {
        if (validarEntradaDeDados()) {
            int qtdTotal = Integer.parseInt(this.textFieldQtdInserida.getText()) + this.produto.getQuantidade();
            this.produto.setQuantidade(qtdTotal);
            this.produto.setNovaQuantidade(Integer.parseInt(this.textFieldQtdInserida.getText()));

            this.confirmar = true;
            this.dialogStage.close();
        }
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        try{
            Integer.parseInt(this.textFieldQtdInserida.getText());
        }
        catch(Exception e){
            errorMessage += "Quantidade inválida!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        }
        else {
            // Mostrando a mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}