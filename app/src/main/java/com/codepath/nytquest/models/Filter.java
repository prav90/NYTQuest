package com.codepath.nytquest.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by rpraveen on 10/23/16.
 */
@Parcel
public class Filter {
  public String beginDate;
  public String endDate;
  public String searchQuery;
  public boolean sortByNewest;
  public ArrayList<String> newsDesk;
  public int page;

  public Filter() {
    sortByNewest = true;
    newsDesk = new ArrayList<>();
  }
}
