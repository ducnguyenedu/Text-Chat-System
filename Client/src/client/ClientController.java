/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import comunication_model.ExchangeMessage;
import controller.BasicFrameController;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author duchi
 */
public class ClientController extends Thread {

    private Socket socket;
    private BasicFrameController bfc;

    private String currentUserLogin;
    private String currentUserFullName;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BasicFrameController getBfc() {
        return bfc;
    }

    public void setBfc(BasicFrameController bfc) {
        this.bfc = bfc;
    }

    public ClientController(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * To get current user and user for key communication
     */
    public String getCurrentUserLogin() {
        return currentUserLogin;
    }

    /**
     *
     * To known who is current user and user for key communication
     */
    public void setCurrentUserLogin(String currentUser) {
        this.currentUserLogin = currentUser;
    }

    /**
     *
     * To get current user and user for display
     */
    public String getCurrentUserFullName() {
        return currentUserFullName;
    }

    /**
     *
     * To known who is current user and user for display
     */
    public void setCurrentUserFullName(String currentUserFullName) {
        this.currentUserFullName = currentUserFullName;
    }

    private boolean isAlive = true;

    @Override
    public void run() {

        while (isAlive) {
            try {

                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                ExchangeMessage message = (ExchangeMessage) ois.readObject();
                bfc.getRespone(message);

            } catch (IOException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
               
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                
            }

        }

        
    }
}
