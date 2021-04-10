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
  private String participants;//A comma separated string of participants
  private List<String> messages;

  //Constructor 1
  Chat(long id, String participants, List<String> messages) {
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

  public static Chat getChat(Datastore datastore, String chatid) {
    Key key = datastore.newKeyFactory().setKind("Chat").newKey(chatid);

    try {
      Entity entity = datastore.get(key);
      Chat c = new Chat(entity);
      return c;
    } catch (Exception e) {
      return null;
    }
  }

  public static List<Chat> getChats(Datastore datastore, String participantid) {
    
    List<Chat> chats = new ArrayList<>();
    Query<Entity> chatQuery = Query
    .newEntityQueryBuilder()
    .setKind("Chat")
    .build();
    QueryResults<Entity> chatEntities = datastore.run(chatQuery);

    // Iterate through the chats
    // for (Entity entity : chatEntities.asIterable()) {
    //   long chatid = entity.getKey.getId();
    //   List<String> participantList = Arrays.asList(entity.getProperty("participants").split(","));

    //   if (participantList.contains(participantid)){
    //     Chat chat = new Chat(entity);
    //     chats.add(chat);
    //   }
    // }
    return chats;
  }


  long getId(){
    return id;
  }

  String getParticipants(){
    return participants;
  }

  List<String> getMessages(){
    return messages;
  }
}