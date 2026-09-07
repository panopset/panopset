package com.panopset.flywheel;

public class FlywheelException extends Exception {
  
  public FlywheelException(String msg) {
    super(msg);
  }

  public FlywheelException(String msg, Exception ex) {
    super(msg, ex);
  }

  private static final long serialVersionUID = 1L;

}
