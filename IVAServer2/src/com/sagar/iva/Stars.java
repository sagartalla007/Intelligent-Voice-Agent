package com.sagar.iva;

import java.util.ArrayList;

public class Stars extends ArrayList<String>
{
  public String star(int i)
  {
    if (i < size())
      return (String)get(i);
    return null;
  }
}
