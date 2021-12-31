package edu.seu.rabbitmqdemo.utils;

import java.util.Scanner;

public class ScannerUtils {
    private ScannerUtils() {
    }

    private static class ScannerUtilsInstance {
        private static final Scanner scanner = new Scanner(System.in);
    }

    public static String getString() {
        return ScannerUtilsInstance.scanner.nextLine();
    }
}