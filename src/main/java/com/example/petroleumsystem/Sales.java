package com.example.petroleumsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class Sales implements Initializable {

    int currentTunnkCapacity = 0;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtTunkNumber;

    @FXML
    private TextField txtCustomerPhone;

    @FXML
    private TextField txtLitters;

    @FXML
    private TextField txtPerLitters;

    @FXML
    private TextField txtTotalPrice;

    @FXML
    private DatePicker txtDate;

    @FXML
    private Button btnEdit;

    @FXML
    private ComboBox<String> comboCustomerName;

    @FXML
    private ComboBox<String> comboFuelType;

    @FXML
    private TableView<SalesClass> TableViewInfo;

    @FXML
    private TableColumn<SalesClass, Integer> id;

    @FXML
    private TableColumn<SalesClass, String> colCustomerrName;

    @FXML
    private TableColumn<SalesClass, Integer> colCustomerPhone;

    @FXML
    private TableColumn<SalesClass, String> colFuelType;

    @FXML
    private TableColumn<SalesClass, Integer> colTunkNumber;

    @FXML
    private TableColumn<SalesClass,Integer> colLitters;

    @FXML
    private TableColumn<SalesClass, Double> colPerLitters;

    @FXML
    private TableColumn<SalesClass, Double> colTotalPrice;

    @FXML
    private TableColumn<SalesClass, Date> colDate;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnRefresh;

    ObservableList<String> cmbFeul = FXCollections.observableArrayList();
    ObservableList<String> cmbCustomer = FXCollections.observableArrayList();
    ObservableList<SalesClass> list = FXCollections.observableArrayList();

    @FXML
    void FetchComboCustomerName() {
        try{
            db con = new db("SELECT * FROM customer");
            while (db.resultSet.next()){
                if(!cmbCustomer.contains(db.resultSet.getString("name"))){
                    cmbCustomer.addAll(db.resultSet.getString("name"));
                }
            }
            comboCustomerName.setItems(cmbCustomer);
            db cons = new db("SELECT * FROM customer where name = '"+comboCustomerName.getSelectionModel().getSelectedItem()+"' ");
            if(db.resultSet.next()){
                txtCustomerPhone.setText(String.valueOf(db.resultSet.getInt("phone")));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void FetchcomboFeulType() throws SQLException {

        db con = new db("select * from fuel ");
        while (db.resultSet.next()){
            if(!cmbFeul.contains(db.resultSet.getString("fuel_type"))){
                cmbFeul.addAll(db.resultSet.getString("fuel_type"));
            }
        }
        comboFuelType.setItems(cmbFeul);
        db cons = new db("select * from fuel where fuel_type = '"+comboFuelType.getSelectionModel().getSelectedItem()+"' ");
        if(db.resultSet.next()){
            txtTunkNumber.setText(String.valueOf(db.resultSet.getInt("tunk_number")));
            txtPerLitters.setText(String.valueOf(db.resultSet.getDouble("price_per_litters")));
        }

    }

    @FXML
    void OnDelete(ActionEvent event) {
        try{
            int myIndex = TableViewInfo.getSelectionModel().getSelectedIndex();
            int id = Integer.valueOf(String.valueOf(TableViewInfo.getItems().get(myIndex).getId()));
            PreparedStatement ps = db.connection.prepareStatement("delete from sales where id = ? ");
            ps.setInt(1,id);
            ps.executeUpdate();
            FetchData();
            ClearDate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("DELETE");
            alert.setContentText("successfully Sale Deleted ...");
            alert.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    @FXML
    void OnRefresh(ActionEvent event) {

        try{
            FetchData();
            ClearDate();
            FetchComboCustomerName();
            FetchcomboFeulType();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void OnSave(ActionEvent event) {

        try{
            if(comboCustomerName.getItems().isEmpty() || txtCustomerPhone.getText().isEmpty() || comboFuelType.getItems().isEmpty()
                    || txtTunkNumber.getText().isEmpty() || txtLitters.getText().isEmpty() || txtPerLitters.getText().isEmpty() ||
                    txtTotalPrice.getText().isEmpty() || txtDate.equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("fields can not empty ...");
                alert.show();
            }

            else {
                String status = null;
                String name = comboCustomerName.getSelectionModel().getSelectedItem();
                int phone = Integer.parseInt(txtCustomerPhone.getText());
                String fuel = comboFuelType.getSelectionModel().getSelectedItem();
                int tunk = Integer.parseInt(txtTunkNumber.getText());
                int litters = Integer.parseInt(txtLitters.getText());
                double price_per_litters = Double.parseDouble(txtPerLitters.getText());
                double total = Double.parseDouble(txtTotalPrice.getText());
                LocalDate date = txtDate.getValue();
                PreparedStatement ps = db.connection.prepareStatement("insert into sales values(default , ? , ? , ? , ? , ? , ? , ? , ?)");
                ps.setString(1,name);
                ps.setInt(2,phone);
                ps.setString(3,fuel);
                ps.setInt(4,tunk);
                ps.setInt(5, litters);
                ps.setDouble(6,price_per_litters);
                ps.setDouble(7,total);
                ps.setDate(8, java.sql.Date.valueOf(date));
                ps.executeUpdate();
                db  con = new db("select tunk_capacity from fuel where fuel_type = '"+fuel+"'");
                if(db.resultSet.next()){
                    int capacity = db.resultSet.getInt("tunk_capacity");
                    int liter = litters;
                    System.out.println("hi litters : "+liter);
                    currentTunnkCapacity = capacity;
                    System.out.println("hi current capacity : "+currentTunnkCapacity);
                    int result = currentTunnkCapacity-liter;
                    System.out.println("Hi result capacity : "+result);
                    PreparedStatement ps1 = db.connection.prepareStatement("update fuel set tunk_capacity = ? where fuel_type = ? ");
                    ps1.setInt(1,result);
                    ps1.setString(2,fuel);
                    ps1.executeUpdate();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SAVE");
                alert.setContentText("Successfully Sale saved ...");
                alert.show();
                ClearDate();
                FetchData();
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void OnSearchPress(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            try{
                TableViewInfo.getItems().clear();
                TableViewInfo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                id.setCellValueFactory(new PropertyValueFactory<SalesClass , Integer>("id"));
                colCustomerrName.setCellValueFactory(new PropertyValueFactory<SalesClass , String>("customer_name"));
                colCustomerPhone.setCellValueFactory(new PropertyValueFactory<SalesClass, Integer>("customer_phone"));
                colFuelType.setCellValueFactory(new PropertyValueFactory<SalesClass , String>("fuel_type"));
                colTunkNumber.setCellValueFactory(new PropertyValueFactory<SalesClass , Integer>("tunk_number"));
                colLitters.setCellValueFactory(new PropertyValueFactory<SalesClass , Integer>("litters"));
                colPerLitters.setCellValueFactory(new PropertyValueFactory<SalesClass , Double>("per_litters"));
                colTotalPrice.setCellValueFactory(new PropertyValueFactory<SalesClass, Double>("total_price"));
                colDate.setCellValueFactory(new PropertyValueFactory<SalesClass , Date>("date"));
                int search = Integer.parseInt(txtSearch.getText());
                db con = new db("select * from sales where id = '"+search+"'");
                while (db.resultSet.next()){
                    list.addAll(new SalesClass(
                            db.resultSet.getInt("id"),
                            db.resultSet.getString("customer_name"),
                            db.resultSet.getInt("customer_phone"),
                            db.resultSet.getString("fuel_type"),
                            db.resultSet.getInt("tunk_number"),
                            db.resultSet.getInt("litters"),
                            db.resultSet.getDouble("price_per_litters"),
                            db.resultSet.getDouble("total_price"),
                            db.resultSet.getDate("date")
                    ));
                }
                TableViewInfo.setItems(list);

            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

    }

    @FXML
    void getTableOfData(MouseEvent event) {

        SalesClass salesClass = TableViewInfo.getSelectionModel().getSelectedItem();
        ObservableList<String> supplier = FXCollections.observableArrayList(salesClass.getCustomer_name());
        ObservableList<String> fuel = FXCollections.observableArrayList(salesClass.getFuel_type());
        comboCustomerName.setItems(supplier);
        txtCustomerPhone.setText(String.valueOf(salesClass.getCustomer_phone()));
        comboFuelType.setItems(fuel);
        txtTunkNumber.setText(String.valueOf(salesClass.getTunk_number()));
        txtLitters.setText(String.valueOf(salesClass.getLitters()));
        txtPerLitters.setText(String.valueOf(salesClass.getPer_litters()));
        txtTotalPrice.setText(String.valueOf(salesClass.getTotal_price()));
        txtDate.setValue(LocalDate.parse(String.valueOf(salesClass.getDate())));
    }

    @FXML
    void onEdit(ActionEvent event) {

        try{
            if(comboCustomerName.getItems().isEmpty() || txtCustomerPhone.getText().isEmpty() || comboFuelType.getItems().isEmpty()
                    || txtTunkNumber.getText().isEmpty() || txtLitters.getText().isEmpty() || txtPerLitters.getText().isEmpty() ||
                    txtTotalPrice.getText().isEmpty() || txtDate.equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("fields can not empty ...");
                alert.show();
            }

            else {
                String status = null;
                String name = comboCustomerName.getSelectionModel().getSelectedItem();
                int phone = Integer.parseInt(txtCustomerPhone.getText());
                String fuel = comboFuelType.getSelectionModel().getSelectedItem();
                int tunk = Integer.parseInt(txtTunkNumber.getText());
                int litters = Integer.parseInt(txtLitters.getText());
                double price_per_litters = Double.parseDouble(txtPerLitters.getText());
                double total = Double.parseDouble(txtTotalPrice.getText());
                LocalDate date = txtDate.getValue();
                int myIndex = TableViewInfo.getSelectionModel().getSelectedIndex();
                int id = Integer.valueOf(String.valueOf(TableViewInfo.getItems().get(myIndex).getId()));
                PreparedStatement ps = db.connection.prepareStatement("update sales set customer_name = ? , customer_phone = ? , fuel_type = ? , tunk_number = ? , litters = ? , price_per_litters = ? , total_price = ? , date = ?  where id = ? ");
                ps.setString(1,name);
                ps.setInt(2,phone);
                ps.setString(3,fuel);
                ps.setInt(4,tunk);
                ps.setInt(5, litters);
                ps.setDouble(6,price_per_litters);
                ps.setDouble(7,total);
                ps.setDate(8, java.sql.Date.valueOf(date));
                ps.setInt(9,id);
                ps.executeUpdate();
                db  con = new db("select tunk_capacity from fuel where fuel_type = '"+fuel+"'");
                if(db.resultSet.next()){
                    int capacity = db.resultSet.getInt("tunk_capacity");
                    int liter = litters;
                    System.out.println("hi litters : "+liter);
                    currentTunnkCapacity = capacity;
                    System.out.println("hi current capacity : "+currentTunnkCapacity);
                    int result = currentTunnkCapacity-liter;
                    System.out.println("Hi result capacity : "+result);
                    PreparedStatement ps1 = db.connection.prepareStatement("update fuel set tunk_capacity = ? where fuel_type = ? ");
                    ps1.setInt(1,result);
                    ps1.setString(2,fuel);
                    ps1.executeUpdate();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Updated");
                alert.setContentText("Successfully Purchase Updated ...");
                alert.show();
                ClearDate();
                FetchData();
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    @FXML
    void onLitters(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            double price = Double.parseDouble(txtPerLitters.getText());
            if(!txtLitters.getText().isEmpty()){
                double litters = Double.parseDouble(txtLitters.getText());
                double result = price*litters;
                txtTotalPrice.setText(String.valueOf(result));
            }
        }

    }
    void FetchData(){
        try{
            TableViewInfo.getItems().clear();
            TableViewInfo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            id.setCellValueFactory(new PropertyValueFactory<SalesClass , Integer>("id"));
            colCustomerrName.setCellValueFactory(new PropertyValueFactory<SalesClass , String>("customer_name"));
            colCustomerPhone.setCellValueFactory(new PropertyValueFactory<SalesClass, Integer>("customer_phone"));
            colFuelType.setCellValueFactory(new PropertyValueFactory<SalesClass , String>("fuel_type"));
            colTunkNumber.setCellValueFactory(new PropertyValueFactory<SalesClass , Integer>("tunk_number"));
            colLitters.setCellValueFactory(new PropertyValueFactory<SalesClass , Integer>("litters"));
            colPerLitters.setCellValueFactory(new PropertyValueFactory<SalesClass , Double>("per_litters"));
            colTotalPrice.setCellValueFactory(new PropertyValueFactory<SalesClass, Double>("total_price"));
            colDate.setCellValueFactory(new PropertyValueFactory<SalesClass , Date>("date"));
            db con = new db("SELECT * FROM sales");
            while (db.resultSet.next()){
                list.addAll(new SalesClass(
                        db.resultSet.getInt("id"),
                        db.resultSet.getString("customer_name"),
                        db.resultSet.getInt("customer_phone"),
                        db.resultSet.getString("fuel_type"),
                        db.resultSet.getInt("tunk_number"),
                        db.resultSet.getInt("litters"),
                        db.resultSet.getDouble("price_per_litters"),
                        db.resultSet.getDouble("total_price"),
                        db.resultSet.getDate("date")
                ));
            }
            TableViewInfo.setItems(list);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    void ClearDate(){
        txtDate.setValue(null);
        txtCustomerPhone.clear();
        txtTotalPrice.clear();
        txtTunkNumber.clear();
        txtLitters.clear();
        txtPerLitters.clear();
        comboFuelType.getItems().clear();
        comboCustomerName.getItems().clear();
        txtSearch.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FetchData();
            FetchcomboFeulType();
            FetchComboCustomerName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


