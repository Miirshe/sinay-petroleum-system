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

public class Purchase implements Initializable {
    int currentTunnkCapacity = 0;
    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtTunkNumber;

    @FXML
    private ComboBox<String> comboSupplierName;

    @FXML
    private ComboBox<String> comboFuelType;
    ObservableList<String> cmbFeul = FXCollections.observableArrayList();
    ObservableList<String> cmbSupplier = FXCollections.observableArrayList();
    ObservableList<purchaseClass> list = FXCollections.observableArrayList();
    @FXML
    private TextField txtSupplierPhone;

    @FXML
    private TextField txtLitters;

    @FXML
    private TextField txtPerLitters;

    @FXML
    private TextField txtTotalPrice;

    @FXML
    private DatePicker txtDate;

    @FXML
    private RadioButton radioBtnPending;

    @FXML
    private ToggleGroup btnStatus;

    @FXML
    private RadioButton radioBtnApproved;

    @FXML
    private Button btnEdit;

    @FXML
    private TableView<purchaseClass> TableViewInfo;

    @FXML
    private TableColumn<purchaseClass, Integer> id;

    @FXML
    private TableColumn<purchaseClass, String> colSupplierName;

    @FXML
    private TableColumn<purchaseClass, Integer> colSupplierPhone;

    @FXML
    private TableColumn<purchaseClass, String> colFuelType;

    @FXML
    private TableColumn<purchaseClass, Integer> colTunkNumber;

    @FXML
    private TableColumn<purchaseClass, Integer> colLitters;

    @FXML
    private TableColumn<purchaseClass, Double> colPerLitters;

    @FXML
    private TableColumn<purchaseClass, Double> colTotalPrice;

    @FXML
    private TableColumn<purchaseClass, Date> colDate;

    @FXML
    private TableColumn<purchaseClass, String> colStatus;

    @FXML
    void FetchComboSupllierName() {
        try{
            db con = new db("select * from supplier");
            while (db.resultSet.next()){
                if(!cmbSupplier.contains(db.resultSet.getString("name"))){
                    cmbSupplier.addAll(db.resultSet.getString("name"));
                }
            }
            comboSupplierName.setItems(cmbSupplier);
            db cons = new db("select * from supplier where name = '"+comboSupplierName.getSelectionModel().getSelectedItem()+"' ");
            if(db.resultSet.next()){
                txtSupplierPhone.setText(String.valueOf(db.resultSet.getInt("phone")));
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
            PreparedStatement ps = db.connection.prepareStatement("delete from purchase where id = ? ");
            ps.setInt(1,id);
            ps.executeUpdate();
            FetchData();
            ClearDate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("DELETE");
            alert.setContentText("successfully Purchase Deleted ...");
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
            FetchComboSupllierName();
            FetchcomboFeulType();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void OnSave(ActionEvent event) {
        try{
            if(comboSupplierName.getItems().isEmpty() || txtSupplierPhone.getText().isEmpty() || comboFuelType.getItems().isEmpty()
            || txtTunkNumber.getText().isEmpty() || txtLitters.getText().isEmpty() || txtPerLitters.getText().isEmpty() ||
            txtTotalPrice.getText().isEmpty() || txtDate.equals("") || radioBtnApproved.getText().equals("") || radioBtnPending.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("fields can not empty ...");
                alert.show();
            }

            else {
                String status = null;
                String name = comboSupplierName.getSelectionModel().getSelectedItem();
                int phone = Integer.parseInt(txtSupplierPhone.getText());
                String fuel = comboFuelType.getSelectionModel().getSelectedItem();
                int tunk = Integer.parseInt(txtTunkNumber.getText());
                int litters = Integer.parseInt(txtLitters.getText());
                double price_per_litters = Double.parseDouble(txtPerLitters.getText());
                double total = Double.parseDouble(txtTotalPrice.getText());
                LocalDate date = txtDate.getValue();
                if(radioBtnPending.isSelected()){
                    status = radioBtnPending.getText();
                }
                if(radioBtnApproved.isSelected()){
                    status = radioBtnApproved.getText();
                }
                PreparedStatement ps = db.connection.prepareStatement("insert into purchase values(default , ? , ? , ? , ? , ? , ? , ? , ? , ?)");
                ps.setString(1,name);
                ps.setInt(2,phone);
                ps.setString(3,fuel);
                ps.setInt(4,tunk);
                ps.setInt(5, litters);
                ps.setDouble(6,price_per_litters);
                ps.setDouble(7,total);
                ps.setDate(8, java.sql.Date.valueOf(date));
                ps.setString(9,status);
                ps.executeUpdate();
                db  con = new db("select tunk_capacity from fuel where fuel_type = '"+fuel+"'");
                if(db.resultSet.next()){
                    int capacity = db.resultSet.getInt("tunk_capacity");
                    int liter = litters;
                    System.out.println("hi litters : "+liter);
                    currentTunnkCapacity = capacity;
                    System.out.println("hi current capacity : "+currentTunnkCapacity);
                    int result = currentTunnkCapacity+liter;
                    System.out.println("Hi result capacity : "+result);
                    PreparedStatement ps1 = db.connection.prepareStatement("update fuel set tunk_capacity = ? where fuel_type = ? ");
                    ps1.setInt(1,result);
                    ps1.setString(2,fuel);
                    ps1.executeUpdate();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SAVE");
                alert.setContentText("Successfully Purchase saved ...");
                alert.show();
                ClearDate();
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    @FXML
    void OnSearchPress(KeyEvent event) {
        try{
            TableViewInfo.getItems().clear();
            TableViewInfo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            id.setCellValueFactory(new PropertyValueFactory<purchaseClass , Integer>("id"));
            colSupplierName.setCellValueFactory(new PropertyValueFactory<purchaseClass , String>("supplier_name"));
            colSupplierPhone.setCellValueFactory(new PropertyValueFactory<purchaseClass , Integer>("supplier_phone"));
            colFuelType.setCellValueFactory(new PropertyValueFactory<purchaseClass , String>("fuel_type"));
            colTunkNumber.setCellValueFactory(new PropertyValueFactory<purchaseClass , Integer>("tunk_number"));
            colLitters.setCellValueFactory(new PropertyValueFactory<purchaseClass , Integer>("litters"));
            colPerLitters.setCellValueFactory(new PropertyValueFactory<purchaseClass , Double>("per_litters"));
            colTotalPrice.setCellValueFactory(new PropertyValueFactory<purchaseClass , Double>("total_price"));
            colDate.setCellValueFactory(new PropertyValueFactory<purchaseClass , Date>("date"));
            colStatus.setCellValueFactory(new PropertyValueFactory<purchaseClass , String>("status"));
            int search = Integer.parseInt(txtSearch.getText());
            db con = new db("select * from purchase where id = '"+search+"' ");
            while (db.resultSet.next()){
                list.addAll(new purchaseClass(
                        db.resultSet.getInt("id"),
                        db.resultSet.getString("supplier_name"),
                        db.resultSet.getInt("supplier_phone"),
                        db.resultSet.getString("fuel_type"),
                        db.resultSet.getInt("tunk_number"),
                        db.resultSet.getInt("litters"),
                        db.resultSet.getDouble("price_per_litters"),
                        db.resultSet.getDouble("total_price"),
                        db.resultSet.getDate("date"),
                        db.resultSet.getString("status")
                ));
            }
            TableViewInfo.setItems(list);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void getTableOfData(MouseEvent event) throws SQLException {
        purchaseClass purchaseClass = TableViewInfo.getSelectionModel().getSelectedItem();
        ObservableList<String> supplier = FXCollections.observableArrayList(purchaseClass.getSupplier_name());
        ObservableList<String> fuel = FXCollections.observableArrayList(purchaseClass.getFuel_type());
        comboSupplierName.setItems(supplier);
        txtSupplierPhone.setText(String.valueOf(purchaseClass.getSupplier_phone()));
        comboFuelType.setItems(fuel);
        txtTunkNumber.setText(String.valueOf(purchaseClass.getTunk_number()));
        txtLitters.setText(String.valueOf(purchaseClass.getLitters()));
        txtPerLitters.setText(String.valueOf(purchaseClass.getPer_litters()));
        txtTotalPrice.setText(String.valueOf(purchaseClass.getTotal_price()));
        txtDate.setValue(LocalDate.parse(String.valueOf(purchaseClass.getDate())));
        String status = purchaseClass.getStatus();
        if(status.equals("Pending")){
            radioBtnPending.setSelected(true);
            radioBtnApproved.setSelected(false);
        }
        else if(status.equals("Approved")){
            radioBtnPending.setSelected(false);
            radioBtnApproved.setSelected(true);
        }
        FetchComboSupllierName();
        FetchcomboFeulType();
    }
    @FXML
    void onEdit(ActionEvent event) {
        try{
            if(comboSupplierName.getItems().isEmpty() || txtSupplierPhone.getText().isEmpty() || comboFuelType.getItems().isEmpty()
                    || txtTunkNumber.getText().isEmpty() || txtLitters.getText().isEmpty() || txtPerLitters.getText().isEmpty() ||
                    txtTotalPrice.getText().isEmpty() || txtDate.equals("") || radioBtnApproved.getText().equals("") || radioBtnPending.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("fields can not empty ...");
                alert.show();
            }
            else {
                String status = null;
                String name = comboSupplierName.getSelectionModel().getSelectedItem();
                int phone = Integer.parseInt(txtSupplierPhone.getText());
                String fuel = comboFuelType.getSelectionModel().getSelectedItem();
                int tunk = Integer.parseInt(txtTunkNumber.getText());
                int litters = Integer.parseInt(txtLitters.getText());
                double price_per_litters = Double.parseDouble(txtPerLitters.getText());
                double total = Double.parseDouble(txtTotalPrice.getText());
                LocalDate date = txtDate.getValue();
                int myIndex = TableViewInfo.getSelectionModel().getSelectedIndex();
                int id = Integer.valueOf(String.valueOf(TableViewInfo.getItems().get(myIndex).getId()));
                if(radioBtnPending.isSelected()){
                    status = radioBtnPending.getText();
                } else if (radioBtnApproved.isSelected()) {
                    status = radioBtnApproved.getText();
                }
                PreparedStatement ps = db.connection.prepareStatement("update purchase set supplier_name = ? , supplier_phone = ? , fuel_type = ? , tunk_number = ? , litters = ? , price_per_litters = ? , total_price = ? , date = ? , status = ?  where id = ? ");
                ps.setString(1,name);
                ps.setInt(2,phone);
                ps.setString(3,fuel);
                ps.setInt(4,tunk);
                ps.setInt(5, litters);
                ps.setDouble(6,price_per_litters);
                ps.setDouble(7,total);
                ps.setDate(8, java.sql.Date.valueOf(date));
                ps.setString(9,status);
                ps.setInt(10,id);
                ps.executeUpdate();
                db  con = new db("select tunk_capacity from fuel where fuel_type = '"+fuel+"'");
                if(db.resultSet.next()){
                    int capacity = db.resultSet.getInt("tunk_capacity");
                    int liter = litters;
                    System.out.println("hi litters : "+liter);
                    currentTunnkCapacity = capacity;
                    System.out.println("hi current capacity : "+currentTunnkCapacity);
                    int result = currentTunnkCapacity+liter;
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
    void FetchData(){
        try{
            TableViewInfo.getItems().clear();
            TableViewInfo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            id.setCellValueFactory(new PropertyValueFactory<purchaseClass , Integer>("id"));
            colSupplierName.setCellValueFactory(new PropertyValueFactory<purchaseClass , String>("supplier_name"));
            colSupplierPhone.setCellValueFactory(new PropertyValueFactory<purchaseClass , Integer>("supplier_phone"));
            colFuelType.setCellValueFactory(new PropertyValueFactory<purchaseClass , String>("fuel_type"));
            colTunkNumber.setCellValueFactory(new PropertyValueFactory<purchaseClass , Integer>("tunk_number"));
            colLitters.setCellValueFactory(new PropertyValueFactory<purchaseClass , Integer>("litters"));
            colPerLitters.setCellValueFactory(new PropertyValueFactory<purchaseClass , Double>("per_litters"));
            colTotalPrice.setCellValueFactory(new PropertyValueFactory<purchaseClass , Double>("total_price"));
            colDate.setCellValueFactory(new PropertyValueFactory<purchaseClass , Date>("date"));
            colStatus.setCellValueFactory(new PropertyValueFactory<purchaseClass , String>("status"));
            db con = new db("select * from purchase ");
            while (db.resultSet.next()){
                list.addAll(new purchaseClass(
                        db.resultSet.getInt("id"),
                        db.resultSet.getString("supplier_name"),
                        db.resultSet.getInt("supplier_phone"),
                        db.resultSet.getString("fuel_type"),
                        db.resultSet.getInt("tunk_number"),
                        db.resultSet.getInt("litters"),
                        db.resultSet.getDouble("price_per_litters"),
                        db.resultSet.getDouble("total_price"),
                        db.resultSet.getDate("date"),
                        db.resultSet.getString("status")
                        ));
            }
            TableViewInfo.setItems(list);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FetchComboSupllierName();
            FetchcomboFeulType();
            FetchData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    @FXML
//    void onLitters() {
//    }


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
    void ClearDate(){
        txtDate.setValue(null);
        txtSupplierPhone.clear();
        txtTotalPrice.clear();
        txtTunkNumber.clear();
        txtLitters.clear();
        txtPerLitters.clear();
        comboFuelType.getItems().clear();
        comboSupplierName.getItems().clear();
        radioBtnApproved.setSelected(false);
        radioBtnPending.setSelected(false);
        txtSearch.clear();
    }
}
