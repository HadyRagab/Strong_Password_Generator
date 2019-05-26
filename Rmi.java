/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong_password_generator;
import java.rmi.*;
import java.rmi.server.*;


/**
 *
 * @author Mahmoud
 */
public class Rmi extends UnicastRemoteObject implements Connected{
    
    private String msg;
    public Rmi(String msg) throws RemoteException
    {
        this.msg=msg;
    }
    public String Connected() throws RemoteException
    {
        return msg;
    }
}
