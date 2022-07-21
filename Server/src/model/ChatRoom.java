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
public class ChatRoom {

    private int id;
   
    private HashMap<String, User> members = new HashMap<>();
    private ArrayList<ChatLine> chatLines = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, User> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, User> members) {
        this.members = members;
    }

    

    public ArrayList<ChatLine> getChatLines() {
        return chatLines;
    }

    public void setChatLines(ArrayList<ChatLine> chatLines) {
        this.chatLines = chatLines;
    }

}
