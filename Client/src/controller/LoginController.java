/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import client.ClientController;
import comunication_model.ExchangeMessage;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.Login;

/**
 *
 * @author duchi
 */
public class LoginController extends BasicFrameController {

    private Login login;
    private ClientController clientController;

    public LoginController(ClientController clientController) {
        super(clientController);
        this.clientController = clientController;

        this.login = new Login();
        login.setVisible(true);

        login.getjPassword().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordActionPerformed(evt);
            }

        });

        login.getjBtn_Register().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn_RegisterActionPerformed(evt);
            }

        });

        login.getjBtn_Cancel().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn_CancelActionPerformed(evt);
            }

        });
    }

    @Override
    public void getRespone(ExchangeMessage message) {
        if (message.getCommand() == 2) {

            if (message.isIsSuccess()) {
                clientController.setCurrentUserLogin(login.getjTextLogin().getText());
                clientController.setCurrentUserFullName(message.getOnlineUser().get(login.getjTextLogin().getText()));
                login.setVisible(false);

                ListOfUsersController listOfUsersController = new ListOfUsersController(clientController, 
                        message.getOnlineUser(),
                        message.getUserRooms(),
                        message.getUserlist(),
                        message.getRoomdata(),
                        message.getChatUser(),
                        message.getConntent(),
                        message.getIsRead()
                );
            } else {
                JOptionPane.showMessageDialog(login, message.getServerMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void jPasswordActionPerformed(ActionEvent evt) {
        String loginName = login.getjTextLogin().getText();
        String pass = String.valueOf(login.getjPassword().getPassword());
        if (loginName.length() == 0 || pass.length() == 0) {
            JOptionPane.showMessageDialog(login, "You need to fill all to login", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            ExchangeMessage message = new ExchangeMessage();
            message.setCommand(2);
            message.setLogin(loginName);
            message.setPassword(pass);
            sendrequest(message);
        }
    }

    private void jBtn_RegisterActionPerformed(ActionEvent evt) {
        login.setVisible(false);
        RegisterController registerController = new RegisterController(clientController);

    }

    private void jBtn_CancelActionPerformed(ActionEvent evt) {
        System.exit(1);
    }
}
