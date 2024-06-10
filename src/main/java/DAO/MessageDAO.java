package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    public Message insertMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        if (message.getMessage_text().length() == 0) {
            return null;
        }
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet results = ps.getGeneratedKeys();
            if (results.next()) {
                int generated_id = results.getInt(1);
                return new Message(generated_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> allMessages = new ArrayList<Message>();
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                allMessages.add(new Message(results.getInt("message_id"), results.getInt("posted_by"), results.getString("message_text"), results.getLong("time_posted_epoch")));
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return allMessages;
    }

    public Message getMessageById(int id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return new Message(results.getInt("message_id"), results.getInt("posted_by"), results.getString("message_text"), results.getLong("time_posted_epoch"));
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message deleteMessageById(int id) {
        Message message = getMessageById(id);
        Connection conn = ConnectionUtil.getConnection();
        if (message == null) {
            return null;
        }
        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return message;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message updateMessageById(int id, Message message) {
        Connection conn = ConnectionUtil.getConnection();
        if (message.getMessage_text().length() > 255 || message.getMessage_text().length() == 0) {
            return null;
        }

        Message returnMessage = getMessageById(id);

        try {
            String sql = "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, returnMessage.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, returnMessage.getTime_posted_epoch());
            ps.setInt(4, id);

            int result = ps.executeUpdate();

            if (result != 0) {
                returnMessage = new Message(id, returnMessage.getPosted_by(), message.getMessage_text(), returnMessage.getTime_posted_epoch());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return returnMessage;
    }

    public List<Message> getAllMessagesByUserId(int userid) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            List<Message> allMessages = new ArrayList<Message>();
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userid);

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                allMessages.add(new Message(results.getInt("message_id"), results.getInt("posted_by"), results.getString("message_text"), results.getLong("time_posted_epoch")));
            }

            return allMessages;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
