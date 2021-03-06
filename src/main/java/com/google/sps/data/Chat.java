package com.google.sps.data;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.StringValue;
import com.google.gson.Gson;
import com.google.sps.data.Chat;

import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/** An item on a todo list. */
public final class Chat {

    private long id;
    private List<String> participants;
    private List<String> messages;

    // Constructor 1
    public Chat(long id, List<String> participants, List<String> messages) {
        this.id = id;
        this.participants = participants;
        this.messages = messages;
    }

    // //Constructor 2
    public Chat(Entity entity) {
        this.id = (long) entity.getKey().getId();
        this.participants = entity.getList("participants").stream().map(val -> ((StringValue) val).get())
                .collect(Collectors.toList());
        this.messages = entity.getList("messages").stream().map(val -> ((StringValue) val).get()).collect(Collectors.toList());
    }

    public static Chat newChat(Datastore datastore, String participantId1, String participantId2) {
        Chat chat = Chat.chatExists(datastore, participantId1, participantId2);

        if (chat == null){
            KeyFactory keyFactory = datastore.newKeyFactory().setKind("chat");
            Key chatKey = datastore.allocateId(keyFactory.newKey());

            Entity chatEntity = Entity.newBuilder(chatKey).set("participants", participantId1, participantId2)
                    .set("messages", ListValue.of("Chat Start")).build();

            datastore.add(chatEntity);
            System.out.println("Breakpoint");
            chat = new Chat(chatEntity);
        }

        return chat;
    }

    public static Chat getChatById(Datastore datastore, String chatId) {
        Key chatKey = datastore.newKeyFactory().setKind("chat").newKey(Long.parseLong(chatId));
        Entity chatEntity = datastore.get(chatKey);

        Chat chat = new Chat(chatEntity);
        System.out.println("Chat found");
        return chat;
    }

    public static List<Chat> getChatsByUser(Datastore datastore, String participantId) {

        List<Chat> chats = new ArrayList<Chat>();
        Query<Entity> chatQuery = Query.newEntityQueryBuilder().setKind("chat")
                .setFilter(CompositeFilter.and(PropertyFilter.eq("participants", participantId))).build();
        QueryResults<Entity> chatEntities = datastore.run(chatQuery);

        while (chatEntities.hasNext()) {
            Entity entity = chatEntities.next();
            Chat chat = new Chat(entity);
            chats.add(chat);
        }
        return chats;
    }

    public static List<Chat> getAllChats(Datastore datastore) {

        List<Chat> chats = new ArrayList<>();

        Query<Entity> query = Query.newEntityQueryBuilder().setKind("chat").build();
        QueryResults<Entity> chatEntities = datastore.run(query);

        while (chatEntities.hasNext()) {
            Entity entity = chatEntities.next();
            Chat chat = new Chat(entity);
            chats.add(chat);
        }
        return chats;
    }

    public static Chat chatExists(Datastore datastore, String participantId1, String participantId2) {
        Query<Entity> chatQuery = Query.newEntityQueryBuilder().setKind("chat")
                .setFilter(CompositeFilter.and(PropertyFilter.eq("participants", participantId1),
                        PropertyFilter.eq("participants", participantId2)))
                .build();
        QueryResults<Entity> chatEntities = datastore.run(chatQuery);

        if (chatEntities.hasNext()) {
            System.out.printf("-Chat Exists\n");
            Entity entity = chatEntities.next();
            Chat chat = new Chat(entity);
            return chat;
        } else {
            System.out.printf("-Chat Doesn't Exist\n");
            return null;
        }
    }

    public static Chat newMessage(Datastore datastore, String chatId, String userId, String message) {
        Key chatKey = datastore.newKeyFactory().setKind("chat").newKey(Long.parseLong(chatId));
        Entity chatEntity = datastore.get(chatKey);

        // Note: So far what I've found online says you cannot add to a list property 
        // without getting the entire list and adding the new item to it, then replace 
        // the old list property on the entity. There must be a better way but Im stumped right now
        List<String> messages = chatEntity.getList("messages").stream().map(val -> ((StringValue) val).get()).collect(Collectors.toList());
        messages.add(message);
        // Now here I need to find out how to make a List<Value<?>> with all 
        // the strings like above but in revers
        
        // Entity updatedChatEntity = Entity.newBuilder(chatEntity).set("messages", message).build();
        // Chat chat = new Chat(updatedChatEntity);
        Chat chat = new Chat(chatEntity);

        return chat;
    }

    public long getId() {
        return id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public List<String> getMessages() {
        return messages;
    }
}
