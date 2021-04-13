package com.google.sps.servlets;
import com.google.sps.data.Chat;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
// import com.google.cloud.datastore.Query;
// import com.google.cloud.datastore.QueryResults;
// import com.google.cloud.datastore.StructuredQuery.OrderBy;
// import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Value;
import com.google.gson.Gson;
import com.google.sps.data.Chat;

import java.io.IOException;
// import java.util.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// import org.jsoup.Jsoup;
// import org.jsoup.safety.Whitelist;
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
       // Test participant ids
        String participantId1 = "1155450902024"; // Test id 1
        String participantId2 = "104737820262548186234"; // Test id 2

        response.getWriter().println("Chat servlet working\n");
        response.getWriter().println("Endpoint: " + endpoint);
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        Gson gson = new Gson();
        
        //Get a specific chat if the chat id is passed
        if (chatid != null){
            Chat chat = Chat.getChatById(datastore, chatid);
            response.getWriter().println("Chat " + chatid + " - Messages: ");
            response.getWriter().println(gson.toJson(chat));
        }
        //Get all chats that belong to the user to put in the menu
        else if (userid != null){
            List<Chat> chats = Chat.getChatsByUser(datastore, userid);
            response.getWriter().println("Number of chats belonging to " + userid + " = " + chats.size());
            response.getWriter().println(gson.toJson(chats));
        }
        else if(participantId1 != null && participantId2 != null){
            Chat chat = Chat.newChat(datastore, participantId1, participantId2);
            response.getWriter().println("Chat between participants - " + gson.toJson(chat));
        }
        else{
            List<Chat> chats = Chat.getAllChats(datastore);
            response.getWriter().println("Number of all chats = " + chats.size());
            response.getWriter().println(gson.toJson(chats));
        }
        // OBJECTIVE: Create a chat between users
        // response.sendRedirect("dashboard.html");
    }  
}

