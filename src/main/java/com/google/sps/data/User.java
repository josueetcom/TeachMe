package com.google.sps.data;

public final class User {

  private final String name;
  private final String email;
  private final String wishlist;

  public User(String name, String email, String wishlist) {
    this.name = name;
    this.email = email;
    this.wishlist = wishlist;
  }

  public String toString(){
    return "name: "+name+", email: "+email+ ", wishlist: " + wishlist;
  }
}

