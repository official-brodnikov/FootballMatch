import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.*;

public class MatchApp extends Application {

    public TextField searchTextField;
    public Button searchButton;
    public TextArea textArea;



    public static void main(String[] args) {
        launch(args);
    }

    private ObservableList<DataTable> usersData = FXCollections.observableArrayList();

    @FXML
    private TableView<DataTable> tableView;

    @FXML
    private TableColumn<DataTable, String> idColumn;

    @FXML
    private TableColumn<DataTable, String> homeColumn;

    @FXML
    private TableColumn<DataTable, String> awayColumn;

    @FXML
    private TableColumn<DataTable, String> resColumn;

    @FXML
    private TableColumn<DataTable, String> dateColumn;
    // инициализируем форму данными

    private String Url = "jdbc:mysql://localhost:3306/footballmatch"+
            "?verifyServerCertificate=false"+
            "&useSSL=false"+
            "&requireSSL=false"+
            "&useLegacyDatetimeCode=false"+
            "&amp"+
            "&serverTimezone=UTC";
    private String Password = "root";
    private String User = "root";

    private Connection connection;
    private Statement stmt;

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("MatchApp.fxml"));
        primaryStage.setTitle("Football Matches App");
        primaryStage.getIcons().add(new Image(MatchApp.class.getResourceAsStream("football.gif")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }




    public void onMouseClicked(MouseEvent mouseEvent) {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //задаем какие будут значения у столбцов тип и тд
        idColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("id"));
        homeColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("home"));
        awayColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("away"));
        resColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("res"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("date"));
        //list из значений
        ObservableList<DataTable> listOfMatches = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection(Url, User, Password);
            stmt=connection.createStatement();//тип запроса статический
            ResultSet executeQueryMatches = stmt.executeQuery("SELECT * FROM footballmatch.matches");
            tableView.getItems().clear();
            while (executeQueryMatches.next()) {
                DataTable matches = new DataTable();
                matches.setId(executeQueryMatches.getString(1));
                matches.setHome(executeQueryMatches.getString(2));
                matches.setAway(executeQueryMatches.getString(3));
                matches.setRes(executeQueryMatches.getString(5));
                matches.setDate(executeQueryMatches.getString(7));
                listOfMatches.add(matches);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //final чтобы нельзя было унаследовать нигде больше не исполлзовали
        final TableView.TableViewSelectionModel<DataTable> selectionModel = tableView.getSelectionModel();
        tableView.setItems(listOfMatches);

        final Statement finalStmt = stmt;
        //когда выделяем столбец запускаем обработчик
        selectionModel.selectedItemProperty().addListener(new ChangeListener<DataTable>() {
            @Override
            public void changed(ObservableValue<? extends DataTable> observable, DataTable oldValue, DataTable newValue) {
                try {
                    //sql библиотека resultset возвращает ответ от запроса
                    ResultSet executeQueryMatches = finalStmt.executeQuery("SELECT * FROM footballmatch.matches WHERE footballmatch.matches.idFixture="+selectionModel.getSelectedItem().getId());

                    while (executeQueryMatches.next()) {
                        String temp;
                        if (executeQueryMatches.getString(4) == null)
                            temp = "";
                        else temp = executeQueryMatches.getString(4);
                        String penalty;
                        if (executeQueryMatches.getString(8) == null)
                            penalty = "Нет";
                        else penalty = "Да";
                        textArea.setText("Счет после первого тайма: " + temp + "\nБыл ли пенальти в матче:" + penalty + "\nНазвание соревнования: " + executeQueryMatches.getString(6) + "\nСтатус матча: " + executeQueryMatches.getString(10));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        //заполняем
        tableView.setItems(listOfMatches);
    }
}
