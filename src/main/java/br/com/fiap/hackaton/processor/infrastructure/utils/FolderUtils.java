package br.com.fiap.hackaton.processor.infrastructure.utils;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FolderUtils {

    private FolderUtils() {}

    private static final Logger log = LoggerFactory.getLogger(FolderUtils.class);

    @SneakyThrows
    public static void zip(String sourceDir, String zipFile) {
        Path sourcePath = Paths.get(sourceDir);
        var zipFilePath = Paths.get(sourceDir, zipFile);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
            try (Stream<Path> paths = Files.walk(sourcePath)) {
                paths.filter(path -> !Files.isDirectory(path))
                        .filter(path -> path.toString().endsWith(".jpg"))
                        .forEach(path -> {
                            try { //vavr TODO: analisar o
                                String relativePath = sourcePath.relativize(path).toString();
                                ZipEntry zipEntry = new ZipEntry(relativePath);
                                zos.putNextEntry(zipEntry);
                                Files.copy(path, zos);
                                zos.closeEntry();
                                log.info("Adicionado ao ZIP: {}", relativePath);
                            } catch (IOException e) {
                                log.error("Erro ao adicionar arquivo ao ZIP: {}", e.getMessage(), e);
                            }
                        });
            }
        }
    }

    @SneakyThrows
    public static byte[] zip(String sourceDir) {
        Path sourcePath = Paths.get(sourceDir);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
            try (Stream<Path> paths = Files.walk(sourcePath)) {
                paths.filter(path -> !Files.isDirectory(path))
                        .filter(path -> path.toString().endsWith(".jpg"))
                        .forEach(path -> {
                            try { //vavr TODO: analisar o
                                String relativePath = sourcePath.relativize(path).toString();
                                ZipEntry zipEntry = new ZipEntry(relativePath);
                                zos.putNextEntry(zipEntry);
                                Files.copy(path, zos);
                                zos.closeEntry();
                                log.info("Adicionado ao ZIP: {}", relativePath);
                            } catch (IOException e) {
                                log.error("Erro ao adicionar arquivo ao ZIP: {}", e.getMessage(), e);
                            }
                        });
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

}
