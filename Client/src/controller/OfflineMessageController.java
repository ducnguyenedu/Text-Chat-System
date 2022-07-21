/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import view.OfflineMessage;

/**
 *
 * @author duchi
 */
public class OfflineMessageController {

    private HashMap<ChattigWindowController, Integer> room = new HashMap<>();
    private HashMap<String, String> userList = new HashMap<>();
    private OfflineMessage offlineMessage;
    private HashMap<Integer, ChattigWindowController> chatsIndexControll = new HashMap<>();
    private DefaultListModel<String> model = new DefaultListModel<>();

    public OfflineMessageController(ListOfUsersController listOfUsersController, HashMap<ChattigWindowController, Integer> room, HashMap<String, String> userList) {
        this.room = room;
        this.userList = userList;
        offlineMessage = new OfflineMessage(listOfUsersController.getListOfUsers(), false);
        offlineMessage.getjLabelUserLogin().setText("login as: " + listOfUsersController.getCurrentUserFullName());
        loadData();
        offlineMessage.getjButtonRead().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReadActionPerformed(evt);
            }

        });
    }

    public HashMap<ChattigWindowController, Integer> getRoom() {
        return room;
    }

    public void setRoom(HashMap<ChattigWindowController, Integer> room) {
        this.room = room;
    }

    public HashMap<String, String> getUserList() {
        return userList;
    }

    public void setUserList(HashMap<String, String> userList) {
        this.userList = userList;
    }

    

    public void open() {
        loadData();
        offlineMessage.setVisible(true);
    }

    public void loadData() {
        int indexOfElement = 0;
        model = new DefaultListModel<>();
        for (Map.Entry<ChattigWindowController, Integer> entry : room.entrySet()) {
            ChattigWindowController key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key.getReceiver());
            System.out.println(userList.get(key.getReceiver()));
            model.addElement(userList.get(key.getReceiver()) + "(" + value + ")");

            chatsIndexControll.put(indexOfElement, key);
            indexOfElement++;
        }
        offlineMessage.getjListOfflineMess().setModel(model);
    }

    private void jButtonReadActionPerformed(ActionEvent evt) {
        try {
            int chat = offlineMessage.getjListOfflineMess().getSelectedIndex();

            ChattigWindowController cd = chatsIndexControll.get(chat);
            cd.setVisible(true);
        } catch (java.lang.NullPointerException e) {
            JOptionPane.showMessageDialog(offlineMessage, "choose one chat room", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
}
