/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import comunication_model.ExchangeMessage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ChatLine;
import model.User;
import server.model.Client;
import server.model.Server;

/**
 *
 * @author duchi
 */
public class DAOChatRoom extends DBContext {

    private Server server;
    private Client current;

    public DAOChatRoom(Server server, Client current) {

        this.server = server;
        this.current = current;
    }

    public ExchangeMessage insertMessage(ExchangeMessage in) {
        ExchangeMessage out = new ExchangeMessage();
        String sender = current.getUser().getLogin();
        int roomid = in.getRoom();
        String receiver = "";
        HashMap<String, User> members = current.getUser().getRooms().get(roomid).getMembers();
        for (Map.Entry<String, User> entry : members.entrySet()) {
            String key = entry.getKey();
            User value = entry.getValue();
            if (!value.getLogin().equals(sender)) {
                receiver = value.getLogin();
            }
        }
        out.setCommand(3);
        out.setIsSuccess(true);
        ChatLine cl = new ChatLine();
        try {
            String sql = "SELECT (COUNT(id)+1) as chatid FROM dbo.[ChatLine]\n"
                    + "DECLARE @sender varchar(150) = ? \n"
                    + "DECLARE @receiver varchar(150) = ? \n"
                    + "DECLARE @content varchar(150) = ? \n"
                    + "DECLARE @roomid int = ? \n"
                    + "\n"
                    + "INSERT INTO [dbo].[ChatLine]\n"
                    + "           ([user]\n"
                    + "           ,[room]\n"
                    + "           ,[content]\n"
                    + "           ,[time])\n"
                    + "     VALUES\n"
                    + "           (@sender\n"
                    + "           ,@roomid\n"
                    + "           ,@content\n"
                    + "           ,GETDATE())\n"
                    + "DECLARE @id int =0\n"
                    + "SET @id= (Select COUNT([id]) from [dbo].ChatLine)\n"
                    + "INSERT INTO [dbo].[isReadChatLines]\n"
                    + "           ([chatline]\n"
                    + "           ,[user]\n"
                    + "           ,[isRead])\n"
                    + "     VALUES\n"
                    + "           (@id\n"
                    + "           ,@sender\n"
                    + "           ,1)\n"
                    + "INSERT INTO [dbo].[isReadChatLines]\n"
                    + "           ([chatline]\n"
                    + "           ,[user]\n"
                    + "           ,[isRead])\n"
                    + "     VALUES\n"
                    + "           (@id\n"
                    + "           ,@receiver\n"
                    + "           ,0)\n";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, sender);
            stm.setString(2, receiver);
            stm.setString(3, in.getMessageContent());
            stm.setInt(4, roomid);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                cl.setId(rs.getInt("chatid"));
                cl.setUser(current.getUser());
                cl.setRoom(current.getUser().getRooms().get(roomid));
                cl.setContent(in.getMessageContent());
                current.getUser().getChatLines().put(cl, true);
                current.getUser().getRooms().get(roomid).getChatLines().add(cl);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
            out.setIsSuccess(false);
            out.setServerMessage("Some error has occurred, please try again later ");
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DAOChatRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        HashMap<Integer, ArrayList<Integer>> roomdata = new HashMap<>();
        ArrayList<Integer> id = new ArrayList<>();
        id.add(cl.getId());
        roomdata.put(roomid, id);
        HashMap<Integer, String> chatUser = new HashMap<>();//chat line id, user who chat this chat line
        chatUser.put(cl.getId(), cl.getUser().getLogin());
        HashMap<Integer, String> conntent = new HashMap<>();//chat line id, conntent of this chat line
        conntent.put(cl.getId(), cl.getContent());
        HashMap<Integer, Boolean> isRead = new HashMap<>();//chat line id, is this user read this chat line
        isRead.put(cl.getId(), current.getUser().getChatLines().get(cl));

        out.setRoom(roomid);
        out.setRoomdata(roomdata);
        out.setChatUser(chatUser);
        out.setConntent(conntent);
        out.setIsRead(isRead);
        return out;
    }

    public ExchangeMessage reloadMessReceiver(ExchangeMessage insertMessage, User user) {
        ExchangeMessage out = new ExchangeMessage();
        ChatLine cl = new ChatLine();
        int id = insertMessage.getRoomdata().get(insertMessage.getRoom()).get(0);
        cl.setId(id);
        out.setCommand(3);
        out.setIsSuccess(true);
        for (Map.Entry<String, User> entry : user.getRooms().get(insertMessage.getRoom()).getMembers().entrySet()) {
            String key = entry.getKey();
            User value = entry.getValue();
            if (!value.getLogin().equals(user.getLogin())) {
                cl.setUser(value);
            }
        }
        cl.setRoom(user.getRooms().get(insertMessage.getRoom()));
        cl.setContent(insertMessage.getConntent().get(id));
        user.getChatLines().put(cl, false);
        user.getRooms().get(insertMessage.getRoom()).getChatLines().add(cl);

        HashMap<Integer, ArrayList<Integer>> roomdata = new HashMap<>();
        ArrayList<Integer> idcl = new ArrayList<>();
        idcl.add(cl.getId());
        roomdata.put(insertMessage.getRoom(), idcl);
        HashMap<Integer, String> chatUser = new HashMap<>();//chat line id, user who chat this chat line
        chatUser.put(cl.getId(), cl.getUser().getLogin());
        HashMap<Integer, String> conntent = new HashMap<>();//chat line id, conntent of this chat line
        conntent.put(cl.getId(), cl.getContent());
        HashMap<Integer, Boolean> isRead = new HashMap<>();//chat line id, is this user read this chat line
        isRead.put(cl.getId(), user.getChatLines().get(cl));

        out.setRoom(insertMessage.getRoom());
        out.setRoomdata(roomdata);
        out.setChatUser(chatUser);
        out.setConntent(conntent);
        out.setIsRead(isRead);
        return out;
    }

    public ExchangeMessage readChat(ExchangeMessage in) {
        in.getIsRead();

        ExchangeMessage out = new ExchangeMessage();
        out.setCommand(4);
        out.setIsSuccess(true);
        try {
            String sql = "UPDATE [dbo].[isReadChatLines]\n"
                    + "   SET [isRead] = 1\n"
                    + " WHERE [user] = ? AND ([chatline] = ";
            int moreThanOne = 0;
            for (Map.Entry<Integer, Boolean> entry : in.getIsRead().entrySet()) {
                if (moreThanOne > 0) {
                    sql = sql + " OR [chatline] = ";
                }
                Integer key = entry.getKey();
                Boolean value = entry.getValue();
                sql = sql + key;
                moreThanOne++;
            }
            sql = sql + " )";
            
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, current.getUser().getLogin());
            stm.executeUpdate();

            for (Map.Entry<ChatLine, Boolean> entry : current.getUser().getChatLines().entrySet()) {
                ChatLine key = entry.getKey();
                Boolean value = entry.getValue();
                if (in.getIsRead().containsKey(key.getId())) {
                    entry.setValue(true);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOChatRoom.class.getName()).log(Level.SEVERE, null, ex);
            out.setIsSuccess(false);
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DAOChatRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return out;
    }
}
