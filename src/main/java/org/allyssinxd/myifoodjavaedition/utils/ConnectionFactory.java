package org.allyssinxd.myifoodjavaedition.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static String host = "localhost";
    public static int port = 3306;
    public static String user = "root";
    public static String password = "Nossil@12";
    public static String database = "myifood";

    public static Connection CreateConnection(){
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        Connection connection;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco de dados.");
            e.printStackTrace();
            return null;
        }

        return connection;
    }

    public static void CloseConnection(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Não foi possivel fechar a conexão no banco de dados");
            e.printStackTrace();
        }
    }
}
