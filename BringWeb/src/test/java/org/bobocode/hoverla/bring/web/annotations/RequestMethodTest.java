package org.bobocode.hoverla.bring.web.annotations;

import java.util.Arrays;

import org.approvaltests.Approvals;
import org.bobocode.hoverla.bring.web.util.FolderBasedNamer;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

  @Test
  public void testFrozenRequestMethodNames() {
    Approvals.verify(Arrays.toString(RequestMethod.values()), new FolderBasedNamer().createOptions());
  }

}