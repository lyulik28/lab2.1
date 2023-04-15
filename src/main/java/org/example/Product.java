package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private String company;
    private double price;
    private String quality;
    private boolean in_stock;

    /*
    Конструктор без параметрів
     */
    public Product() {
    }

    /*
    Конструктор з параметрами
     */
    public Product(int id, String name, String company, double price, String quality, boolean in_stock) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.price = price;
        this.quality = quality;
        this.in_stock = in_stock;
    }

    /*
    Гетери та сетери
     */

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public double getPrice() {
        return price;
    }

    public String getQuality() {
        return quality;
    }

    public boolean isIn_stock() {
        return in_stock;
    }

    /*
    Метод addData приймає String name, String company, double price, String quality, boolean in_stock
    Підключається до бази даних
    Створю запит завдяки оператору SQL - PreparedStatement,
     в параметри якого встановлюються значення, що приймає функція
     Зовнішній ключ у таблиці AUTO_INCREMENT, тому його не потрібно передавати
     */

    public void addData(String name, String company, double price, String quality, boolean in_stock){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop", "root", "springcourse");
            Statement statement = connection.createStatement();

            PreparedStatement sql = connection.prepareStatement("insert into products "
                    +" (`name`, `company`, `price`, `quality`, `in_stock`)"
                    +" values (?, ?, ?, ?, ?)");

            sql.setString(1, name);
            sql.setString(2, company);
            sql.setDouble(3, price);
            sql.setString(4, quality);
            sql.setBoolean(5, in_stock);
            sql.executeUpdate();


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /*
    Метод deleteElement приймає int id
    Підключення до бази даних
    Видаляємо елемент з таблиці за введеним первинним ключем
     */

    public void deleteElement(int id){
                try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop", "root", "springcourse");
            Statement statement = connection.createStatement();

                    PreparedStatement sql = connection.prepareStatement("delete from products "
                            +" where id=?");

                    sql.setString(1, String.valueOf(id));
                    sql.executeUpdate();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /*
    Метод getById приймає int id
    Підключається до бази даних
    Знаходить рядок у таблиці з потрібним id
    Створюємо об'єкт класу Product,
    завдяки конструктору записуються необхідні поля об'єкту,
    усі дані бере із обраного елементу таблиці
     */
    public Product getById(int id){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop", "root", "springcourse");
            Statement statement = connection.createStatement();

            PreparedStatement el = connection.prepareStatement("select * from products"
            +" where id =?");
            el.setString(1, String.valueOf(id));

            ResultSet resultSet = el.executeQuery();
            if (resultSet.next()) {
                Product product = new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("company"), resultSet.getDouble("price"),
                        resultSet.getString("quality"), resultSet.getBoolean("in_stock"));
                return product;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /*
        Метод addData приймає String name, String company, double price, String quality, boolean in_stock
        З'єднується з базою даних,
        оновлює кожну колонку вибраного за id елементу тблиці бази даних
     */
    public void update(int id, String name, String company, double price, String quality, boolean in_stock){
                try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop", "root", "springcourse");
            Statement statement = connection.createStatement();

                    PreparedStatement sql = connection.prepareStatement("update products set"
                            +" `name` = ?"
                            +", `company` = ?"
                            +", `price` = ?"
                            +", `quality` = ?"
                            +", `in_stock` = ?"
                            +" where (`id`=?)");

                    sql.setString(1, name);
                    sql.setString(2, company);
                    sql.setDouble(3, price);
                    sql.setString(4, quality);
                    sql.setBoolean(5, in_stock);
                    sql.setInt(6, id);
                    sql.executeUpdate();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
