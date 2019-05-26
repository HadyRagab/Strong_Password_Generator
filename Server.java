/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong_password_generator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mahmoud
 */
public class Server {
    public static void main(String[] args) throws IOException
    {
        try {
            Registry r=java.rmi.registry.LocateRegistry.createRegistry(1099);
            r.rebind("Con", new Rmi("Connected to server"));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        ServerSocket ss=new ServerSocket(9093);
        Socket s;
        thread th;
        System.out.println("Server is running");
        while(true)
        {
            s=ss.accept();
            System.out.println("Connection Established: "+s);
            th=new thread(s);
            th.run();
            
        }
    }
}