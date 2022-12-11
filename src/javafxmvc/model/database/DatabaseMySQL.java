package javafxmvc.model.database;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseMySQL{

    private Connection connection;

    public Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/trabalho", "root","");
            return this.connection;
        } catch(ClassNotFoundException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Não foi possível realizar a conexão com o banco de dados!");
            alert.show();
        }
        return null;
    }

    public void desconectar(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ex) {

        }
    }
    
}
