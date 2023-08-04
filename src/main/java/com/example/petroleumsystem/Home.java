package com.example.petroleumsystem;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private Label lblEmployee;
    @FXML
    private Label lblCustomer;
    @FXML
    private PieChart purchasePieChart;
    @FXML
    private Label lblPurchase;
    @FXML
    private Button btnRefresh;
    @FXML
    private ComboBox<Date> comboSearch;
    @FXML
    private PieChart salesPieChart;

    @FXML
    private Label lblSales;

    @FXML
    private ComboBox<Date> comboSearchSales;


    void totalEmployee() throws SQLException {
        db con = new db("select count(*) from employee ");
        while (db.resultSet.next()){
            lblEmployee.setText(String.valueOf(db.resultSet.getInt("count(*)")));
        }
    }
    void totalPricePurchase() throws SQLException {
        db con = new db("SELECT sum(total_price) FROM purchase ");
        while (db.resultSet.next()){
            lblPurchase.setText(String.valueOf("$ "+db.resultSet.getDouble("sum(total_price)")));
        }
    }

    void totalPriceSales() throws SQLException {
        db con = new db("SELECT sum(total_price) FROM sales ");
        while (db.resultSet.next()){
            lblSales.setText(String.valueOf("$ "+db.resultSet.getDouble("sum(total_price)")));
        }
    }

    void totalCustomer() throws SQLException {
        try{
            db con = new db("select count(*) from customer");
            while (db.resultSet.next()){
                lblCustomer.setText(String.valueOf(db.resultSet.getInt("count(*)")));
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            totalEmployee();
            totalCustomer();
            totalPricePurchase();
            totalPriceSales();
            CombofilterByDate();
            PieCharts();
            PieChartSales();
            CombofilterByDateSales();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void PieCharts() throws SQLException {
        db con = new db("select fuel_type , price_per_litters , litters , total_price from purchase ");
        while (db.resultSet.next()){
                ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList(
                        new PieChart.Data(db.resultSet.getString("fuel_type").concat(" litters "),db.resultSet.getInt("litters")),
                        new PieChart.Data(db.resultSet.getString("fuel_type").concat(" price_per_litter "), db.resultSet.getDouble("price_per_litters")),
                        new PieChart.Data(db.resultSet.getString("fuel_type").concat(" total_price "),db.resultSet.getInt("total_price"))
                );
                piechartData.forEach(data ->
                        data.nameProperty().bind(
                                Bindings.concat(
                                        data.getName()," amount : ",data.pieValueProperty()
                                )
                        )
                );
                purchasePieChart.getData().addAll(piechartData);
        }
    }

    void PieChartSales() throws SQLException {
        db con = new db("select fuel_type , price_per_litters , litters , total_price from sales ");
        while (db.resultSet.next()){
            ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList(
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" litters "),db.resultSet.getInt("litters")),
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" price_per_litter "), db.resultSet.getDouble("price_per_litters")),
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" total_price "),db.resultSet.getInt("total_price"))
            );
            piechartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName()," amount : ",data.pieValueProperty()
                            )
                    )
            );
            salesPieChart.getData().addAll(piechartData);
        }
    }

    @FXML
    void CombofilterByDate() throws SQLException {
        ObservableList<Date> dates = FXCollections.observableArrayList();
        db cons = new db("select date from purchase ");
        while (db.resultSet.next()){
            if(!dates.contains(db.resultSet.getDate("date"))){
                dates.addAll(db.resultSet.getDate("date"));
            }
        }
        comboSearch.setItems(dates);
        db con = new db("select fuel_type , price_per_litters , litters , total_price from purchase where date = '"+comboSearch.getSelectionModel().getSelectedItem()+"' ");
//        db con = new db("select fuel_type , price_per_litters , litters , total_price from purchase ");
        while (db.resultSet.next()){
            purchasePieChart.getData().clear();
            ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList(
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" litters "),db.resultSet.getInt("litters")),
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" price_per_litter "), db.resultSet.getDouble("price_per_litters")),
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" total_price "),db.resultSet.getInt("total_price"))
            );
            piechartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName()," amount : ",data.pieValueProperty()
                            )
                    )
            );
            purchasePieChart.getData().addAll(piechartData);

        }

    }
    @FXML
    void OnRefresh(ActionEvent event) throws SQLException {
        try{
            PieCharts();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void CombofilterByDateSales() throws SQLException {

        ObservableList<Date> dates = FXCollections.observableArrayList();
        db cons = new db("select date from sales ");
        while (db.resultSet.next()){
            if(!dates.contains(db.resultSet.getDate("date"))){
                dates.addAll(db.resultSet.getDate("date"));
            }
        }
        comboSearchSales.setItems(dates);
        db con = new db("select fuel_type , price_per_litters , litters , total_price from sales where date = '"+comboSearchSales.getSelectionModel().getSelectedItem()+"' ");
//        db con = new db("select fuel_type , price_per_litters , litters , total_price from purchase ");
        while (db.resultSet.next()){
            salesPieChart.getData().clear();
            ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList(
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" litters "),db.resultSet.getInt("litters")),
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" price_per_litter "), db.resultSet.getDouble("price_per_litters")),
                    new PieChart.Data(db.resultSet.getString("fuel_type").concat(" total_price "),db.resultSet.getInt("total_price"))
            );
            piechartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName()," amount : ",data.pieValueProperty()
                            )
                    )
            );
            salesPieChart.getData().addAll(piechartData);

        }

    }
}
