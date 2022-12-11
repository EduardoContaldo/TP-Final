package javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.dao.MarcaDAO;
import javafxmvc.model.database.DatabaseMySQL;
import javafxmvc.model.domain.Marca;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ControllerFormularioMarca implements Initializable {

    @FXML
    private TextField textFieldNomeMarca;
    @FXML
    private Button buttonCadastrar;

    private Stage dialogStage;
    private boolean confirmar = false;
    private Marca marca;

    private final DatabaseMySQL database = new DatabaseMySQL();
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
        this.textFieldNomeMarca.setText(this.marca.getNomeMarca());
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

    }

    public void confirmarCadastro() {
        if (validarEntradaDeDados()) {
            this.marca.setNomeMarca(this.textFieldNomeMarca.getText());

            this.confirmar = true;
            this.dialogStage.close();
        }
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        this.marca.setNomeMarca(this.textFieldNomeMarca.getText());
        this.marcaDAO.setConnection(this.connection);
        if(this.marcaDAO.verificarMarca(this.marca)){
            errorMessage += "Marca já existe!\n";
        }
        if (this.textFieldNomeMarca.getText() == null || this.textFieldNomeMarca.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
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