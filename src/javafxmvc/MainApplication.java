package javafxmvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.controller.*;
import javafxmvc.model.domain.Funcionario;
import javafxmvc.model.domain.Marca;
import javafxmvc.model.domain.Produto;

import java.io.IOException;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        ControllerLogin loginController = new ControllerLogin();
        loginController.launchLoginController(stage);
    }

    public static boolean showArquivoXML(String path, String titulo, Object objeto, String funcao) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ControllerFormularioProduto.class.getResource(path));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle(titulo);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando a classe no Controller.
        if(objeto instanceof Produto && (funcao.equals("cadastrar") || funcao.equals("alterar"))){
            ControllerFormularioProduto controller = loader.getController();
            controller.setDialogStage(dialogStage);
            Produto produto = (Produto) objeto;
            controller.setProduto(produto);
            dialogStage.showAndWait();

            return controller.isConfirmar();
        }
        else if (objeto instanceof Produto && funcao.equals("inserir")) {
            ControllerInsercaoProduto controller = loader.getController();
            controller.setDialogStage(dialogStage);
            Produto produto = (Produto) objeto;
            controller.setProduto(produto);
            dialogStage.showAndWait();

            return controller.isConfirmar();
        }
        else if(objeto instanceof Marca){
            ControllerFormularioMarca controller = loader.getController();
            controller.setDialogStage(dialogStage);
            Marca marca = (Marca) objeto;
            controller.setMarca(marca);
            dialogStage.showAndWait();

            return controller.isConfirmar();
        }
        else if(objeto instanceof Funcionario && (funcao.equals("cadastrar") || funcao.equals("alterar"))) {
            ControllerFormularioFuncionario controller = loader.getController();
            controller.setDialogStage(dialogStage);
            Funcionario funcionario = (Funcionario) objeto;
            controller.setFuncionario(funcionario);
            dialogStage.showAndWait();

            return controller.isConfirmar();
        }
        else if(objeto instanceof Funcionario && funcao.equals("alterarUsuario")) {
            ControllerFormularioAlteracaoUsuario controller = loader.getController();
            controller.setDialogStage(dialogStage);
            Funcionario funcionario = (Funcionario) objeto;

            controller.setUsuario(funcionario);
            dialogStage.showAndWait();

            return controller.isConfirmar();
        }
        else if(objeto instanceof Funcionario && funcao.equals("alterarSenha")) {
            ControllerFormularioAlteracaoSenha controller = loader.getController();
            controller.setDialogStage(dialogStage);
            Funcionario funcionario = (Funcionario) objeto;

            controller.setUsuario(funcionario);
            dialogStage.showAndWait();

            return controller.isConfirmar();
        }
        else{
            return false;
        }
    }

}