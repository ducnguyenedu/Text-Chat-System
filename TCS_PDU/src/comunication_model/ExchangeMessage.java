/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunication_model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author duchi
 */
public class ExchangeMessage implements Serializable {

    private int command; //1 register, 2 login, 3 load list user data
    private boolean isSuccess;
    private String serverMessage;

    private String fullName;
    private String login;
    private String password;

    private HashMap<String, String> onlineUsers = new HashMap<>();
    private HashMap<Integer, String> userRooms = new HashMap<>();
    private HashMap<String, String> userlist = new HashMap<>();

    private HashMap<Integer, ArrayList<Integer>> roomdata = new HashMap<>();//room id, list id chat line
    private HashMap<Integer, String> chatUser = new HashMap<>();//chat line id, user who chat this chat line
    private HashMap<Integer, String> conntent = new HashMap<>();//chat line id, conntent of this chat line
    private HashMap<Integer, Boolean> isRead = new HashMap<>();//chat line id, is this user read this chat line
    
    private int room;//want to send message, need to know room id
    private String messageContent;// message content
    private String receiver;
    /**
     * 1 is Register 2 is Login 3 send message 4 read message 5 logout
     */
    public int getCommand() {
        return command;
    }

    /**
     * 1 is Register 2 is Login 3 send message 4 read message 5 logout
     */
    public void setCommand(int command) {
        this.command = command;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * HashMap Login, FullName Login is Key for searching, Full Name for display
     */
    public HashMap<String, String> getOnlineUser() {
        return onlineUsers;
    }

    /**
     * HashMap Login, FullName Login is Key for searching, Full Name for display
     */
    public void setOnlineUser(HashMap<String, String> onlineUser) {
        this.onlineUsers = onlineUser;
    }

    /**
     * HashMap Room id, Another User login
     */
    public HashMap<Integer, String> getUserRooms() {
        return userRooms;
    }

    /**
     * HashMap Room id, Another User login
     */
    public void setUserRooms(HashMap<Integer, String> userRooms) {
        this.userRooms = userRooms;
    }

    public HashMap<String, String> getUserlist() {
        return userlist;
    }

    public void setUserlist(HashMap<String, String> userlist) {
        this.userlist = userlist;
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

    public HashMap<Integer, Boolean> getIsRead() {
        return isRead;
    }

    public void setIsRead(HashMap<Integer, Boolean> isRead) {
        this.isRead = isRead;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    
}
