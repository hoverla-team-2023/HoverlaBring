package org.bobocode.hoverla.bring.web.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import lombok.Getter;

@Getter
public class MockedAppender extends AbstractAppender {

  private final List<String> messages = new ArrayList<>();

  protected MockedAppender() {
    super("MockedAppender", null, null);
  }

  @Override
  public void append(LogEvent event) {
    messages.add("%s %s [%s] : %s".formatted(event.getLevel(), event.getLoggerName(), event.getThreadName(), event.getMessage().getFormattedMessage()));
  }

  public String getAllMessagesAsString() {
    return String.join("\n", messages);
  }

  public void clear() {
    getMessages().clear();
  }

}