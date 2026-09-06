package com.panopset.flywheel;

public class FlywheelFunction implements Comparable<FlywheelFunction> {
  
  public FlywheelFunction(){
    this("", "", "", "", "");
  }
  public FlywheelFunction(String key, String packageName, String className, String methodName, String example) {
    setKey(key);
    setPackageName(packageName);
    setClassName(className);
    setMethodName(methodName);
    setExample(example);
  }

  public String getKey() {
    return key;
  }
  
  public void setKey(String key) {
    this.key = key;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getExample() {
    return example;
  }

  public void setExample(String example) {
    this.example = example;
  }
  
  @Override
  public String toString() {
    return getKey();
  }

  private String key;
  private String packageName;
  private String className;
  private String methodName;
  private String example;
  
  @Override
  public int compareTo(FlywheelFunction o) {
    return this.getKey().compareTo(o.getKey());
  }
}
