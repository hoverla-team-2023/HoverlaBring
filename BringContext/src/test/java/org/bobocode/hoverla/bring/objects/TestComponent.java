package org.bobocode.hoverla.bring.objects;

import org.bobocode.hoverla.bring.annotations.Autowired;
import org.bobocode.hoverla.bring.annotations.Component;

@Component
public class TestComponent {
  @Autowired
  private MyService myService;

  public MyService getMyService() {
    return myService;
  }

}
