package com.google.sps.data;

public final class User {

  private final String name;
  private final String email;
  private final String wishlist;
  private final String teachlist;

  public User(String name, String email, String wishlist, String teachlist) {
    this.name = name;
    this.email = email;
    this.wishlist = wishlist;
    this.teachlist = teachlist;
  }

  public String toString(){
    return "name: "+name+", email: "+email+ ", wishlist: " + wishlist + ", teachlist: " + teachlist;
  }

  public String getName(){
    return this.name;
  }

  /*
  public String getWishes{}

  public String getTeaches{}
   */
}