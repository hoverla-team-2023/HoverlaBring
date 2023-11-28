package org.bobocode.hoverla.bring.objects;

import org.bobocode.hoverla.bring.annotations.Autowired;

public class TestComponent {
  @Autowired
  private MyService myService;

  public MyService getMyService() {
    return myService;
  }

}
