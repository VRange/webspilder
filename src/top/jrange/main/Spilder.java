package top.jrange.main;

import java.sql.*;

/**
 * Created by lenovo on 2016-02-04.
 */
public class Spilder {
    public static void main(String[] args){
        String frontPage = "http://www.cnblogs.com/ya-cpp/";
        Connection connection = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf8";
            connection = DriverManager.getConnection(url, "root", "");
            System.out.println("connection...");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        String sql = null;
        String pageUrl = frontPage;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;

        if(connection != null){
            //create database and tables that will be needed
            try{
                sql = "CREATE DATABASE IF NOT EXISTS WebPage";
                statement = connection.createStatement();
                statement.executeUpdate(sql);

                sql = "USE WebPage";
                statement = connection.createStatement();
                statement.executeUpdate(sql);

                sql = "CREATE TABLE IF NOT EXISTS record(recordID int (5) NOT NULL AUTO_INCREMENT," +
                        "url text NOT NULL, spilder TINYINT(1) NOT NULL," +
                        "PRIMARY KEY (recordID)) ENGINE = InnoDB DEFAULT CHARSET=UTF8";
                statement = connection.createStatement();
                statement.executeUpdate(sql);

                sql = "CREATE TABLE IF NOT EXISTS tags(tagnum int(4) not null auto_increment, tagname text not null, primary key (tagnum)) engine=InnoDB DEFAULT CHARSET=utf8";
                statement = connection.createStatement();
                statement.executeUpdate(sql);
                sql = "INSERT INTO record(url, spilder) VALUES ('" + frontPage +"', 0)";
                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while(true){
            try{
                HTTPGet.getByString(pageUrl, connection);
                count++;
            } catch (Exception e) {

            }

            try{
                sql = "UPDATE record SET spilder = 1 WHERE url = '"+ pageUrl +"'";
                statement = connection.createStatement();
                if(statement.executeUpdate(sql) > 0){
                    sql = "SELECT * FROM record WHERE spilder = 0";
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(sql);

                    if(resultSet.next()){
                        pageUrl = resultSet.getString(2);
                    } else {
                        break;
                    }
                }

                if(count > 1000 || pageUrl == null){
                    break;
                }
            } catch (SQLException e) {

            }
        }
    }

}
