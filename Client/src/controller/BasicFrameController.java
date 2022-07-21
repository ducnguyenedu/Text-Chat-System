/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import client.ClientController;
import comunication_model.ExchangeMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duchi
 */
public abstract class BasicFrameController {

    private ClientController clientController;

    public BasicFrameController(ClientController clientController) {
        this.clientController = clientController;
        clientController.setBfc(this);
       
    }

    public void sendrequest(ExchangeMessage message) {
        try {
            OutputStream os = clientController.getSocket().getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getRespone(ExchangeMessage message) {

    }
;
}
