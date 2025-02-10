package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileFilter {
    private static boolean appendMode = false;
    private static boolean fullStats = false;
    private static String outputPath = "";
    private static String prefix = "";

    public static void main(String[] args) {
        List<String> inputFiles = new ArrayList<>();
        parseArguments(args, inputFiles);

        Map<String, List<String>> categorizedData = new HashMap<>();
        categorizedData.put("integers", new ArrayList<>());
        categorizedData.put("floats", new ArrayList<>());
        categorizedData.put("strings", new ArrayList<>());

        for (String file : inputFiles) {
            processFile(file, categorizedData);
        }

        writeOutputFiles(categorizedData);
        printStatistics(categorizedData);
    }

    private static void parseArguments(String[] args, List<String> inputFiles) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-a":
                    appendMode = true;
                    break;
                case "-o":
                    if (++i < args.length) outputPath = args[i];
                    break;
                case "-p":
                    if (++i < args.length) prefix = args[i];
                    break;
                case "-s":
                    fullStats = false;
                    break;
                case "-f":
                    fullStats = true;
                    break;
                default:
                    inputFiles.add(args[i]);
            }
        }
    }

    private static void processFile(String fileName, Map<String, List<String>> categorizedData) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categorizeLine(line, categorizedData);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файла " + fileName + ": " + e.getMessage());
        }
    }

    private static void categorizeLine(String line, Map<String, List<String>> categorizedData) {
        if (line.matches("-?\\d+")) {
            categorizedData.get("integers").add(line);
        } else if (line.matches("-?\\d+\\.\\d+")) {
            categorizedData.get("floats").add(line);
        } else {
            categorizedData.get("strings").add(line);
        }
    }

    private static void writeOutputFiles(Map<String, List<String>> categorizedData) {
        categorizedData.forEach((type, data) -> {
            if (!data.isEmpty()) {
                String fileName = outputPath + File.separator + prefix + type + ".txt";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, appendMode))) {
                    for (String entry : data) {
                        writer.write(entry);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при записи в файл " + fileName + ": " + e.getMessage());
                }
            }
        });
    }

    private static void printStatistics(Map<String, List<String>> categorizedData) {
        categorizedData.forEach((type, data) -> {
            System.out.println("Тип: " + type);
            System.out.println("Количество: " + data.size());
            if (fullStats && !data.isEmpty()) {
                if (type.equals("integers") || type.equals("floats")) {
                    List<Double> numbers = data.stream().map(Double::parseDouble).collect(Collectors.toList());
                    System.out.printf("Мин: %.2f, Макс: %.2f, Сумма: %.2f, Среднее: %.2f\n",
                            Collections.min(numbers), Collections.max(numbers), numbers.stream().mapToDouble(Double::doubleValue).sum(),
                            numbers.stream().mapToDouble(Double::doubleValue).average().orElse(0));
                } else {
                    int minLen = data.stream().mapToInt(String::length).min().orElse(0);
                    int maxLen = data.stream().mapToInt(String::length).max().orElse(0);
                    System.out.println("Самая короткая строка: " + minLen + ", Самая длинная строка: " + maxLen);
                }
            }
        });
    }
}
