/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ChatLine;
import model.ChatRoom;
import model.User;

/**
 *
 * @author duchi
 */
public class DBContext {

    public Connection connection;

    public DBContext() {
        try {
            String user = "ducnm";
            String pass = "12345";
            String url = "jdbc:sqlserver://localhost:1433;databaseName=J2.L.P0007TCS";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
