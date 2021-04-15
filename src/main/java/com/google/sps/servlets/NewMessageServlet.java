package com.google.sps.servlets;
import com.google.sps.data.Chat;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
// import com.google.cloud.datastore.Query;
// import com.google.cloud.datastore.QueryResults;
// import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.StringValue;
import com.google.gson.Gson;
import com.google.sps.data.Chat;

import java.io.IOException;
// import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

// import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.lang.String;
import com.google.sps.data.Chat;

// Try typing "/chats" or "/chats?userid=Elijah" or "/chats?chatid=someid"
// to see how the query parameters work for the servlet
@WebServlet("/message")
public class NewMessageServlet extends HttpServlet {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String message = Jsoup.clean(request.getParameter("message"), Whitelist.none());
        
        String chatId = request.getParameter("chatid");
        String userName = request.getParameter("username");
        
        List<StringValue> updatedMessages = Chat.newMessage(datastore, chatId, userName, message);
        
        response.getWriter().println(message + chatId + userName);
        response.getWriter().println(gson.toJson(updatedMessages));
    }  
}

