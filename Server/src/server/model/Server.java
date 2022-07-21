/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import dal.DBContext;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author duchi
 */
public class Server {

    private ServerSocket serverSocket;
    private ArrayList<Client> clients = new ArrayList<>();

    public Server() {
       
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public synchronized ArrayList<Client> getClients() {
        return clients;
    }

    public synchronized void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }
}
