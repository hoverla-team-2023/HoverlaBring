package org.bobocode.hoverla.bring.web.util;

import java.io.File;

import org.approvaltests.core.Options;
import org.approvaltests.namer.NamerWrapper;

public class FolderBasedNamer extends NamerWrapper {

  private static final String DATA_DIR = "/data/";
  private static final String APPROVED_DIR = "approved/";
  private static final String RECEIVED_DIR = "received/";

  public FolderBasedNamer() {
    super(new Options().forFile().getNamer());
  }

  public Options createOptions() {
    return new Options(new Options(), new Options.FileOptions(this, ".txt"));
  }

  public File getReceivedFile(String extensionWithDot) {
    return makeFilepath(extensionWithDot, RECEIVED_DIR);
  }

  public File getApprovalFile(String extensionWithDot) {
    return makeFilepath(extensionWithDot, APPROVED_DIR);
  }

  private File makeFilepath(String extensionWithDot, String subdir) {
    return new File(getSourceFilePath()
                    + DATA_DIR
                    + subdir
                    + getApprovalName()
                    + extensionWithDot);
  }

}