/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strong_password_generator;
import java.rmi.*;
/**
 *
 * @author Mahmoud
 */
public interface Connected extends Remote{
        public String Connected() throws RemoteException;
}
