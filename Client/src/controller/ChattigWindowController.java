/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import client.ClientController;
import comunication_model.ExchangeMessage;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import view.ChattingWindow;
import view.ListOfUsers;

/**
 *
 * @author duchi
 */
public class ChattigWindowController {

    private ChattingWindow chattingWindow;
    private String receiver;
    private ListOfUsersController listOfUsersController;
    private int roomid;

    private HashMap<Integer, ArrayList<Integer>> roomdata;
    private HashMap<Integer, String> chatUser;
    private HashMap<Integer, String> conntent;
    private HashMap<Integer, Boolean> sendisRead;

    public ListOfUsersController getListOfUsersController() {
        return listOfUsersController;
    }

    public void setListOfUsersController(ListOfUsersController listOfUsersController) {
        this.listOfUsersController = listOfUsersController;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public HashMap<Integer, ArrayList<Integer>> getRoomdata() {
        return roomdata;
    }

    public void setRoomdata(HashMap<Integer, ArrayList<Integer>> roomdata) {
        this.roomdata = roomdata;
    }

    public HashMap<Integer, String> getChatUser() {
        return chatUser;
    }

    public void setChatUser(HashMap<Integer, String> chatUser) {
        this.chatUser = chatUser;
    }

    public HashMap<Integer, String> getConntent() {
        return conntent;
    }

    public void setConntent(HashMap<Integer, String> conntent) {
        this.conntent = conntent;
    }

    public HashMap<Integer, Boolean> getSendisRead() {
        return sendisRead;
    }

    public void setSendisRead(HashMap<Integer, Boolean> sendisRead) {
        this.sendisRead = sendisRead;
    }

    public ChattigWindowController(ListOfUsersController listOfUsersController, String receiver, String receiverName,
            int roomid,
            HashMap<Integer, ArrayList<Integer>> roomdata,
            HashMap<Integer, String> chatUser,
            HashMap<Integer, String> conntent,
            HashMap<Integer, Boolean> sendisRead) {
        this.roomid = roomid;
        this.roomdata = roomdata;
        this.chatUser = chatUser;
        this.conntent = conntent;
        this.sendisRead = sendisRead;
        this.listOfUsersController = listOfUsersController;
        this.receiver = receiver;
        this.chattingWindow = new ChattingWindow(listOfUsersController.getListOfUsers(), false);
        chattingWindow.getjLabelDisplay().setText(listOfUsersController.getCurrentUserFullName() + " chat to " + receiverName);
        loadData();
        chattingWindow.getjTextMessage().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextMessageActionPerformed(evt);
            }

        });
    }

    public void loadData() {
        String allchat = "";
        for (Integer integer : roomdata.get(roomid)) {
            allchat = allchat + chatUser.get(integer) + ":" + "\n" + "     " + conntent.get(integer) + "\n";
        }

        chattingWindow.getjTextAreaChat().setText(allchat);

    }

    private void jTextMessageActionPerformed(ActionEvent evt) {
        ExchangeMessage message = new ExchangeMessage();
        message.setCommand(3);
        message.setReceiver(receiver);
        message.setRoom(roomid);
        message.setMessageContent(chattingWindow.getjTextMessage().getText());
        listOfUsersController.sendrequest(message);
        chattingWindow.getjTextMessage().setText("");
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isVisible() {
        boolean b = chattingWindow.isVisible();
        if (b) {
            HashMap<Integer, Boolean> unread = new HashMap<>();
            for (Integer integer : roomdata.get(roomid)) {
                if (!sendisRead.get(integer)) {
                    unread.put(integer, true);
                    sendisRead.put(integer, true);
                }
            }
            if(unread.size()>0){
            ExchangeMessage message = new ExchangeMessage();
            message.setIsRead(unread);
            message.setCommand(4);
            listOfUsersController.sendrequest(message);}
        }
        return b;
    }

    public void setVisible(boolean b) {
        chattingWindow.setVisible(b);

        if (b) {
            HashMap<Integer, Boolean> unread = new HashMap<>();
            for (Integer integer : roomdata.get(roomid)) {
                if (!sendisRead.get(integer)) {
                    unread.put(integer, true);
                    sendisRead.put(integer, true);
                }
            }
            if(unread.size()>0){
            ExchangeMessage message = new ExchangeMessage();
            message.setIsRead(unread);
            message.setCommand(4);
            listOfUsersController.sendrequest(message);}
        }
        
    }

    public ChattingWindow getChattingWindow() {
        return chattingWindow;
    }

    public void setChattingWindow(ChattingWindow chattingWindow) {
        this.chattingWindow = chattingWindow;
    }

}
