package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileFilter {
    public static String outputPath;
    public static String prefix = "";
    public static boolean appendMode = false;

    public static void categorizeLine(String line, Map<String, List<String>> categorizedData) {
        if (line.matches("-?\\d+")) {
            categorizedData.computeIfAbsent("integers", k -> new ArrayList<>()).add(line);
        } else if (line.matches("-?\\d+\\.\\d+")) {
            categorizedData.computeIfAbsent("floats", k -> new ArrayList<>()).add(line);
        } else {
            categorizedData.computeIfAbsent("strings", k -> new ArrayList<>()).add(line);
        }
    }

    public static void parseArguments(String[] args, List<String> inputFiles) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--output":
                    outputPath = args[++i];
                    break;
                case "--prefix":
                    prefix = args[++i];
                    break;
                case "--append":
                    appendMode = true;
                    break;
                default:
                    inputFiles.add(args[i]);
            }
        }
    }

    public static void writeOutputFiles(Map<String, List<String>> categorizedData) {
        for (Map.Entry<String, List<String>> entry : categorizedData.entrySet()) {
            String category = entry.getKey();
            List<String> data = entry.getValue();
            Path filePath = Paths.get(outputPath, prefix + category + ".txt");

            try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                    appendMode ? StandardOpenOption.APPEND : StandardOpenOption.CREATE)) {
                for (String line : data) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Ошибка при записи в файл " + filePath + ": " + e.getMessage());
            }
        }
    }

    public static void processFiles(List<String> inputFiles) {
        Map<String, List<String>> categorizedData = new HashMap<>();

        for (String inputFile : inputFiles) {
            Path path = Paths.get(inputFile);

            try {
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    categorizeLine(line, categorizedData);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла " + inputFile + ": " + e.getMessage());
            }
        }

        writeOutputFiles(categorizedData);
    }

    public static void main(String[] args) {
        List<String> inputFiles = new ArrayList<>();
        parseArguments(args, inputFiles);

        if (inputFiles.isEmpty()) {
            System.err.println("Не указаны файлы для обработки!");
            return;
        }

        if (outputPath == null || outputPath.isEmpty()) {
            System.err.println("Не указан путь для вывода!");
            return;
        }

        Path outputDir = Paths.get(outputPath);
        if (!Files.exists(outputDir)) {
            try {
                Files.createDirectories(outputDir);
            } catch (IOException e) {
                System.err.println("Ошибка при создании директории " + outputPath + ": " + e.getMessage());
                return;
            }
        }

        processFiles(inputFiles);
    }
}
