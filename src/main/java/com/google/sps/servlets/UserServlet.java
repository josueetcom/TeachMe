package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Key;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException { // create

        // get form information
        String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
        String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
        String bio = Jsoup.clean(request.getParameter("bio"), Whitelist.none());
        String wishlist = Jsoup.clean(request.getParameter("wishlist"), Whitelist.none());
        String socialInfo = Jsoup.clean(request.getParameter("socialInfo"), Whitelist.none());

        // create new datastore and keyfactory 
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory();

        // add kind users to keyfactory
        keyFactory.setKind("users");

        // take the user elements and create a new entity object for it 
        FullEntity userEntity =
          Entity.newBuilder(keyFactory.newKey())
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

    /*@Override
    public void doGet() { // read


    }

    @Override
    public void doPut() { // update


    }

    @Override
    public void doDelete() { // delete

    }*/
}