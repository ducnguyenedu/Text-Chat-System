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
import model.ChatRoom;
import model.User;
import server.model.Client;
import server.model.Server;

/**
 *
 * @author duchi
 */
public class DAOUser extends DBContext {

    private Server server;
    private Client current;

    public DAOUser(Server server, Client current) {

        this.server = server;
        this.current = current;
    }

    public ExchangeMessage register(ExchangeMessage in) {
        ExchangeMessage out = new ExchangeMessage();
        out.setCommand(1);
        try {
            String sql = "DECLARE @apper INT = 1;\n"
                    + "DECLARE @login varchar(150) =  ? ;\n"
                    + " SELECT * FROM dbo.[User] WHERE [login] = @login \n"
                    + "set @apper = (Select COUNT([login]) FROM [dbo].[User]\n"
                    + "WHERE [login] = @login);\n"
                    + "\n"
                    + "IF @apper =0 \n"
                    + "BEGIN\n"
                    + "INSERT [dbo].[User] ([login], [fullName], [password]) VALUES (@login, ? , ? )\n"
                    + "\n"
                    + "\n"
                    + "DECLARE @n INT = 0;\n"
                    + "set @n = (Select COUNT([login]) from [dbo].[User])\n"
                    + "DECLARE @i INT = 1;\n"
                    + "DECLARE @alogin varchar(150) =''\n"
                    + "DECLARE @ylogin varchar(150) =''\n"
                    + "set @ylogin = (Select [login] from [dbo].[User] WHERE [id]=@n)\n"
                    + "DECLARE @roomid int =0;\n"
                    + "WHILE @i < @n\n"
                    + "BEGIN\n"
                    + "       set @alogin = (Select [login] from [dbo].[User] WHERE [id]=@i)\n"
                    + "    \n"
                    + "     INSERT INTO [dbo].[ChatRoom] DEFAULT\n"
                    + "     VALUES\n"
                    + "	 set @roomid = (Select COUNT([id]) from [dbo].ChatRoom);\n"
                    + "	 INSERT INTO [dbo].[Members]\n"
                    + "           ([room]\n"
                    + "           ,[user])\n"
                    + "     VALUES\n"
                    + "           (@roomid\n"
                    + "           ,@ylogin)\n"
                    + "\n"
                    + "		   INSERT INTO [dbo].[Members]\n"
                    + "           ([room]\n"
                    + "           ,[user])\n"
                    + "     VALUES\n"
                    + "           (@roomid\n"
                    + "           ,@alogin)\n"
                    + "\n"
                    + "    SET @i = @i + 1\n"
                    + "\n"
                    + "END\n"
                    + "END";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, in.getLogin());
            stm.setString(2, in.getFullName());
            stm.setString(3, in.getPassword());

            ResultSet rs = stm.executeQuery();
            Boolean Success = !rs.next();

            if (Success) {
                out.setIsSuccess(true);
                out.setServerMessage("You have successfully registered");
            } else {
                out.setIsSuccess(false);
                out.setServerMessage("This login name already appears in the data");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
            out.setIsSuccess(false);
            out.setServerMessage("Some error has occurred, please try again later ");
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return out;

    }

    public ExchangeMessage login(ExchangeMessage in) {
        ExchangeMessage out = new ExchangeMessage();
        out.setCommand(2);
        String login = in.getLogin();

        Boolean isSuccess = false;

        String pass = in.getPassword();

        User u = new User();
        HashMap<Integer, ChatRoom> room = new HashMap<>();
        HashMap<String, User> userList = new HashMap<>();

        HashMap<Integer, String> sendRoom = new HashMap<>();
        HashMap<String, String> sendUserList = new HashMap<>();

        HashMap<Integer, ArrayList<Integer>> sendroomdata = new HashMap<>();//room id, list id chat line
        HashMap<Integer, String> sendchatUser = new HashMap<>();//chat line id, user who chat this chat line
        HashMap<Integer, String> sendconntent = new HashMap<>();//chat line id, conntent of this chat line
        HashMap<Integer, Boolean> sendisRead = new HashMap<>();//chat line id, is this user read this chat line
        //CHECK USER
        //GET ROOM LIST
        //USER LIST
        try {
            String sql = "SELECT u.[login] as ylogin,u.[password] as ypass, u.[fullName] as yfullname , cr.id as crid , au.[login] as alogin ,  au.[fullName] as afullname ,\n"
                    + "isNULL(cl.id,-1) as clid, isNULL(cl.[user],'') as sender, isNULL(cl.content,'') as content, isNULL(cl.[user],'') as uchat, isNULL(ircl.isRead,0) as isRead, isNULL(ircl.[user],'') as uread\n"
                    + "FROM     dbo.[User] u INNER JOIN\n"
                    + "                  dbo.Members m ON u.[login] = m.[user] INNER JOIN dbo.[ChatRoom] cr on cr.id = m.[room]\n"
                    + "				  INNER JOIN dbo.Members am ON cr.id = am.[room]\n"
                    + "				  INNER JOIN dbo.[User] au ON au.[login] = am.[user]\n"
                    + "				  LEFT JOIN dbo.[ChatLine] cl ON cl.[room] = cr.[id]\n"
                    + "				  LEFT JOIN dbo.[isReadChatLines] ircl ON ircl.[chatline] = cl.[id]\n"
                    + "				  WHERE u.[login] = ? AND u.[login] != au.[login] \n"
                    + "				  Order by crid";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, login);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String ypass = rs.getString("ypass");
                if (!isSuccess) {
                    if (!ypass.equals(pass)) {
                        break;
                    }
                    isSuccess = true;
                    u.setLogin(login);
                    u.setFullName(rs.getString("yfullname"));
                    u.setPassword(pass);
                    current.setUser(u);
                    userList.put(login, u);

                }
                ChatRoom chatRoom = new ChatRoom();
                int crid = rs.getInt("crid");
                if (!room.containsKey(crid)) {
                    chatRoom.setId(crid);
                    room.put(crid, chatRoom);
                    chatRoom.getMembers().put(u.getLogin(), u);
                    u.getRooms().put(chatRoom.getId(), chatRoom);
                    ArrayList<Integer> crlids = new ArrayList<>();
                    sendroomdata.put(crid, crlids);
                } else {
                    chatRoom = room.get(crid);
                }

                User user = new User();
                String alogin = rs.getString("alogin");
                if (!userList.containsKey(alogin)) {
                    user.setLogin(alogin);
                    user.setFullName(rs.getString("afullname"));
                    chatRoom.getMembers().put(alogin, user);
                    user.getRooms().put(chatRoom.getId(), chatRoom);
                    sendRoom.put(crid, alogin);
                    sendUserList.put(alogin, user.getFullName());
                    userList.put(alogin, user);
                } else {
                    user = userList.get(alogin);
                }
                int clid = rs.getInt("clid");
                if (clid != -1) {
                    String ru = rs.getString("uread");
                    if (ru.equals(login)) {

                        ChatLine chatLine = new ChatLine();
                        chatLine.setId(clid);
                        chatLine.setRoom(chatRoom);
                        chatLine.setUser(userList.get(rs.getString("sender")));
                        chatLine.setContent(rs.getString("content"));

                        boolean isRead = rs.getBoolean("isRead");
                        u.getChatLines().put(chatLine, isRead);

                        sendroomdata.get(chatRoom.getId()).add(clid);
                        sendchatUser.put(clid, chatLine.getUser().getLogin());
                        sendconntent.put(clid, chatLine.getContent());

                        sendisRead.put(clid, isRead);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (isSuccess) {
            //GET ONLINE USER
            HashMap<String, String> map = new HashMap<>();
            for (Client client : server.getClients()) {
                if (client.getUser() != null) {
                    map.put(client.getUser().getLogin(), client.getUser().getFullName());
                }
            }

            out.setOnlineUser(map);

            out.setUserRooms(sendRoom);
            out.setUserlist(sendUserList);
            out.setRoomdata(sendroomdata);
            out.setChatUser(sendchatUser);
            out.setConntent(sendconntent);
            out.setIsRead(sendisRead);
        }
        out.setIsSuccess(isSuccess);
        out.setServerMessage(isSuccess ? "" : "Please check your username and password");

        return out;

    }

    public ExchangeMessage reloadNewRegister(String newLogin, User u) {
        ExchangeMessage out = new ExchangeMessage();
        out.setCommand(1);

        HashMap<String, User> members = new HashMap<>();
        members.put(u.getLogin(), u);

        HashMap<Integer, String> sendRoom = new HashMap<>();
        HashMap<String, String> sendUserList = new HashMap<>();

        //CHECK USER
        //GET ROOM LIST
        //USER LIST
        try {
            String sql = "SELECT cr.id as crid, m1.[user] as user1, m2.[user] as user2, u2.fullName as user2name\n"
                    + "                     FROM [dbo].[ChatRoom] cr\n"
                    + "                    INNER JOIN dbo.[Members] m1 on cr.id= m1.room \n"
                    + "                    INNER JOIN dbo.[Members] m2 on cr.id= m2.room\n"
                    + "					INNER JOIn dbo.[User] u2 on u2.[login] = m2.[user]\n"
                    + "                    WHERE (m1.[user] = ? AND m2.[user] = ?)";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, u.getLogin());
            stm.setString(2, newLogin);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                ChatRoom chatRoom = new ChatRoom();
                int crid = rs.getInt("crid");
                chatRoom.setId(crid);
                ArrayList<ChatLine> chatLines = new ArrayList<>();
                chatRoom.setChatLines(chatLines);
                chatRoom.setMembers(members);
                User user = new User();
                user.setLogin(rs.getString("user2"));
                user.setFullName(rs.getString("user2name"));
                user.getRooms().put(chatRoom.getId(), chatRoom);
                chatRoom.getMembers().put(user.getLogin(), user);
                u.getRooms().put(chatRoom.getId(), chatRoom);
                
                sendRoom.put(crid, user.getLogin());
                sendUserList.put(user.getLogin(), user.getFullName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        out.setUserRooms(sendRoom);
        out.setUserlist(sendUserList);

        return out;
    }

    public ExchangeMessage reloadNewLogin() {
        ExchangeMessage out = new ExchangeMessage();
        out.setCommand(2);
        HashMap<String, String> onlineUserList = new HashMap<>();

        onlineUserList.put(current.getUser().getLogin(), current.getUser().getFullName());
        out.setOnlineUser(onlineUserList);

        return out;

    }

    public ExchangeMessage reloadLogot() {
        ExchangeMessage out = new ExchangeMessage();
        out.setCommand(5);
        HashMap<String, String> logoutUserList = new HashMap<>();

        logoutUserList.put(current.getUser().getLogin(), current.getUser().getFullName());
        out.setOnlineUser(logoutUserList);

        return out;

    }

}
