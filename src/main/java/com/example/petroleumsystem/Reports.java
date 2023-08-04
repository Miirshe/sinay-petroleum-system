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
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Reports implements Initializable {
    ObservableList<FetchSupplier> list = FXCollections.observableArrayList();
    ObservableList<FetchCustomer> list1 = FXCollections.observableArrayList();

    @FXML
    private Button btnSalesReport;

    @FXML
    private Button btnReportsales;

    @FXML
    private TableView<FetchCustomer> TableViewInfoCustomer;

    @FXML
    private TableColumn<FetchCustomer, Integer> colCustomerid;

    @FXML
    private TableColumn<FetchCustomer, String> colCustomerrName;

    @FXML
    private TableColumn<FetchCustomer, String> colCustomerFuelType;

    @FXML
    private TableColumn<FetchCustomer, Integer> colCutomerLitters;

    @FXML
    private TableColumn<FetchCustomer, Double> colCustomerPerLitters;

    @FXML
    private TableColumn<FetchCustomer, Double> colCustomerTotalPrice;

    @FXML
    private TableView<FetchSupplier> TableViewInfoSupplier;

    @FXML
    private TableColumn<FetchSupplier, Integer> colSupplierId;

    @FXML
    private TableColumn<FetchSupplier, String> colSupplierName;

    @FXML
    private TableColumn<FetchSupplier, String> colSupplierFuelType;

    @FXML
    private TableColumn<FetchSupplier, Integer> colSupplierLitters;

    @FXML
    private TableColumn<FetchSupplier, Double> colSupplierPerLitters;

    @FXML
    private TableColumn<FetchSupplier, Double> colSupplierTotalPrice;

    @FXML
    private TableColumn<FetchSupplier, String> colSupplierStatus;

    @FXML
    private TextField txtSearchSales;

    @FXML
    private Button btnRefresh;

    @FXML
    private TextField txtSearchPurchase;

    @FXML
    void OnPurchaseReport(ActionEvent event) {
        JasperPrint jp;

        System.out.println();

        Map param = new HashMap();

        try {
            jp = JasperFillManager.fillReport("reports/Coffee_Landscape.jasper",param, ReportConnection.createConnection());

            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setTitle("Purchase Report");
            viewer.setVisible(true);
        }
        catch (JRException e){
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void OnRefresh(ActionEvent event) {

        try{
            FetchDataCustomer();
            FetchDataSupplier();
            txtSearchPurchase.clear();
            txtSearchSales.clear();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void OnSalesReport(ActionEvent event) {
        JasperPrint jp;

        System.out.println();

        Map param = new HashMap();

        try {
            jp = JasperFillManager.fillReport("reports/sales.jasper",param, ReportConnection.createConnection());

            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setTitle("Purchase Report");
            viewer.setVisible(true);
        }
        catch (JRException e){
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void OnSearchPurchasePress(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            try{
                TableViewInfoSupplier.getItems().clear();
                TableViewInfoSupplier.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                colSupplierId.setCellValueFactory(new PropertyValueFactory<FetchSupplier , Integer>("id"));
                colSupplierName.setCellValueFactory(new PropertyValueFactory<FetchSupplier , String>("name"));
                colSupplierFuelType.setCellValueFactory(new PropertyValueFactory<FetchSupplier , String>("fuel"));
                colSupplierLitters.setCellValueFactory(new PropertyValueFactory<FetchSupplier , Integer>("litters"));
                colSupplierPerLitters.setCellValueFactory(new PropertyValueFactory<FetchSupplier , Double>("price_per_litter"));
                colSupplierTotalPrice.setCellValueFactory(new PropertyValueFactory<FetchSupplier , Double>("total_price"));
                colSupplierStatus.setCellValueFactory(new PropertyValueFactory<FetchSupplier , String>("status"));
                int search = Integer.parseInt(txtSearchPurchase.getText());
                db con = new db("select * from purchase where id = '"+search+"' ");
                while (db.resultSet.next()){
                    list.addAll(new FetchSupplier(
                            db.resultSet.getInt("id"),
                            db.resultSet.getString("supplier_name"),
                            db.resultSet.getString("fuel_type"),
                            db.resultSet.getInt("litters"),
                            db.resultSet.getDouble("price_per_litters"),
                            db.resultSet.getDouble("total_price"),
                            db.resultSet.getString("status")
                    ));
                }
                TableViewInfoSupplier.setItems(list);

            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

    }

    @FXML
    void OnSearchSalesPress(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            try{
                TableViewInfoCustomer.getItems().clear();
                TableViewInfoCustomer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                colCustomerid.setCellValueFactory(new PropertyValueFactory<FetchCustomer , Integer>("id"));
                colCustomerrName.setCellValueFactory(new PropertyValueFactory<FetchCustomer , String>("name"));
                colCustomerFuelType.setCellValueFactory(new PropertyValueFactory<FetchCustomer , String>("fuel"));
                colCutomerLitters.setCellValueFactory(new PropertyValueFactory<FetchCustomer , Integer>("litters"));
                colCustomerPerLitters.setCellValueFactory(new PropertyValueFactory<FetchCustomer , Double>("price_per_litter"));
                colCustomerTotalPrice.setCellValueFactory(new PropertyValueFactory<FetchCustomer, Double>("total_price"));
                int search = Integer.parseInt(txtSearchSales.getText());
                db con = new db("SELECT * FROM sales where id = '"+search+"'");
                while (db.resultSet.next()){
                    list1.addAll(new FetchCustomer(
                            db.resultSet.getInt("id"),
                            db.resultSet.getString("customer_name"),
                            db.resultSet.getString("fuel_type"),
                            db.resultSet.getInt("litters"),
                            db.resultSet.getDouble("price_per_litters"),
                            db.resultSet.getDouble("total_price")
                    ));
                }
                TableViewInfoCustomer.setItems(list1);

            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

    }

    @FXML
    void getTableOfDataCustomer(MouseEvent event) {

    }

    @FXML
    void getTableOfDataSupplier(MouseEvent event) {

    }

   void  FetchDataSupplier (){

       try{
           TableViewInfoSupplier.getItems().clear();
           TableViewInfoSupplier.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
           colSupplierId.setCellValueFactory(new PropertyValueFactory<FetchSupplier , Integer>("id"));
           colSupplierName.setCellValueFactory(new PropertyValueFactory<FetchSupplier , String>("name"));
           colSupplierFuelType.setCellValueFactory(new PropertyValueFactory<FetchSupplier , String>("fuel"));
           colSupplierLitters.setCellValueFactory(new PropertyValueFactory<FetchSupplier , Integer>("litters"));
           colSupplierPerLitters.setCellValueFactory(new PropertyValueFactory<FetchSupplier , Double>("price_per_litter"));
           colSupplierTotalPrice.setCellValueFactory(new PropertyValueFactory<FetchSupplier , Double>("total_price"));
           colSupplierStatus.setCellValueFactory(new PropertyValueFactory<FetchSupplier , String>("status"));
           db con = new db("select * from purchase ");
           while (db.resultSet.next()){
               list.addAll(new FetchSupplier(
                       db.resultSet.getInt("id"),
                       db.resultSet.getString("supplier_name"),
                       db.resultSet.getString("fuel_type"),
                       db.resultSet.getInt("litters"),
                       db.resultSet.getDouble("price_per_litters"),
                       db.resultSet.getDouble("total_price"),
                       db.resultSet.getString("status")
               ));
           }
           TableViewInfoSupplier.setItems(list);

       }catch (Exception e){
           System.out.println(e.getMessage());
       }

    }
    void  FetchDataCustomer (){
        try{
            TableViewInfoCustomer.getItems().clear();
            TableViewInfoCustomer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            colCustomerid.setCellValueFactory(new PropertyValueFactory<FetchCustomer , Integer>("id"));
            colCustomerrName.setCellValueFactory(new PropertyValueFactory<FetchCustomer , String>("name"));
            colCustomerFuelType.setCellValueFactory(new PropertyValueFactory<FetchCustomer , String>("fuel"));
            colCutomerLitters.setCellValueFactory(new PropertyValueFactory<FetchCustomer , Integer>("litters"));
            colCustomerPerLitters.setCellValueFactory(new PropertyValueFactory<FetchCustomer , Double>("price_per_litter"));
            colCustomerTotalPrice.setCellValueFactory(new PropertyValueFactory<FetchCustomer, Double>("total_price"));
            db con = new db("SELECT * FROM sales");
            while (db.resultSet.next()){
                list1.addAll(new FetchCustomer(
                        db.resultSet.getInt("id"),
                        db.resultSet.getString("customer_name"),
                        db.resultSet.getString("fuel_type"),
                        db.resultSet.getInt("litters"),
                        db.resultSet.getDouble("price_per_litters"),
                        db.resultSet.getDouble("total_price")
                ));
            }
            TableViewInfoCustomer.setItems(list1);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            FetchDataSupplier();
            FetchDataCustomer();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
