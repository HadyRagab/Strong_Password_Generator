/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong_password_generator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mahmoud
 */
public class thread extends Thread{
    DB_Conn c;
    Socket s;
    String username="";
    final String secretKey = "hhhhhhhhhh!!!!";
    public thread(Socket s)
    {
        this.s=s;
    }
    @Override
    public void run()
    {
        c=new DB_Conn();
        try {
            c.Connect();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataInputStream in = null;
        DataOutputStream out = null;
        String str="";
        while (true)
        {
            try {
                in= new DataInputStream(s.getInputStream());
                out = new DataOutputStream(s.getOutputStream());
                 str=in.readUTF();
            } catch (IOException ex) {
            Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(str.equals("login"))
            {
                String user;
                String pass;
                try {
                    user=in.readUTF();
                    pass=in.readUTF();
                    Statement stmt=c.con.createStatement();
                    ResultSet rs=stmt.executeQuery("SELECT Username,Pass FROM login WHERE Username='"+user+"';");
                  
                    String str2="";
                    if( rs.next())
                        str2=rs.getString(2);
                    System.out.println(str2);
                    System.out.println(pass);
                    if(str2.equals(pass))
                    {
                        out.writeUTF("k");
                        username=user;
                    }
                    else
                         out.writeUTF("no");
                } catch (IOException ex) {
                    Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
            }
            else if(str.equals("register"))
            {
                String user;
                String pass;
                try {
                    user=in.readUTF();
                    pass=in.readUTF();
                    System.out.println(user+" "+pass);
                    Statement stmt=c.con.createStatement();
                    ResultSet rs=stmt.executeQuery("SELECT Username,Pass FROM login WHERE Username='"+user+"';");
                    if(rs.next())
                        out.writeUTF("Username exists");
                    else
                    {
                        stmt.executeUpdate("insert into login(Username,Pass) values('"+user+"','"+pass+"');");
                    }
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(str.equals("oldpass"))
            {
                try {
                    Statement stmt=c.con.createStatement();
                    ResultSet rs=stmt.executeQuery("SELECT gen_pass FROM generated WHERE Username='"+username+"';");
                    while(rs.next())
                        out.writeUTF(rs.getString(1));
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(str.equals("gen"))
            {
                try {
                    int len=in.readInt();
                    String SP=new strong_password_generator().generatePassword(len);
                    System.out.print(SP);
                    String encryptedSP=new AES().encrypt(SP, secretKey);
                    Statement stmt=c.con.createStatement();
                    stmt.executeUpdate("insert into generated(Username,gen_pass) values('"+username+"','"+encryptedSP+"');");
                    out.writeUTF(encryptedSP);
                    
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(thread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
    }
     
}
