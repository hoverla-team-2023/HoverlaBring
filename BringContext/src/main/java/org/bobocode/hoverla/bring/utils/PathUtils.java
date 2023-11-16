package org.bobocode.hoverla.bring.utils;


import lombok.experimental.UtilityClass;

import java.util.Optional;

import static java.util.Objects.isNull;

/**
 * Utility class for handling paths related to Java classes and packages.
 */
@UtilityClass
public class PathUtils {

    private static final char PACKAGE_SEPARATOR = '.';
    private static final char FILE_SEPARATOR = getSystemFileSeparator();

    /**
     * Converts a package name to a file system path.
     *
     * @param path The package name.
     * @return The file system path representation of the package name, or null if input is invalid.
     * Example usage: toFileSystemPath("com.example.util") returns "com/example/util".
     */
    public static String toFileSystemPath(String path) {
        return isNull(path) ? null : path.replace(PACKAGE_SEPARATOR, FILE_SEPARATOR);
    }

    /**
     * Converts a file system path to a package name.
     *
     * @param path The file system path (using '/' as separator).
     * @return The package name representation of the path, or null if input is invalid.
     * Example usage: toPackageNamePath("com/example/util") returns "com.example.util".
     */
    public static String toPackageNamePath(String path) {
        return isNull(path) ? null : path.replace(FILE_SEPARATOR, PACKAGE_SEPARATOR);
    }

    /**
     * Checks if a given file name represents a Java class file.
     *
     * @param fileName The file name to check.
     * @return true if the file name ends with ".class", false otherwise.
     * Example usage: isClass("MyClass.class") returns true.
     */
    public static boolean isClass(String fileName) {
        return !isNull(fileName) && fileName.endsWith(".class");
    }

    /**
     * Generates a package name by appending a file name to an existing package name.
     *
     * @param parentPackageName The parent package name.
     * @param fileName          The file name to append.
     * @return The concatenated package name, or null if input is invalid.
     * Example usage: getPackageName("com.example", "subpackage") returns "com.example.subpackage".
     */
    public static String getPackageName(String parentPackageName, String fileName) {
        if (isNull(parentPackageName) || isNull(fileName)) {
            return null;
        }
        return parentPackageName + PACKAGE_SEPARATOR + fileName;
    }

    /**
     * Generates a fully qualified class name from a package name and a file name ( .class removed) .
     *
     * @param packageName The package name.
     * @param fileName    The file name, expected to end with ".class".
     * @return The fully qualified class name, or null if input is invalid.
     * Example usage: getClassName("com.example", "MyClass.class") returns "com.example.MyClass"
     */
    public static String getClassName(String packageName, String fileName) {
        if (isNull(packageName) || !isClass(fileName)) {
            return null;
        }
        return packageName + PACKAGE_SEPARATOR + fileName.substring(0, fileName.length() - 6);
    }


    private static char getSystemFileSeparator() {
        return Optional.ofNullable(System.getProperty("file.separator"))
                .map(s -> s.charAt(0))
                .orElseThrow(() -> new IllegalStateException("File separator property not found"));
    }
}

