package com.sagar.iva;

public class Timer
{
  private long startTimeMillis;

  public void start()
  {
    this.startTimeMillis = System.currentTimeMillis();
  }
  long elapsedTimeMillis() {
    return System.currentTimeMillis() - this.startTimeMillis + 1L;
  }
  public float elapsedTimeSecs() {
    return (float)elapsedTimeMillis() / 1000.0F;
  }
  public float elapsedTimeMins() {
    return elapsedTimeSecs() / 60.0F;
  }
}

