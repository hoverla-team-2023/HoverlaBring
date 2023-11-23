package org.bobocode.hoverla.bring.test.repository;

import org.bobocode.hoverla.bring.annotations.Autowired;
import org.bobocode.hoverla.bring.annotations.Component;
import org.bobocode.hoverla.bring.test.repository.inner.InnerRepo;

@Component
public class SimpleRepo {

  @Autowired
  private InnerRepo innerRepo;
}
