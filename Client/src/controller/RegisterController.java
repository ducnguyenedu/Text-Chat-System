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
import view.Register;

/**
 *
 * @author duchi
 */
public class RegisterController extends BasicFrameController {

    private Register register;
private ClientController clientController;
    public RegisterController(ClientController clientController) {
        super(clientController);
       this.clientController =clientController;
       
        this.register = new Register();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                register.setVisible(true);
            }
        });
        register.getjRegister().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRegisterActionPerformed(evt);
            }
        });

        register.getjCancel().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCancelActionPerformed(evt);
            }

        });
    }

    @Override
    public void getRespone(ExchangeMessage message) {
        if (message.getCommand() == 1) {

            if (message.isIsSuccess()) {
                JOptionPane.showMessageDialog(register, message.getServerMessage(), "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(register, message.getServerMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void jRegisterActionPerformed(ActionEvent evt) {
        String login = register.getjLogin().getText();
        String fullName = register.getjFullName().getText();
        String pass = String.valueOf(register.getjPassword().getPassword());
        String re_type = String.valueOf(register.getjReTypePassword().getPassword());
        if (login.length() == 0 || fullName.length() == 0 || pass.length() == 0 || re_type.length() == 0) {
            JOptionPane.showMessageDialog(register, "You need to fill all to register", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {

            if (pass.equals(re_type)) {
                ExchangeMessage message = new ExchangeMessage();
                message.setCommand(1);
                message.setFullName(fullName);
                message.setLogin(login);
                message.setPassword(pass);
                sendrequest(message);
            } else {
                JOptionPane.showMessageDialog(register, "password and re-type password do not match", "Warning", JOptionPane.ERROR_MESSAGE);

            }
        }

    }

    private void JCancelActionPerformed(ActionEvent evt) {
       register.setVisible(false);
        LoginController loginController = new LoginController(clientController);
    }
}
