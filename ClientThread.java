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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import strong_password_generator.Client.*;

/**
 *
 * @author Mahmoud
 */
public class ClientThread extends Thread{
    Socket s;
    DataInputStream in;
    DataOutputStream out;
    String user,password,type;
    private JLabel l;
    JButton login, reg, gen, old;
    JTextArea area;
    final String secretKey = "hhhhhhhhhh!!!!";
    int len;
    public ClientThread(Socket s,String user,String password,String type) throws IOException
    {
        this.s=s;
        this.user=user;
        this.password=password;
        this.type=type;
        in=new DataInputStream(s.getInputStream());
        out=new DataOutputStream(s.getOutputStream());
        
    }
    public ClientThread(Socket s,String type) throws IOException
    {
        this.s=s;
        this.type=type;
        in=new DataInputStream(s.getInputStream());
        out=new DataOutputStream(s.getOutputStream());
    }
    public ClientThread(Socket s,int len,String type) throws IOException
    {
        this.s=s;
        this.type=type;
        this.len=len;
        in=new DataInputStream(s.getInputStream());
        out=new DataOutputStream(s.getOutputStream());
    }
    public void GUI(JLabel l,JButton login,JButton reg,JButton gen,JButton old,JTextArea area)
    {
        this.l=l;
        this.login=login;
        this.reg=reg;
        this.gen=gen;
        this.old=old;
        this.area=area;
    }
    @Override
    public void run()
    {
        if(type.equals("register"))
        {
             try {
                out.writeUTF("register");
                out.writeUTF(user);
                out.writeUTF((password));
                Client.flag=true;
                l.setText("");
                if(in.readUTF().equals("Username exists"))
                {
                    l.setText("Username exists");
                    Client.flag=false;
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(type.equals("login"))
        {
            try {
                out.writeUTF("login");
                out.writeUTF(user);
                out.writeUTF(password);
                if(Client.flag==false)
                {
                    String err;
                    err=in.readUTF();
                    if(err.equals("k"))
                    {
                        l.setText("");
                        login.setEnabled(false);
                        reg.setEnabled(false);
                        gen.setEnabled(true);
                        old.setEnabled(true);
                    }
                    else if(err.equals("no"))
                    {
                        l.setText("Username or password is incorrect!");
                    }
                }
                else
                {
                    login.setEnabled(false);
                    reg.setEnabled(false);
                    gen.setEnabled(true);
                    old.setEnabled(true);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(type.equals("gen"))
        {
            try {
                out.writeUTF("gen");
                out.writeInt(len);
                String enc=in.readUTF();
                area.setText(new AES().decrypt(enc, secretKey));
            } catch (IOException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(type.equals("old"))
        {
            try {
                area.setText("");
                out.writeUTF("oldpass");
                while(true)
                {
                    String str=new AES().decrypt(in.readUTF(), secretKey);
                    if(str.isEmpty())
                        break;
                    area.append(str+"\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
