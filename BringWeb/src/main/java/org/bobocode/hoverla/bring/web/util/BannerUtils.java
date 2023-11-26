package org.bobocode.hoverla.bring.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BannerUtils {

  public static void printBanner(String bannerFilePath) {

    try (InputStream inputStream = BannerUtils.class.getResourceAsStream(bannerFilePath)) {
      if (inputStream != null) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        log.info("\n{}", content);
      } else {
        log.warn("Banner with path: {} is not found in classpath", bannerFilePath);
      }
    } catch (IOException e) {
      log.warn("Printing banner with path: {} is failed", bannerFilePath, e);
    }

  }

}


