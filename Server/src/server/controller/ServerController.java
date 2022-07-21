/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import dal.DBContext;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.model.Client;
import server.model.Server;

/**
 *
 * @author duchi
 */
public class ServerController extends Thread {

    private Server server;
    

    public ServerController(Server server) {
        this.server = server;
       
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = server.getServerSocket().accept();

                Client client = new Client();
                client.setSocket(socket);
                server.getClients().add(client);
                ClientController clientController = new ClientController(client, server);
                clientController.start();
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
