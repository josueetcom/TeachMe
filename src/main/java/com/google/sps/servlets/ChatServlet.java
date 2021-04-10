package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String endpoint = request.getPathInfo();
    String chatid = request.getParameter("chatid");
    String userid = request.getParameter("userid");
    response.getWriter().println("Chat servlet working\n");
    response.getWriter().println("Endpoint: " + endpoint);
    response.getWriter().println("ChatID: " + chatid);
    response.getWriter().println("UserID: " + userid);
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    Gson gson = new Gson();
    //Get all chats that belong to the user to put in the menu
    if (userid != null){
        List<Chat> chats = Chat.getChats(datastore, userid);
        response.getWriter().println(gson.toJson(chats));
    }
    //Get a specific chat if the chat id is passed
    else if (chatid != null){
        Chat chat = Chat.getChat(datastore, userid);
        response.getWriter().println(gson.toJson(chat));
    }
    // OBJECTIVE: Create a chat between users
    // response.sendRedirect("dashboard.html");
  }
}


