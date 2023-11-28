package org.bobocode.hoverla.bring.objects;

import org.bobocode.hoverla.bring.annotations.Autowired;
import org.bobocode.hoverla.bring.annotations.Component;

@Component
public class TestFirstService {

  @Autowired
  private TestSecondService testSecondService;

  public TestFirstService() {
  }

  public TestSecondService getTestSecondService() {
    return testSecondService;
  }

}
