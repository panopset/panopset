package com.panopset.flywheel;

/**
 * Flywheel control flags.
 */
public final class Control {

  private boolean stopped = false;
  private String stopReason = "";

  /**
   * @return true if replacements are suppressed.
   */
  public boolean isReplacementsSuppressed() {
    return replacementsSuppressed;
  }

  /**
   * Used to temporarily suppress replacements.
   *
   * @param value Replacements suppressed.
   */
  public void setReplacementsSuppressed(final boolean value) {
    this.replacementsSuppressed = value;
  }

  /**
   * @return Stop execution flag.
   */
  public boolean isStopped() {
    return stopped;
  }

  public String getStopReason() {
    return stopReason;
  }

  /**
   * Some error conditions require that Flywheel execution must stop.
   */
  public void stop(String reason) {
    stopReason = reason;
    stopped = true;
  }

  private boolean replacementsSuppressed = false;
}
