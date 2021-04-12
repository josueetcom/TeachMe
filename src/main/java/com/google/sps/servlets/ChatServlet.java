package com.google.sps.servlets;
import com.google.sps.data.Chat;

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

// Try typing "/chats" or "/chats?userid=Elijah" or "/chats?chatid=someid"
// to see how the query parameters work for the servlet
@WebServlet(urlPatterns = {"/chats/*"})
public class ChatServlet extends HttpServlet {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String endpoint = request.getPathInfo();
        String chatid = request.getParameter("chatid");
        String userid = request.getParameter("userid");
        response.getWriter().println("Chat servlet working\n");
        response.getWriter().println("Endpoint: " + endpoint);
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        Gson gson = new Gson();
        
        //Get a specific chat if the chat id is passed
        if (chatid != null){
            Chat chat = Chat.getUserChat(datastore, chatid);
            response.getWriter().println("Chat " + chatid + " - Messages: " + chat.getMessages());
            response.getWriter().println(gson.toJson(chat));
        }
        //Get all chats that belong to the user to put in the menu
        else if (userid != null){
            List<Chat> chats = Chat.getUserChats(datastore, userid);
            response.getWriter().println("Number of chats belonging to " + userid + " = " + chats.size());
            response.getWriter().println(gson.toJson(chats));
        }
        else{
            List<Chat> chats = Chat.getAllChats(datastore);
            response.getWriter().println("Number of all chats = " + chats.size());
            response.getWriter().println(gson.toJson(chats));
        }
        // OBJECTIVE: Create a chat between users
        // response.sendRedirect("dashboard.html");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException { // create

      // Get the user id and the partner id that they're matching with
    //   String userid = Jsoup.clean(request.getParameter("id"), Whitelist.none());
      String userid = "115545090202496645724"; // Test id 1
    //   String partnerid = Jsoup.clean(request.getParameter("partnerid"), Whitelist.none());
      String partnerId = "104737820262548186234"; // Test id 2

      // Creating the participants list - its ordered by whichever id is greater
      String participants;
      if(userid.compareTo(partnerid) > 0){
          participants = userid + partnerid;
      } else {
          participants = partnerid + userid;
      }

      // Initializing the messages to empty string
      String messages = "";

      // Add the kind chat to keyfactory
      keyFactory.setKind("chat");

      // take the user elements and create a new entity object for it 
      FullEntity chatEntity =
        Entity.newBuilder(keyFactory.newKey())
            .set("participants", participants)
            .set("messages", messages)
            .build();
      
      // Check if the chat exists and insert the new entity
      
      if(Chat.chatExists(datastore, userid, partnerid) == false){
        datastore.add(chatEntity);
        response.sendRedirect("/dashboard.html");
      }
      else{
        response.getWriter().println("Chat exists. Users already matched");
      }

    }
  
}

