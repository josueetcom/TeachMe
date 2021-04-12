package com.google.sps.data;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.sps.data.Chat;

import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.lang.String;

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
    this.participants = entity.getString("participants");
    // this.messages = entity.getProperty("messages");
  }

  public static Chat getUserChat(Datastore datastore, String chatid) {
    Key key = datastore.newKeyFactory().setKind("chat").newKey(chatid);

    try {
      Entity entity = datastore.get(key);
      Chat c = new Chat(entity);
      return c;
    } catch (Exception e) {
      return null;
    }
  }

  public static List<Chat> getUserChats(Datastore datastore, String participantid) {
    
    List<Chat> chats = new ArrayList<>();
    Query<Entity> chatQuery = Query.newEntityQueryBuilder().setKind("chat").build();
    QueryResults<Entity> chatEntities = datastore.run(chatQuery);

    while(chatEntities.hasNext()){
        Entity entity = chatEntities.next();
        List<String> participantList = entity.getList("participants");

        if (participantList.contains(participantid)){
            Chat chat = new Chat(entity);
            chats.add(chat);
        }
    }
    return chats;
  }

  public static List<Chat> getAllChats(Datastore datastore) {
    
    List<Chat> chats = new ArrayList<>();
    Query<Entity> chatQuery = Query.newEntityQueryBuilder().setKind("chat").build();
    QueryResults<Entity> chatEntities = datastore.run(chatQuery);

    while(chatEntities.hasNext()){
        Entity entity = chatEntities.next();
        Chat chat = new Chat(entity);
        chats.add(chat);
    }
    return chats;
  }

  public long getId(){
    return id;
  }

  public String getParticipants(){
    return participants;
  }

  public String getMessages(){
    return messages;
  }

  public static boolean chatExists(Datastore datastore, String participantId1, String participantId2){
    List<Chat> chats = Chat.getAllChats(datastore);
    ListIterator<Chat> chatsIterator = chats.listIterator();
    String participants;

    while(chatsIterator.hasNext()){
        Chat chat = chatsIterator.next();
        participants = chat.getParticipants();
        if(participants.contains(participantId1) || participants.contains(participantId2)){
            return true;
        }
    }

    return false;
  }
}
