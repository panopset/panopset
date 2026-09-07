package com.panopset.tests.transformer

import com.panopset.compat.Fileop
import java.io.File

abstract class StandardTransformerTest//  this.fromFileRezPath = fromFileRezPath;
//  this.toFileRezPath = toFileRezPath;
// private final String fromFileRezPath;
// private final String toFileRezPath;
// public StandardTransformerTest(String packageName, String fromFileRezPath, String toFileRezPath,
//   ResultsDataSupplier expectedResults) {
//  super(expectedResults);
//  this.packageName = packageName;
//  this.fromFileRezPath = fromFileRezPath;
//  this.toFileRezPath = toFileRezPath;
//  //new File(  Fileop.combinePaths(new StandardPackagePath(packageName).getPackagePath(), expectedFileRezPath))
// }
    (// private File fromFile;
    val packageName: String
) : TransformerTest() {

    fun getPackagePath(fileName: String): File {
        return StandardPackagePath(packageName).getFile(fileName)
    }

    fun readTextFromStandardResource(fileName: String): String {
        return Fileop.readTextFile(getPackagePath(fileName))
    }
    //
    // @Override
    // File getFromFile() {
    //  if (fromFile == null) {
    //   fromFile = new File(Fileop.combinePaths(getPackagePath(), fromFileRezPath));
    //  }
    //  return fromFile;
    // }
    // private File toFile;
    //
    // @Override
    // File getToFile() {
    //  if (toFile == null) {
    //   toFile = new File(Fileop.combinePaths(getPackagePath(), toFileRezPath));
    //  }
    //  return toFile;
    // }
}
