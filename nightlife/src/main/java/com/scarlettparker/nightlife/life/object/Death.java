package com.scarlettparker.nightlife.life.object;

public class Death {
  private long time;
  private String cause;

  public Death(long time, String cause) {
    this.time = time;
    this.cause = cause;
  }

  public long getTime() {
    return time;
  }

  public String getCause() {
    return cause;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public void setCause(String cause) {
    this.cause = cause;
  }
}