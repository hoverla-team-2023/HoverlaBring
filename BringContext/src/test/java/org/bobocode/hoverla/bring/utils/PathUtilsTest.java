package org.bobocode.hoverla.bring.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathUtilsTest {

    @Test
    public void testToFileSystemPath() {
        assertEquals("org/bobocode/hoverla/bring/utils", PathUtils.toFileSystemPath("org.bobocode.hoverla.bring.utils"));
        assertNull(PathUtils.toFileSystemPath(null));
        assertEquals("", PathUtils.toFileSystemPath(""));
    }

    @Test
    public void testToPackageNamePath() {
        assertEquals("org.bobocode.hoverla.bring.utils", PathUtils.toPackageNamePath("org/bobocode/hoverla/bring/utils"));
        assertNull(PathUtils.toPackageNamePath(null));
        assertEquals("", PathUtils.toPackageNamePath(""));
    }

    @Test
    public void testIsClass() {
        assertTrue(PathUtils.isClass("Service.class"));
        assertFalse(PathUtils.isClass("Service.java"));
        assertFalse(PathUtils.isClass(null));
        assertFalse(PathUtils.isClass(""));
    }

    @Test
    public void testGetPackageName() {
        assertEquals("com.example.Service", PathUtils.getPackageName("com.example", "Service"));
        assertNull(PathUtils.getPackageName(null, "Service"));
        assertNull(PathUtils.getPackageName("com.example", null));
        assertNull(PathUtils.getPackageName(null, null));
    }

    @Test
    public void testGetClassName() {
        assertEquals("com.example.Service", PathUtils.getClassName("com.example", "Service.class"));
        assertNull(PathUtils.getClassName("com.example", "Service.java"));
        assertNull(PathUtils.getClassName(null, "Service.class"));
        assertNull(PathUtils.getClassName("com.example", null));
    }
}
