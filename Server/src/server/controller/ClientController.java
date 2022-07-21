/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import comunication_model.ExchangeMessage;
import dal.DAOChatRoom;
import dal.DAOUser;
import dal.DBContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.model.Client;
import server.model.Server;

/**
 *
 * @author duchi
 */
public class ClientController extends Thread {

    private Client current;
    private Server server;

    boolean isRunning = true;

    public ClientController(Client current, Server server) {

        this.current = current;
        this.server = server;

    }

    public void sendMessage(Client client, ExchangeMessage message) {
        try {
            OutputStream clios = client.getSocket().getOutputStream();
            ObjectOutputStream clioos = new ObjectOutputStream(clios);
            clioos.writeObject(message);

        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                InputStream is = current.getSocket().getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                ExchangeMessage message = (ExchangeMessage) ois.readObject();
                if (message.getCommand() == 1) {
                    String login = message.getLogin();
                    DAOUser daou = new DAOUser(server, current);
                    ExchangeMessage responeMess = daou.register(message);
                    sendMessage(current, responeMess);
                    //when the user action is successful, the other user needs to know there is a new register 
                    if (responeMess.isIsSuccess()) {
                        for (Client client : server.getClients()) {

                            if ((client.getUser() != null)) {
                                daou = new DAOUser(server, current);
                                ExchangeMessage respone = daou.reloadNewRegister(login, client.getUser());
                                sendMessage(client, respone);
                            }
                        }
                    }
                }
                if (message.getCommand() == 2) {
                    DAOUser daou = new DAOUser(server, current);
                    ExchangeMessage responeMess = daou.login(message);
                    sendMessage(current, responeMess);
                    //when the user action is successful, the other user needs to know there is a new register 
                    if (responeMess.isIsSuccess()) {
                        for (Client client : server.getClients()) {

                            if ((client.getUser() != null)) {
                                if (!client.getUser().getLogin().equals(current.getUser().getLogin())) {
                                    daou = new DAOUser(server, current);
                                    ExchangeMessage respone = daou.reloadNewLogin();
                                    sendMessage(client, respone);
                                }
                            }
                        }
                    }
                }
                if (message.getCommand() == 3) {
                    DAOChatRoom daocr = new DAOChatRoom(server, current);
                    ExchangeMessage responeMess = daocr.insertMessage(message);
                    sendMessage(current, responeMess);
                    //when the user action is successful, the other user needs to know there is a new register 
                    if (responeMess.isIsSuccess()) {
                        for (Client client : server.getClients()) {

                            if ((client.getUser() != current.getUser()) && (client.getUser() != null)) {
                                if (client.getUser().getLogin().equals(message.getReceiver())) {
                                    daocr = new DAOChatRoom(server, current);
                                    ExchangeMessage respone = daocr.reloadMessReceiver(responeMess, client.getUser());
                                    sendMessage(client, respone);
                                }
                            }
                        }
                    }
                }
                if (message.getCommand() == 4) {
                    DAOChatRoom daocr = new DAOChatRoom(server, current);
                    ExchangeMessage responeMess = daocr.readChat(message);
                    for (Client client : server.getClients()) {
                        if (client.getUser() != null) {
                            if (client.getUser().getLogin().equals(current.getUser().getLogin())) {
                                sendMessage(client, responeMess);
                            }
                        }

                    }

                }

            } catch (IOException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                isRunning = false;

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                isRunning = false;

            }

        }

        if (current.getUser() != null) {

            if (isOnlyOne()) {
                DAOUser daou = new DAOUser(server, current);
                ExchangeMessage message = daou.reloadLogot();
                for (Client client : server.getClients()) {
                    if (!client.getUser().getLogin().equals(current.getUser().getLogin())) {

                        sendMessage(client, message);
                    }
                }
            }
        }

        server.getClients().remove(current);
    }

    private boolean isOnlyOne() {
        for (Client client : server.getClients()) {
            if ((!client.equals(current)) && current.getUser().getLogin().equals(client.getUser().getLogin())) {

                return false;
            };
        }

        return true;
    }
}
