// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package main.java.com.google.sps.data;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.util.*;
import java.lang.String;
import java.lang.Long;

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
  // Chat(Entity entity) {
  //   this.id = (long) entity.getKey.getId();
  //   this.participants = (String) entity.getProperty("participants");
  //   this.messages = entity.getProperty("messages");
  // }

  // public static List<Chat> getChat(DatastoreService datastore, long chatid) {
  //   Key key = KeyFactory.createKey("Chat", chatid);

  //   try {
  //     Entity entity = datastore.get(key);
  //     Chat c = new Chat(entity);
  //     return c;
  //   } catch (EntityNotFoundException e) {
  //     return null;
  //   }
  // }

  // public static List<Chat> getChats(DatastoreService datastore, long participantid) {
  //   List<Chat> chats = new ArrayList<>();
  //   Query chatQuery = new Query("Chat");
  //   PreparedQuery chatEntities = datastore.prepare(chatQuery);

  //   // Iterate through the chats
  //   for (Entity entity : chatEntities.asIterable()) {
  //     long chatid = entity.getKey.getId();
  //     List<String> participantList = Arrays.asList(entity.getProperty("participants").split(","));

  //     if (participantList.contains(Long.toString(participantid))){
  //       Chat chat = new Chat(entity);
  //       chats.add(chat);
  //     }
  //   }
  //   return chats;
  // }


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