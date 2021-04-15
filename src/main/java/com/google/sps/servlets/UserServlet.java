package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreException;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import com.google.sps.data.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

  // create new datastore and keyfactory
  Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
  KeyFactory keyFactory = datastore.newKeyFactory();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { // create
    FullEntity userEntity = parseUserEntity(request);
    datastore.add(userEntity);
    response.sendRedirect("index.html");
  }

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // update
    FullEntity userEntity = parseUserEntity(request);
    datastore.put(userEntity);
    response.sendRedirect("index.html");
    return;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { // read
    boolean isID = request.getParameter("id") != null;
    String id = "";

    // check to see if there is an id
    if(isID){
      id = Jsoup.clean(request.getParameter("id"), Whitelist.none());
    }

    Query<Entity> query = Query.newEntityQueryBuilder().setKind("user").build();
    keyFactory.setKind("user");

    // If there is an id parameter, then return only that user
    if (isID) {
      query = Query.newEntityQueryBuilder().setKind("user")
          .setFilter(PropertyFilter.gt("__key__", keyFactory.newKey(id))).build();
    }

    QueryResults<Entity> results = datastore.run(query);

    // Turn the user info into an arraylist of user objects
    List<User> users = new ArrayList<>();
    while (results.hasNext()) {
      Entity entity = results.next();

      String imgURL = entity.getString("imgURL");
      String name = entity.getString("name");
      String email = entity.getString("email");
      List<String> wishlist = entity.getList("wishlist").stream().map(val -> ((StringValue) val).get())
          .collect(Collectors.toList());
      List<String> teachlist = entity.getList("teachlist").stream().map(val -> ((StringValue) val).get())
          .collect(Collectors.toList());
      ;

      User user = new User(imgURL, name, email, wishlist, teachlist);
      users.add(user);
    }

    // return the users as json
    Gson gson = new Gson();
    String json = gson.toJson(users);
    response.setContentType("application/json;");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(json);

  }

  public FullEntity parseUserEntity(HttpServletRequest request) {
    // get form information
    String imgURL = Jsoup.clean(request.getParameter("imgURL"), Whitelist.none());
    String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
    String[] wishlist = Jsoup.clean(request.getParameter("wishlist"), Whitelist.none()).split(",");
    String[] teachlist = Jsoup.clean(request.getParameter("teachlist"), Whitelist.none()).split(",");
    String id = Jsoup.clean(request.getParameter("id"), Whitelist.none());

    // add kind users to keyfactory
    keyFactory.setKind("user");

    // take the user elements and create a new entity object for it
    FullEntity userEntity = Entity.newBuilder(keyFactory.newKey(id)).set("imgURL", imgURL).set("name", name)
        .set("email", email)
        .set("wishlist", Arrays.asList(wishlist).stream().map(StringValue::of).collect(Collectors.toList()))
        .set("teachlist", Arrays.asList(teachlist).stream().map(StringValue::of).collect(Collectors.toList())).build();

    return userEntity;
  }

}