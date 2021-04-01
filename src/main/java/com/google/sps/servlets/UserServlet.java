package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/user")
public class UserServlet extends HttpServlet{

    @Override
    public void doPost(){ // create

        // get form information
        String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
        String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
        String bio = Jsoup.clean(request.getParameter("bio"), Whitelist.none());
        String wishlist = Jsoup.clean(request.getParameter("wishlist"), Whitelist.none());
        String socialInfo = Jsoup.clean(request.getParameter("socialInfo"), Whitelist.none());
    }

    @Override
    public void doGet(){ // read


    }

    @Override
    public void doPut(){ // update


    }

    @Override
    public void doDelete(){ // delete

    }
}