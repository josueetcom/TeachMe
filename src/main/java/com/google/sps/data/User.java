package com.google.sps.data;

import java.util.List;

public final class User {

  private final String imgURL;
  private final String name;
  private final String email;
  private final List<String> wishlist;
  private final List<String> teachlist;

  public User(String imgURL, String name, String email, List<String> wishlist, List<String> teachlist) {
    this.imgURL = imgURL;
    this.name = name;
    this.email = email;
    this.wishlist = wishlist;
    this.teachlist = teachlist;
  }

  // public String toString(){
  // return "name: "+name+", email: "+email+ ", wishlist: " + wishlist + ",
  // teachlist: " + teachlist;
  // }

  public String getName() {
    return this.name;
  }

  /*
   * public String getWishes{}
   * 
   * public String getTeaches{}
   */
}