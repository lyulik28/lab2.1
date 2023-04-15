package org.example;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/*
Клас Main дочірній (розширює) Application та реалізує EventHandler
 */
public class Main extends Application implements EventHandler<ActionEvent>{

    int id;
    String name;
    String company;
    double price;
    String quality;
    boolean inStock;
    Button btnAdd;
    Button btnDelete;
    Button btnEdit;

    int idUpdate;
    String nameUpdate;
    String companyUpdate;
    double priceUpdate;
    String qualityUpdate;
    boolean inStockUpdate;

    TableView<Product> table;

    /*
    точка входу main
     */

    public static void main(String[] args) {
        launch(args);
    }

    /*
    Перевизначений метод start
    Створює VBox, таблицю та додає у неї колонки, вмістом яких будуть поля об'єкту Product
    Додає текстові поля, значення, що будуть у них введені присвоютьполя класу
    додається кнопка btnAdd

    Усі створені елементи будуть додані до scene
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Lab 2");

        btnAdd = new Button("Add");
        btnAdd.setOnAction(this);
        btnDelete = new Button("Delete");
        btnDelete.setOnAction(this);
        btnEdit = new Button("Edit");
        btnEdit.setOnAction(this);

        TableColumn<Product, Integer> idColumn = new TableColumn<Product, Integer>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> nameColumn = new TableColumn<Product, String>("Name");
        nameColumn.setMinWidth(80);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> companyColumn = new TableColumn<Product, String>("Company");
        companyColumn.setMinWidth(80);
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));

        TableColumn<Product, Double> priceColumn = new TableColumn<Product, Double>("Price");
        priceColumn.setMinWidth(20);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, String> qualityColumn = new TableColumn<Product, String>("Quality");
        qualityColumn.setMinWidth(50);
        qualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));

        TableColumn<Product, Boolean> inStockColumn = new TableColumn<Product, Boolean>("In stock");
        inStockColumn.setMinWidth(50);
        inStockColumn.setCellValueFactory(new PropertyValueFactory<>("in_stock"));


        table = new TableView<>();
        table.setItems(getAll());
        table.getColumns().addAll(idColumn, nameColumn, companyColumn, priceColumn, qualityColumn, inStockColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setFocusTraversable(false);
        nameField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                name = nameField.getText();
            }
        });

        TextField companyField = new TextField();
        companyField.setPromptText("Company");
        companyField.setFocusTraversable(false);
        companyField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                company = companyField.getText();
            }
        });

        TextField priceField = new TextField();
        priceField.setPromptText("Price (double)");
        priceField.setFocusTraversable(false);
        priceField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                price = Double.parseDouble(priceField.getText());
            }
        });

        TextField qualityField = new TextField();
        qualityField.setPromptText("Quality");
        qualityField.setFocusTraversable(false);
        qualityField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                quality = qualityField.getText();
            }
        });

        TextField inStockField = new TextField();
        inStockField.setPromptText("In stock (boolean)");
        inStockField.setFocusTraversable(false);
        inStockField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                inStock = Boolean.parseBoolean(inStockField.getText());
            }
        });

        Text text1 = new Text("Add new product (! press Enter after typing)");
        vBox.getChildren().add(text1);

        vBox.getChildren().add(nameField);
        vBox.getChildren().add(companyField);
        vBox.getChildren().add(priceField);
        vBox.getChildren().add(qualityField);
        vBox.getChildren().add(inStockField);
        vBox.getChildren().add(btnAdd);


        /*
        Створюється текстове поле idField
        Значення, що запишуть у дане поле буде присвоєно полем класу id
        Текст, текстове поле та кнопка btnDelete додаються до головного вікна
         */

        TextField idField = new TextField();
        idField.setPromptText("ID");
        idField.setFocusTraversable(false);
        idField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                id = Integer.parseInt(idField.getText());
            }
        });

        Text text2 = new Text("Delete product by id");
        vBox.getChildren().add(text2);
        vBox.getChildren().add(idField);
        vBox.getChildren().add(btnDelete);


        /*
            Створюється текстове поле idUpdateField
            Після введення знаходиться продукт з даним id
            Створюються такстові поля, в яких автоматично записуються дані об'єкту класу Product
            Введені дані (або дефолтні, якщо не було змін) записуються у поля класу Main
            Текстовы поля та кнопка btnEdit додаються у вікно
         */


        TextField idUpdateField = new TextField();
        idUpdateField.setPromptText("ID");
        idUpdateField.setFocusTraversable(false);
        idUpdateField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                idUpdate = Integer.parseInt(idUpdateField.getText());
                Product p = new Product();
                Product product = p.getById(idUpdate);

                if (product != null) {

                    TextField nameUpdateField = new TextField(product.getName());
                    nameUpdateField.setFocusTraversable(false);
                    nameUpdateField.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            nameUpdate = nameUpdateField.getText();
                        }
                    });

                    TextField companyUpdateField = new TextField(product.getCompany());
                    companyUpdateField.setFocusTraversable(false);
                    companyUpdateField.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            companyUpdate = companyUpdateField.getText();
                        }
                    });

                    TextField priceUpdateField = new TextField(Double.toString(product.getPrice()));
                    priceUpdateField.setFocusTraversable(false);
                    priceUpdateField.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            priceUpdate = Double.parseDouble(priceUpdateField.getText());
                        }
                    });

                    TextField qualityUpdateField = new TextField(product.getQuality());
                    qualityUpdateField.setFocusTraversable(false);
                    qualityUpdateField.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            qualityUpdate = qualityUpdateField.getText();
                        }
                    });

                    TextField inStockUpdateField = new TextField(Boolean.toString(product.isIn_stock()));
                    inStockUpdateField.setFocusTraversable(false);
                    inStockUpdateField.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            inStockUpdate = Boolean.parseBoolean(inStockUpdateField.getText());
                        }
                    });

                    vBox.getChildren().add(nameUpdateField);
                    vBox.getChildren().add(companyUpdateField);
                    vBox.getChildren().add(priceUpdateField);
                    vBox.getChildren().add(qualityUpdateField);
                    vBox.getChildren().add(inStockUpdateField);
                    vBox.getChildren().add(btnEdit);
                }
            }
        });

        Text text3 = new Text("Edit information (! press Enter after typing)");
        vBox.getChildren().add(text3);
        vBox.getChildren().add(idUpdateField);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    /*
    Метод getAll обирає усі дані з таблиці products, для запису кожного товару створює об'єкт класу Product,
    додає об'єкти у список
    повертає список об'єктів
     */
    public ObservableList<Product> getAll(){
        ObservableList<Product> allProducts = FXCollections.observableArrayList();
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop", "root", "springcourse");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from products");
            while (resultSet.next()){
                Product product = new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("company"), resultSet.getDouble("price"),
                        resultSet.getString("quality"), resultSet.getBoolean("in_stock"));
                allProducts.add(product);

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return allProducts;
    }

    /*
    Первизначений метод для обробки подій
    Якщо була натиснута кнопка btnAdd, то виконується додавання у базу даних
    завдяки методу класу Product
    Якщо btnDelete, то видаляється рядок у таблиці бази даних із вказаним індексом
    Якщо btnEdit, то оновлюються дані
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource()==btnAdd){
            Product product = new Product();
            product.addData(name, company, price, quality, inStock);
        }
        if(actionEvent.getSource()==btnDelete){
            Product product = new Product();
            product.deleteElement(id);
        }
        if(actionEvent.getSource()==btnEdit){
            Product product = new Product();
            product.update(idUpdate, nameUpdate, companyUpdate, priceUpdate, qualityUpdate, inStockUpdate);
        }
    }
}