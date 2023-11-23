package org.bobocode.hoverla.bring.test;

import org.bobocode.hoverla.bring.annotations.Autowired;
import org.bobocode.hoverla.bring.annotations.Component;
import org.bobocode.hoverla.bring.test.service.UserService;
import org.bobocode.hoverla.bring.test.service.innerservice.InnerService;
import org.bobocode.hoverla.bring.test.service.nestedservice.NestedService;

@Component
public class TestComponent {

  @Autowired
  private InnerService innerService;

  @Autowired
  private NestedService nestedService;

  @Autowired
  private UserService userService;


}
