package com.example.petroleumsystem;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class dashboard implements Initializable{

    @FXML
    private Button btnHome;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnSupplier;

    @FXML
    private Button btnFuel;

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnSales;

    @FXML
    private Button btnPurchase;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnLogOut;

    public static String roll_type;
    public static String user_name;

    private volatile boolean stop = false;

    @FXML
    public Label lblPrintDate;

    @FXML
    private Label lblRollType;

    @FXML
    private Label lblUserName;


    @FXML
    private BorderPane fxborderPane;

    @FXML
    void OnClose(ActionEvent event) {
        System.exit(1);
        stop = true;
    }

    @FXML
    void OnCustomer(ActionEvent event) {

        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("Customer");
        fxborderPane.setCenter(view);
        set_active_buttons(btnCustomer);

    }

    @FXML
    void OnEmployee(ActionEvent event) {

        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("Employee");
        fxborderPane.setCenter(view);
        set_active_buttons(btnEmployee);

    }

    @FXML
    void OnFuel(ActionEvent event) {
        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("Fuel");
        fxborderPane.setCenter(view);
        set_active_buttons(btnFuel);
    }

    @FXML
    void OnReport(ActionEvent event) {
        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("reports");
        fxborderPane.setCenter(view);
        set_active_buttons(btnReports);
    }

    @FXML
    void OnSale(ActionEvent event) {
        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("Sales");
        fxborderPane.setCenter(view);
        set_active_buttons(btnSales);
    }

    @FXML
    void OnSupplier(ActionEvent event) {

        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("Supplier");
        fxborderPane.setCenter(view);
        set_active_buttons(btnSupplier);

    }

    @FXML
    void OnUser(ActionEvent event) {
        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("Users");
        fxborderPane.setCenter(view);
        set_active_buttons(btnUsers);
    }

    @FXML
    void onHome() {
        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("Home");
        fxborderPane.setCenter(view);
        set_active_buttons(btnHome);
    }
    @FXML
    void onPurchase(ActionEvent event) {
        System.out.println("you Clicked me! ");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPane("Purchase");
        fxborderPane.setCenter(view);
        set_active_buttons(btnPurchase);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onHome();
        lblUserName.setText(user_name);
        lblRollType.setText(roll_type);
        TimeNow();
    }
    private void TimeNow(){
        Thread thread = new Thread(()->{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
            while (!stop){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                final  String timeNow = simpleDateFormat.format(new Date());
                Platform.runLater(()->{
                    lblPrintDate.setText(timeNow);
                });
            }
        });
        thread.start();
    }

    @FXML
    void OnLogOut(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Login Page ");
        stage.resizableProperty().setValue(false);
        stage.setScene(scene);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    void set_active_buttons (Button active_button){

        btnCustomer.getStyleClass().remove("active");
        btnEmployee.getStyleClass().remove("active");
        btnFuel.getStyleClass().remove("active");
        btnHome.getStyleClass().remove("active");
        btnPurchase.getStyleClass().remove("active");
        btnLogOut.getStyleClass().remove("active");
        btnSales.getStyleClass().remove("active");
        btnReports.getStyleClass().remove("active");
        btnSupplier.getStyleClass().remove("active");
        btnUsers.getStyleClass().remove("active");
        active_button.getStyleClass().add("active");
    }
}
