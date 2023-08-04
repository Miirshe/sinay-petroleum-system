package com.example.petroleumsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Fuel implements Initializable {

    ObservableList<FuelClass> list = FXCollections.observableArrayList();
    @FXML
    private TextField txtSearch;


    @FXML
    private TableColumn<FuelClass, Double> colPricePerLitters;

    @FXML
    private TextField txtPricePerLitters;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtTunkCapacity;
    @FXML
    private TextField cmbFuelType;
    @FXML
    private TextField cmbTunkNumber;
    @FXML
    private TableView<FuelClass> TableViewInfo;

    @FXML
    private TableColumn<FuelClass, Integer> id;

    @FXML
    private TableColumn<FuelClass, Integer> tunkNumber;

    @FXML
    private TableColumn<FuelClass, String> fuelType;

    @FXML
    private TableColumn<FuelClass, Integer> tunkCapacity;
    @FXML
    void OnDelete(ActionEvent event) {
        try{
            int myIndex = TableViewInfo.getSelectionModel().getSelectedIndex();
            int id = Integer.valueOf(String.valueOf(TableViewInfo.getItems().get(myIndex).getId()));
            PreparedStatement ps = db.connection.prepareStatement("delete from fuel where id = ? ");
            ps.setInt(1,id);
            ps.executeUpdate();
            FetchData();
            ClearData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("DELETE");
            alert.setContentText("successfully Fuel Deleted ...");
            alert.show();
            ClearData();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    @FXML
    void OnEdit(ActionEvent event) {
        try{
            if(cmbTunkNumber.getText().isEmpty() || cmbFuelType.getText().isEmpty() || txtTunkCapacity.getText().isEmpty() || txtPricePerLitters.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("fields can not empty ...");
                alert.show();
            }
            else {
                int tunk_number = Integer.parseInt(cmbTunkNumber.getText());
                String fuel = cmbFuelType.getText();
                int tunk_capacity = Integer.parseInt(txtTunkCapacity.getText());
                double price_litter = Double.parseDouble(txtPricePerLitters.getText());
                int myIndex = TableViewInfo.getSelectionModel().getSelectedIndex();
                int id = Integer.valueOf(String.valueOf(TableViewInfo.getItems().get(myIndex).getId()));
                PreparedStatement ps = db.connection.prepareStatement("update fuel set tunk_number= ? , tunk_capacity = ? , fuel_type = ? , price_per_litters = ?  where id = ? ");
                ps.setInt(1,tunk_number);
                ps.setInt(2,tunk_capacity);
                ps.setString(3,fuel);
                ps.setDouble(4,price_litter);
                ps.setInt(5,id);
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Updated");
                alert.setContentText("Successfully Fuel Updated ...");
                alert.show();
                FetchData();
                ClearData();
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void OnRefresh(ActionEvent event) throws SQLException, IOException {
      try{
          FetchData();
          ClearData();
      }catch (Exception e){
          System.out.println(e.getMessage());
      }

    }

    @FXML
    void OnSave(ActionEvent event) {
        try{
            if(cmbTunkNumber.getText().isEmpty()|| cmbFuelType.getText().isEmpty() || txtTunkCapacity.getText().isEmpty()|| txtPricePerLitters.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("fields can not empty ...");
                alert.show();
            }
            else {
                int tunk_number = Integer.parseInt(cmbTunkNumber.getText());
                String fuel = cmbFuelType.getText();
                int tunk_capacity = Integer.parseInt(txtTunkCapacity.getText());
                double price_per_litters = Double.parseDouble(txtPricePerLitters.getText());
                PreparedStatement ps = db.connection.prepareStatement("insert into fuel values(default , ? , ? , ? , ?)");
                ps.setInt(1,tunk_number);
                ps.setInt(2,tunk_capacity);
                ps.setString(3,fuel);
                ps.setDouble(4,price_per_litters);
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SAVE");
                alert.setContentText("Successfully Fuel saved ...");
                alert.show();
                FetchData();
                ClearData();
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void OnSearchPress(KeyEvent event) {
        try{
           if(event.getCode().equals(KeyCode.ENTER)){
               TableViewInfo.getItems().clear();
               TableViewInfo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
               id.setCellValueFactory(new PropertyValueFactory<FuelClass , Integer>("id"));
               tunkNumber.setCellValueFactory(new PropertyValueFactory<FuelClass , Integer>("tunk_number"));
               tunkCapacity.setCellValueFactory(new PropertyValueFactory<FuelClass , Integer>("tunk_capacity"));
               fuelType.setCellValueFactory(new PropertyValueFactory<FuelClass , String>("fuel_type"));
               colPricePerLitters.setCellValueFactory(new PropertyValueFactory<FuelClass , Double>("price_per_litters"));
               int search =Integer.parseInt(txtSearch.getText());
               db con = new db("select * from fuel where id = '"+search+"'");
               while (db.resultSet.next()){
                   list.addAll(new FuelClass(
                           db.resultSet.getInt("id"),
                           db.resultSet.getInt("tunk_number"),
                           db.resultSet.getInt("tunk_capacity"),
                           db.resultSet.getString("fuel_type"),
                           db.resultSet.getDouble("price_per_litters")
                   ));
               }
               TableViewInfo.setItems(list);
           }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void getTableOfData(MouseEvent event) {
        try{
            FuelClass fuelClass = TableViewInfo.getSelectionModel().getSelectedItem();
            cmbTunkNumber.setText(String.valueOf(fuelClass.getTunk_number()));
            txtTunkCapacity.setText(String.valueOf(fuelClass.getTunk_capacity()));
            cmbFuelType.setText(String.valueOf(fuelClass.getFuel_type()));
            txtPricePerLitters.setText(String.valueOf(fuelClass.getPrice_per_litters()));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            FetchData();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    void FetchData() throws SQLException , IOException {
        try{
            TableViewInfo.getItems().clear();
            TableViewInfo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            id.setCellValueFactory(new PropertyValueFactory<FuelClass , Integer>("id"));
            tunkNumber.setCellValueFactory(new PropertyValueFactory<FuelClass , Integer>("tunk_number"));
            tunkCapacity.setCellValueFactory(new PropertyValueFactory<FuelClass , Integer>("tunk_capacity"));
            fuelType.setCellValueFactory(new PropertyValueFactory<FuelClass , String>("fuel_type"));
            colPricePerLitters.setCellValueFactory(new PropertyValueFactory<FuelClass , Double>("price_per_litters"));
            db con = new db("select * from fuel");
            while (db.resultSet.next()){
                list.addAll(new FuelClass(
                   db.resultSet.getInt("id"),
                   db.resultSet.getInt("tunk_number"),
                   db.resultSet.getInt("tunk_capacity"),
                   db.resultSet.getString("fuel_type"),
                   db.resultSet.getDouble("price_per_litters")
                ));
            }
            TableViewInfo.setItems(list);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void ClearData(){
        txtSearch.clear();
        cmbTunkNumber.clear();
        cmbFuelType.clear();
        txtTunkCapacity.clear();
        txtPricePerLitters.clear();
    }

    @FXML
    void OnAddFuelType(ActionEvent event) throws IOException {
    }

    @FXML
    void OnAddTunkNumber(ActionEvent event) throws IOException {
    }




}


