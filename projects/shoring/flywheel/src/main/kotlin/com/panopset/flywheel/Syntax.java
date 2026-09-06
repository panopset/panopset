package com.panopset.flywheel;

/**
 * Flywheel script syntax.
 * <p>
 * Script commands are started with an open directive, and closed with an end
 * directive. Within the command there is a directive that determines what
 * command is to be executed. If that directive is omitted, then the command
 * becomes a simple variable resolution command, the Command Variable.
 * </p>
 * <p>
 * The default syntax has a dollar sign and open curly bracket as the open
 * directive, and a closing curly bracket as the close directives.
 * </p>
 * You may easily change that to a JSP like syntax if you like, by specifying
 * new directives in FlywheelBuilder.
 *
 * @see FlywheelBuilder
 *
 */
public final class Syntax {

  /**
   * Default directive is <b>&#064;</b>.
   */
  public static final String DEFAULT_DIRECTIVE = "@";

  /**
   * Default open directive is <b>${</b>.
   */
  public static final String DEFAULT_OPEN_DIRECTIVE = "${";

  /**
   * Default close directive is "}".
   */
  public static final String DEFAULT_CLOSE_DIRECTIVE = "}";

  /**
   * Directive.
   */
  private String directive;

  /**
   * Open directive.
   */
  private String openDirective;

  /**
   * Close directive.
   */
  private String closeDirective;

  /**
   * @return Directive, default is <b>&#064;</b>.
   */
  public static String getDirective() {
    if (getInstance().directive == null) {
      setDirective(DEFAULT_DIRECTIVE);
    }
    return getInstance().directive;
  }

  /**
   * Set directive, rarely used.
   * 
   * @param newDirective
   *          New directive to set.
   */
  public static void setDirective(final String newDirective) {
    getInstance().directive = newDirective;
  }

  /**
   * @return Open directive, default is <b>${</b>.
   */
  public static String getOpenDirective() {
    if (getInstance().openDirective == null) {
      setOpenDirective(DEFAULT_OPEN_DIRECTIVE);
    }
    return getInstance().openDirective;
  }

  /**
   * Set open directive, rarely used.
   * 
   * @param newOpenDirective
   *          New open directive.
   */
  public static void setOpenDirective(final String newOpenDirective) {
    getInstance().openDirective = newOpenDirective;
  }

  /**
   * @return Close directive, default is <b>}</b>.
   */
  public static String getCloseDirective() {
    if (getInstance().closeDirective == null) {
      setCloseDirective(DEFAULT_CLOSE_DIRECTIVE);
    }
    return getInstance().closeDirective;
  }

  /**
   * Set close directive, rarely used.
   * 
   * @param newCloseDirective
   *          New close directive.
   */
  public static void setCloseDirective(final String newCloseDirective) {
    getInstance().closeDirective = newCloseDirective;
  }

  /**
   * Based on SingletonHolder inner class by Bill Pugh.
   *
   * <h1>References</h1>
   * <ul>
   * <li>
   * <a href="http://en.wikipedia.org/wiki/Singleton_pattern">
   * http://en.wikipedia.org/wiki/Singleton_pattern </a></li>
   * </ul>
   *
   */
  private static final class Singleton {
    /**
     * Instance variable.
     */
    private static final Syntax INSTANCE = new Syntax();

    /**
     * Private constructor.
     */
    private Singleton() {
      // Prevent instantiation.
    }
  }

  /**
   * @return static Syntax instance.
   */
  private static Syntax getInstance() {
    return Singleton.INSTANCE;
  }

  /**
   * Private singleton constructor.
   */
  private Syntax() {
  }
}
