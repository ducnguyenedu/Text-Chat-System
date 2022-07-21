/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import dal.DBContext;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.ServerController;
import server.model.Server;

/**
 * J2.L.P0007
 * @author duchi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Server server = new Server();
            //DBContext db= new DBContext();
            ServerSocket socket = new ServerSocket(9999);
            server.setServerSocket(socket);
            
            ServerController serverController = new ServerController(server);
            serverController.start();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
