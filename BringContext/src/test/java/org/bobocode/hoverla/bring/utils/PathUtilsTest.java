package org.bobocode.hoverla.bring.utils;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathUtilsTest {

  @Test
  void testToFileSystemPath() {
    String expected = "org" + File.separator + "bobocode" + File.separator + "hoverla" + File.separator + "bring" + File.separator + "utils";
    assertEquals(expected, PathUtils.toFileSystemPath("org.bobocode.hoverla.bring.utils"));
    assertNull(PathUtils.toFileSystemPath(null));
    assertEquals("", PathUtils.toFileSystemPath(""));
  }

  @Test
  void testToPackageNamePath() {
    String expectedPath = "org.bobocode.hoverla.bring.utils";
    String inputPath = "org/bobocode/hoverla/bring/utils".replace("/", java.io.File.separator);
    assertEquals(expectedPath, PathUtils.toPackageNamePath(inputPath));
    assertNull(PathUtils.toPackageNamePath(null));
    assertEquals("", PathUtils.toPackageNamePath(""));
  }

  @Test
  void testIsClass() {
    assertTrue(PathUtils.isClass("Service.class"));
    assertFalse(PathUtils.isClass("Service.java"));
    assertFalse(PathUtils.isClass(null));
    assertFalse(PathUtils.isClass(""));
  }

  @Test
  void testGetPackageName() {
    assertEquals("com.example.Service", PathUtils.getPackageName("com.example", "Service"));
    assertNull(PathUtils.getPackageName(null, "Service"));
    assertNull(PathUtils.getPackageName("com.example", null));
    assertNull(PathUtils.getPackageName(null, null));
  }

  @Test
  void testGetClassName() {
    assertEquals("com.example.Service", PathUtils.getClassName("com.example", "Service.class"));
    assertNull(PathUtils.getClassName("com.example", "Service.java"));
    assertNull(PathUtils.getClassName(null, "Service.class"));
    assertNull(PathUtils.getClassName("com.example", null));
  }

  @Test
  void testGetFilesWithValidDirectory(@TempDir File tempDir) throws IOException {
    File tempFile = new File(tempDir, "tempFile.txt");
    assertTrue(tempFile.createNewFile());

    File[] files = PathUtils.getFiles(tempDir);
    assertEquals(1, files.length);
    assertEquals(tempFile.getName(), files[0].getName());
  }

}
