package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreException;
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

    // create new datastore and keyfactory 
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException { // create

      String button_type = request.getParameter("submission").toString();
      if(button_type == "update"){
        this.doPut(request, response);
        return;
      }

      // get form information
      String NOTHING = Jsoup.clean(request.getParameter("NOTHING"), Whitelist.none());
      String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
      String wishlist = Jsoup.clean(request.getParameter("wishlist"), Whitelist.none());
      String teachlist = Jsoup.clean(request.getParameter("teachlist"), Whitelist.none());
      String id = Jsoup.clean(request.getParameter("id"), Whitelist.none());

      // add kind users to keyfactory
      keyFactory.setKind("users");

      // take the user elements and create a new entity object for it 
      FullEntity userEntity =
        Entity.newBuilder(keyFactory.newKey(id))
            .set("email", email)
            .set("wishlist", wishlist)
            .set("teachlist", teachlist)
            .build();
      
      // insert that new entity
      try{
        datastore.add(userEntity);
      }
      catch(Exception DatastoreException){
        this.doPut(request, response);
        return;
      }
      response.sendRedirect("index.html");
    }


    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException  { // update
        
      // get form information
      String NOTHING = Jsoup.clean(request.getParameter("NOTHING"), Whitelist.none());
      String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
      String wishlist = Jsoup.clean(request.getParameter("wishlist"), Whitelist.none());
      String teachlist = Jsoup.clean(request.getParameter("teachlist"), Whitelist.none());
      String id = Jsoup.clean(request.getParameter("id"), Whitelist.none());

      //access the original entity
      keyFactory.setKind("users");
      Key userKey = keyFactory.newKey(id);
      Entity user = datastore.get(userKey);

      if(wishlist.equals(NOTHING)){
          wishlist = user.getString("wishlist");
      }
      if(teachlist.equals(NOTHING)){
          teachlist = user.getString("teachlist");
      }

      // take the user elements and create a new updated entity object for it 
      FullEntity userUpdatedEntity =
        Entity.newBuilder(user)
            .set("email", email)
            .set("wishlist", wishlist)
            .set("teachlist", teachlist)
            .build();

      //update the information in the original entity
      datastore.put(userUpdatedEntity);
      response.sendRedirect("index.html");
      return;
    }

    /*@Override
    public void doGet() { // read


    }

    @Override
    public void doDelete() { // delete

    }*/
}