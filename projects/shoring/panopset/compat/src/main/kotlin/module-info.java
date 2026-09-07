module com.panopset.compat {
  requires kotlin.stdlib;
  requires transitive java.logging;
  requires jdk.crypto.cryptoki;
  requires com.jcraft.jsch;
  requires tools.jackson.databind;
  exports com.panopset.compat;
}
