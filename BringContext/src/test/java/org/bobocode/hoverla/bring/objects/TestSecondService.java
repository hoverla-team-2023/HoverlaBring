package org.bobocode.hoverla.bring.objects;

import org.bobocode.hoverla.bring.annotations.Autowired;
import org.bobocode.hoverla.bring.annotations.Component;

@Component
public class TestSecondService {

  @Autowired
  private TestThirdService testThirdService;

  public TestSecondService() {
  }

  public TestThirdService getTestThirdService() {
    return testThirdService;
  }

}
