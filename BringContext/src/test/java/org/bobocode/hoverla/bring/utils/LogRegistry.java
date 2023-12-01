package org.bobocode.hoverla.bring.utils;

import java.util.ArrayList;
import java.util.List;

public class LogRegistry {

  private static List<String> executionLogs = new ArrayList<>();

  public static void addExecutionLog(String message) {
    executionLogs.add(message);
  }

  public static List<String> getAllLogs() {
    return executionLogs;
  }

  public static void clear() {
    executionLogs.clear();
  }

}
