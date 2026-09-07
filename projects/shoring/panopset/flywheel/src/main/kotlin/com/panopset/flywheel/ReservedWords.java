package com.panopset.flywheel;

/**
 * Flywheel reserved words, all in the com.panopset namespace.
 */
public final class ReservedWords {

  /**
   * Object key for the flywheel object. Used in CommandExecute.
   */
  public static final String FLYWHEEL = "com.panopset.flywheel";
  /**
   * Object key for the script object. Used in CommandExecute.
   */
  public static final String SCRIPT = "com.panopset.flywheel.script";
  /**
   * Will contain the target directory. Used in CommandVariable.
   */
  public static final String TARGET = "com.panopset.flywheel.target";
  /**
   * Will contain the top file name. Used in CommandVariable.
   */
  public static final String FILE = "com.panopset.flywheel.file";
  public static final String OUTFILE = "com.panopset.flywheel.outfile";
  /**
   * Will contain the currently executing template. Used in CommandVariable.
   */
  public static final String TEMPLATE = "com.panopset.flywheel.template";
  /**
   * Used to specify the tokens list lines are separated with, if any. Used
   * in CommandList.
   */
  public static final String TOKENS = "com.panopset.flywheel.tokens";
  /**
   * Comma separated list of integers that specify column splits in a list. Used
   * in CommandList.
   */
  public static final String SPLITS = "com.panopset.flywheel.splits";
  /**
   * MAC Address
   */
  public static final String MACAddress = "com.panopset.MACAddress";
  /**
   * Prevent instantiation.
   */
  private ReservedWords() {
  }
}
