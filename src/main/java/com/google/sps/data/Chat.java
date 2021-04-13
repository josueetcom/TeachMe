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

/** An item on a todo list. */
public final class Chat {

  private long id;
  private List<String> participants;
  private List<String> messages;

  //Constructor 1
  Chat(long id, List<String> participants, List<String> messages) {
    this.id = id;
    this.participants = participants;
    this.messages = messages;
  }

  // //Constructor 2
  Chat(Entity entity) {
    this.id = (long) entity.getKey().getId();
    this.participants = entity.getList("participants");
    this.messages = entity.getList("messages");

  }

    public static Chat newChat(Datastore datastore, String participantId1, String participantId2) {
        boolean chat_exists = Chat.chatExists(datastore, participantId1, participantId2);

        if (chat_exists == true){
            return null;
        }

        KeyFactory keyFactory = datastore.newKeyFactory().setKind("chat");
        Key chatKey = datastore.allocateId(keyFactory.newKey());

        Entity chatEntity = Entity.newBuilder(chatKey)
            .set("participants", participantId1, participantId2)
            .set("messages", ListValue.of("Chat Start"))
            .build();

        datastore.add(chatEntity);
        System.out.println("Breakpoint");
        Chat chat = new Chat(chatEntity);

        return chat;
    }

    public static Chat getChatById(Datastore datastore, String chatId) {
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("chat");

        Query<Entity> query = Query.newEntityQueryBuilder()
            .setKind("chat")
            .setFilter(PropertyFilter.eq("__key__", keyFactory.newKey(chatId)))
            .build();

        QueryResults<Entity> chatResults = datastore.run(query);
        
        try {
            Entity chat = chatResults.next();
            Chat c = new Chat(chat);
            System.out.println("Chat found");
            return c;
        } catch (Exception e) {
            System.out.println("Chat not found");
            return null;
        }
    }

    public static List<Chat> getChatsByUser(Datastore datastore, String participantId) {
        
        List<Chat> chats = new ArrayList<Chat>();
        Query<Entity> chatQuery = Query.newEntityQueryBuilder().setKind("chat")
        .setFilter(CompositeFilter.and(
            PropertyFilter.eq("participants", participantId)))
        .build();
        QueryResults<Entity> chatEntities = datastore.run(chatQuery);

        while(chatEntities.hasNext()){
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

        while(chatEntities.hasNext()){
            Entity entity = chatEntities.next();
            Chat chat = new Chat(entity);
            chats.add(chat);
        }
        return chats;
    }

    public static boolean chatExists(Datastore datastore, String participantId1, String participantId2){
        Query<Entity> chatQuery = Query.newEntityQueryBuilder().setKind("chat")
            .setFilter(CompositeFilter.and(PropertyFilter.eq("participants", participantId1), PropertyFilter.eq("participants", participantId2)))
            .build();
        QueryResults<Entity> chatEntities = datastore.run(chatQuery);

        if (chatEntities.hasNext()){
            System.out.printf("-Chat Exists\n");
            
            while(chatEntities.hasNext()){
                Entity entity = chatEntities.next();
                System.out.println(entity.getList("participants"));
                System.out.println(entity.getList("messages"));
            }  

            return true;
        }
        else{
            System.out.printf("-Chat Doesn't Exist\n");
            
            while(chatEntities.hasNext()){
                Entity entity = chatEntities.next();
                System.out.println(entity.getList("participants"));
                System.out.println(entity.getList("messages"));
            }  

            return false;
        }        
    }

    
    public long getId(){
        return id;
    }

    public List<StringValue> getParticipants(){
        return participants;
    }

    public List<StringValue> getMessages(){
        return messages;
    }
}
