/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import client.ClientController;
import comunication_model.ExchangeMessage;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import view.ListOfUsers;

/**
 *
 * @author duchi
 */
public class ListOfUsersController extends BasicFrameController {

    private ClientController clientController;
    private ListOfUsers listOfUsers;

    private HashMap<String, String> onlineUser;
    private HashMap<Integer, String> rooms;
    private HashMap<String, String> userList;
    private HashMap<Integer, ArrayList<Integer>> roomdata;
    private HashMap<Integer, String> chatUser;
    private HashMap<Integer, String> conntent;
    private HashMap<Integer, Boolean> sendisRead;

    private DefaultListModel<String> model = new DefaultListModel<>();

    int indexOfElement = 0;
    HashMap<Integer, ChattigWindowController> chatsIndexControll = new HashMap<>();

    public ListOfUsersController(ClientController clientController,
            HashMap<String, String> onlineUser,
            HashMap<Integer, String> rooms,
            HashMap<String, String> userList,
            HashMap<Integer, ArrayList<Integer>> roomdata,
            HashMap<Integer, String> chatUser,
            HashMap<Integer, String> conntent,
            HashMap<Integer, Boolean> sendisRead
    ) {
        super(clientController);
//        this.onlineUser = onlineUser;
//        this.rooms = rooms;
        this.clientController = clientController;
        this.listOfUsers = new ListOfUsers();

        listOfUsers.setVisible(true);
        listOfUsers.getjListRegisteredUser();
        listOfUsers.getjLabelUserName().setText("Login as:" + clientController.getCurrentUserFullName());
        this.onlineUser = onlineUser;
        this.rooms = rooms;
        this.userList = userList;
        this.roomdata = roomdata;
        this.chatUser = chatUser;
        this.conntent = conntent;
        this.sendisRead = sendisRead;
        loadData();

        listOfUsers.getjBtnChat().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnChatActionPerformed(evt);
            }

        });
        listOfUsers.getjBtn_OfflineMessages().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn_OfflineMessagesActionPerformed(evt);
            }

        });
        controller = new OfflineMessageController(this, chatRoomOfflineMess, userList);
        loadOfflineMessageBtn();
    }

    public String getCurrentUserFullName() {
        return clientController.getCurrentUserFullName();
    }

    public String getCurrentUserLogin() {
        return clientController.getCurrentUserLogin();
    }

    @Override
    public void getRespone(ExchangeMessage message) {
        if (message.getCommand() == 1) {
            HashMap<Integer, String> rooms = message.getUserRooms();
            HashMap<String, String> userList = message.getUserlist();
            for (Map.Entry<Integer, String> entry : rooms.entrySet()) {
                Integer key = entry.getKey();
                String value = entry.getValue();//login
                ArrayList<Integer> cllist = new ArrayList<>();
                roomdata.put(key, cllist);
               
                ChattigWindowController windowController = new ChattigWindowController(this, value, userList.get(value), key, roomdata, chatUser, conntent, sendisRead);
                model.addElement(userList.get(value));
                chatsIndexControll.put(indexOfElement, windowController);
                indexOfElement++;
            }
            listOfUsers.getjListRegisteredUser().setModel(model);

        }
        if (message.getCommand() == 2) {
            HashMap<String, String> newonline = message.getOnlineUser();
            for (Map.Entry<Integer, ChattigWindowController> entry : chatsIndexControll.entrySet()) {
                Integer key = entry.getKey();
                ChattigWindowController value = entry.getValue();
                if (newonline.containsKey(value.getReceiver())) {

                    model.setElementAt(newonline.get(value.getReceiver()) + "(online)", key);
                }
            }
            listOfUsers.getjListRegisteredUser().setModel(model);
        }
        if (message.getCommand() == 3) {
            if (!message.isIsSuccess()) {
                JOptionPane.showMessageDialog(listOfUsers, message.getServerMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                int roomid = message.getRoom();

                for (Map.Entry<Integer, ChattigWindowController> entry : chatsIndexControll.entrySet()) {
                    Integer key = entry.getKey();
                    ChattigWindowController value = entry.getValue();
                    if (value.getRoomid() == roomid) {
                        int clid = message.getRoomdata().get(roomid).get(0);
                        value.getRoomdata().get(roomid).add(clid);
                        value.getChatUser().put(clid, message.getChatUser().get(clid));
                        value.getConntent().put(clid, message.getConntent().get(clid));
                        value.getSendisRead().put(clid, message.getIsRead().get(clid));
                        value.loadData();
                    }
                    value.isVisible();
                }
                loadOfflineMessageBtn();

            }

        }
        if (message.getCommand() == 4) {
            if (message.isIsSuccess()) {
                loadOfflineMessageBtn();

            }
        }
        if (message.getCommand() == 5) {
            HashMap<String, String> logout = message.getOnlineUser();
            for (Map.Entry<Integer, ChattigWindowController> entry : chatsIndexControll.entrySet()) {
                Integer key = entry.getKey();
                ChattigWindowController value = entry.getValue();
                if (logout.containsKey(value.getReceiver())) {

                    model.setElementAt(logout.get(value.getReceiver()), key);
                }
            }
            listOfUsers.getjListRegisteredUser().setModel(model);
        }
    }

    private void loadData() {

        for (Map.Entry<Integer, String> entry : rooms.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals(clientController.getCurrentUserLogin())) {
                if (onlineUser.containsKey(value)) {
                    model.addElement(userList.get(value) + "(online)");
                } else {
                    model.addElement(userList.get(value));
                }
                ChattigWindowController windowController = new ChattigWindowController(this, value, userList.get(value), key, roomdata, chatUser, conntent, sendisRead);
                chatsIndexControll.put(indexOfElement, windowController);
                indexOfElement++;
            }
        }
        listOfUsers.getjListRegisteredUser().setModel(model);

    }

    public ListOfUsers getListOfUsers() {
        return listOfUsers;
    }

    private void jBtnChatActionPerformed(ActionEvent evt) {
        try {
            int chat = listOfUsers.getjListRegisteredUser().getSelectedIndex();

            ChattigWindowController cd = chatsIndexControll.get(chat);
            cd.setVisible(true);
        } catch (java.lang.NullPointerException e) {
            JOptionPane.showMessageDialog(listOfUsers, "choose one chat room", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }

    HashMap<ChattigWindowController, Integer> chatRoomOfflineMess = new HashMap<>();
    OfflineMessageController controller;

    ;

    private void loadOfflineMessageBtn() {
        int numofflinemess = 0;
        chatRoomOfflineMess = new HashMap<>();
        for (Map.Entry<Integer, ChattigWindowController> entry : chatsIndexControll.entrySet()) {
            int numofflinemessroom = 0;
            Integer key = entry.getKey();
            ChattigWindowController value = entry.getValue();
            int roomid = value.getRoomid();
            ArrayList<Integer> chatlineid = roomdata.get(roomid);
            for (Integer integer : chatlineid) {
                if (!sendisRead.get(integer)) {
                    numofflinemess++;
                    numofflinemessroom++;
                }
            }
            if (numofflinemessroom == 0) {
                continue;
            }
            chatRoomOfflineMess.put(value, numofflinemessroom);
        }
        listOfUsers.getjBtn_OfflineMessages().setText("Offline messages " + "(" + numofflinemess + ")");
        controller.setRoom(chatRoomOfflineMess);
        controller.setUserList(userList);
        controller.loadData();
    }

    private void jBtn_OfflineMessagesActionPerformed(ActionEvent evt) {
        controller.open();
    }
}
