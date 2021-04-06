package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Value;
import com.google.gson.Gson;
import com.google.cloud.datastore.Key;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;


import org.apache.http.auth.UsernamePasswordCredentials;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    // create new datastore and keyfactory 
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException { // create
        
        String method = request.getParameter("_method").toString();

        // change method if it is a put request
        if (method.equals("PUT")){
          this.doPut(request, response);
          return;
        }

        // get form information
        String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
        String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
        String bio = Jsoup.clean(request.getParameter("bio"), Whitelist.none());
        String wishlist = Jsoup.clean(request.getParameter("wishlist"), Whitelist.none());
        String socialInfo = Jsoup.clean(request.getParameter("socialInfo"), Whitelist.none());
        String password = Jsoup.clean(request.getParameter("password"), Whitelist.none());
        String ACC_ID = email+password;

        // add kind users to keyfactory
        keyFactory.setKind("users");

        // take the user elements and create a new entity object for it 
        FullEntity userEntity =
          Entity.newBuilder(keyFactory.newKey(ACC_ID))
              .set("name", name)
              .set("email", email)
              .set("bio", bio)
              .set("wishlist", wishlist)
              .set("socialInfo", socialInfo)
              .build();
        
        // insert that new entity
        datastore.add(userEntity);
        response.sendRedirect("index.html");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException { // read
        
        String method = request.getParameter("_method").toString();

        // change method if it is a put request
        if (method.equals("DELETE")){
          this.doDelete(request, response);
          return;
        }

        //get for information
        String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
        String password = Jsoup.clean(request.getParameter("password"), Whitelist.none());
        String ACC_ID = email+password;
        Gson gson = new Gson();        

        //create a key to access the entity 
        keyFactory.setKind("users");
        Key userKey = keyFactory.newKey(ACC_ID);
        String user = datastore.get(userKey).getProperties().values().toString();

        //take entity information and turn it into json
        String json = gson.toJson(user);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException  { // update
        
        // get form information
        String name = Jsoup.clean(request.getParameter("name-update"), Whitelist.none());
        String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
        String bio = Jsoup.clean(request.getParameter("bio-update"), Whitelist.none());
        String wishlist = Jsoup.clean(request.getParameter("wishlist-update"), Whitelist.none());
        String socialInfo = Jsoup.clean(request.getParameter("socialInfo-update"), Whitelist.none());
        String password = Jsoup.clean(request.getParameter("password"), Whitelist.none());
        String ACC_ID = email+password;

        //access the original entity
        keyFactory.setKind("users");
        Key userKey = keyFactory.newKey(ACC_ID);
        Entity user = datastore.get(userKey);

        // take the user elements and create a new updated entity object for it 
        FullEntity userUpdatedEntity =
          Entity.newBuilder(user)
              .set("name", name)
              .set("bio", bio)
              .set("email", email)
              .set("wishlist", wishlist)
              .set("socialInfo", socialInfo)
              .build();

        //update the information in the original entity
        datastore.put(userUpdatedEntity);
        response.sendRedirect("index.html");
        return;
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException  { // delete

        //get for information
        String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
        String password = Jsoup.clean(request.getParameter("password"), Whitelist.none());
        String ACC_ID = email+password;

        //create a key to access the entity 
        keyFactory.setKind("users");
        Key userKey = keyFactory.newKey(ACC_ID);
        datastore.delete(userKey);
        return;
    }
}