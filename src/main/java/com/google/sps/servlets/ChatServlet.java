package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import main.java.com.google.sps.data.Chat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/chats")
public class ChatServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // OBJECTIVE: Create a chat between users
    // Step 1: Get the user and the partner ids
    String participant1 = Jsoup.clean(request.getParameter("userid"), Whitelist.none());
    String participant2 = Jsoup.clean(request.getParameter("userid"), Whitelist.none());
    String[] participants = {participant1, participant2};

    // Step 2: Check if the chat exists by seeing if any chats contain both users
        // Part 1: Query all chats
        // Part 2: Find if any chats contain both participant1 and participant2 ids       

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query = Query.newEntityQueryBuilder().setKind("Chat").build();
    QueryResults<Entity> results = datastore.run(query);

    List<Chat> chats = new ArrayList<>();
    while (results.hasNext()) {
      Entity entity = results.next();

      long id = entity.getKey().getId();

      Chat chat = new Chat(id);
      chats.add(chat);
    }

    Gson gson = new Gson();

    // Step 3: Create a new chat if it doesn't exist
        // String chatid = participant1 + "-" + participant2;
        // String[] participants = {participant1, participant2};
        // ArrayList<String> messages = new ArrayList<String>();
    // Query<Entity> query = Query.newEntityQueryBuilder();
    // QueryResults<Entity> results = datastore.run(query);
    // get form information

    response.sendRedirect("dashboard.html");
  }
}


