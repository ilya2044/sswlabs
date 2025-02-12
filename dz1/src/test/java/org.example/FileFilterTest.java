package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

class FileFilterTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        System.out.println("Временная директория: " + tempDir);

        Path inputFile = tempDir.resolve("input.txt");
        Files.write(inputFile, List.of("123", "45.67", "test", "test1", "42"));
    }

    @Test
    void testFileFilter() throws IOException {
        System.out.println("Тестируем в директории: " + tempDir);

        Path inputFile = tempDir.resolve("input.txt");

        FileFilter.main(new String[]{inputFile.toString(), "--output", tempDir.toString(), "--prefix", "output_"});

        System.out.println("Файлы в tempDir: " + Files.list(tempDir).map(Path::getFileName).toList());

        Path intFile = tempDir.resolve("output_integers.txt");
        Path floatFile = tempDir.resolve("output_floats.txt");
        Path strFile = tempDir.resolve("output_strings.txt");

        assertTrue(Files.exists(intFile), "output_integers.txt не создан");
        assertTrue(Files.exists(floatFile), "output_floats.txt не создан");
        assertTrue(Files.exists(strFile), "output_strings.txt не создан");

        assertEquals(List.of("123", "42"), Files.readAllLines(intFile));
        assertEquals(List.of("45.67"), Files.readAllLines(floatFile));
        assertEquals(List.of("test", "test1"), Files.readAllLines(strFile));
    }
}
