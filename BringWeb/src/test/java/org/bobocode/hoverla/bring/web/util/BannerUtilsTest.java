package org.bobocode.hoverla.bring.web.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BannerUtilsTest {

  private static MockedAppender mockedAppender;
  private static Logger logger;

  @BeforeEach
  public void setup() {
    mockedAppender.clear();
  }

  @BeforeAll
  public static void setupClass() {
    mockedAppender = new MockedAppender();
    logger = (Logger) LogManager.getLogger(BannerUtils.class);
    logger.addAppender(mockedAppender);
    logger.setLevel(Level.INFO);
  }

  @AfterAll
  public static void teardown() {
    logger.removeAppender(mockedAppender);
  }

  @Test
  public void givenResourceName_whenPrintBanner_thenVerifyLogs() {
    BannerUtils.printBanner("banner_hoverla.txt");

    String messages = mockedAppender.getAllMessagesAsString();
    Approvals.verify(messages, new FolderBasedNamer().createOptions());
  }

  @Test
  public void givenNonExistingResourceName_whenPrintBanner_thenVerifyLogs() {
    BannerUtils.printBanner("non_existing_resource.txt");

    String messages = mockedAppender.getAllMessagesAsString();
    Approvals.verify(messages, new FolderBasedNamer().createOptions());
  }

}