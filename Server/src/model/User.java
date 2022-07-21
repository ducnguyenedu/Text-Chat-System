/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author duchi
 */
public class User {

  
    private String fullName;
    private String login;//Key
    private String password;
    private HashMap<Integer,ChatRoom> rooms = new HashMap<>();
    private HashMap<ChatLine, Boolean> isReadChatLines = new HashMap<>();

    public User() {
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

    public HashMap<Integer, ChatRoom> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<Integer, ChatRoom> rooms) {
        this.rooms = rooms;
    }

    

    public HashMap<ChatLine, Boolean> getChatLines() {
        return isReadChatLines;
    }

    public void setChatLines(HashMap<ChatLine, Boolean> chatLines) {
        this.isReadChatLines = chatLines;
    }

   
}
