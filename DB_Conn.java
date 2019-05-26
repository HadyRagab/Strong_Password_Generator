/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong_password_generator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mahmoud
 */
public class DB_Conn {
    public Connection con;
    public void Connect() throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");  
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dis_project","root","");
    }
    public void CloseConn()
    {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB_Conn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
