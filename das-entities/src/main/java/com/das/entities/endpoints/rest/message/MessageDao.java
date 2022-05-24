package com.das.entities.endpoints.rest.message;

import com.das.entities.commons.enums.MessageType;
import com.das.entities.core.dao.Dao;
import com.das.entities.endpoints.rest.message.model.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessageDao extends Dao<Message, Message> {
    @Override
    public Message make(ResultSet resultSet) throws SQLException {
        Message message = new Message();
        if (this.hasColumn(resultSet, "id")) {
            message.setId(resultSet.getLong("id"));
        }
        if (this.hasColumn(resultSet, "assistanceId")) {
            message.setAssistanceId(resultSet.getLong("assistanceId"));
        }
        if (this.hasColumn(resultSet, "userId")) {
            message.setUserId(resultSet.getLong("userId"));
        }
        if (this.hasColumn(resultSet, "entityId")) {
            message.setEntityId(resultSet.getLong("entityId"));
        }
        if (this.hasColumn(resultSet, "payload")) {
            message.setPayload(resultSet.getString("payload"));
        }
        if (this.hasColumn(resultSet, "attachment")) {
            message.setAttachment(resultSet.getString("attachment"));
        }
        if (this.hasColumn(resultSet, "messageType")) {
            message.setMessageType(MessageType.valueOf(resultSet.getString("messageType").toUpperCase()));
        }
        if (this.hasColumn(resultSet, "timestamp")) {
            message.setTimestamp(resultSet.getDate("timestamp"));
        }
        if (this.hasColumn(resultSet, "isFromOwner")) {
            message.setIsFromUser(resultSet.getBoolean("isFromOwner"));
        }
        return message;
    }

    @Override
    public Message insert(Message message) throws SQLException {
        try {
            this.connect();

            this.setProcedure("InsertMessage(?,?,?,?,?,?)");
            this.setParameter(1, message.getAssistanceId());
            this.setParameter(2, message.getMessageType().getValue());
            this.setParameter(3, message.getPayload() != null ? message.getPayload() : "");
            this.setParameter(4, message.getAttachment() != null ? message.getAttachment() : "");
            this.setParameter(5, message.getIsSynchronized());
            this.setParameter(6, message.getIsFromUser());

            if (this.executeUpdate() == 0) {
                throw new SQLException("No rows were updated");
            }

            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public Message update(Message message) throws SQLException {
        return null;
    }

    @Override
    public Message delete(Message param) throws SQLException {
        return null;
    }

    @Override
    public List<Message> select(Message message) throws SQLException {
        this.connect();
        this.setProcedure("SelectMessage(?)");
        this.setParameter(1, message.getAssistanceId());
        List<Message> messageList = this.executeQuery();
        this.close();
        return messageList;
    }

    @Override
    public boolean valid(Message param) throws SQLException {
        return false;
    }

    public List<Message> markAsSynchronizedAndRetrieve() throws SQLException {
        this.connect();
        this.setProcedure("MarkAsSynchronizedAndRetrieve");
        List<Message> messageList = this.executeQuery();
        this.close();
        return messageList;
    }
}
