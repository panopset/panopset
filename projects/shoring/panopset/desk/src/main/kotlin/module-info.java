module com.panopset.desk {
  requires transitive com.panopset.flywheel;
  requires transitive com.panopset.fxapp;
  requires transitive java.desktop;
  requires javafx.web;
  requires kotlin.stdlib;

  exports com.panopset;
  exports com.panopset.desk.utilities;
  exports com.panopset.desk.security;
  exports com.panopset.marin.compat.util;
  exports com.panopset.marin.fx;
}
