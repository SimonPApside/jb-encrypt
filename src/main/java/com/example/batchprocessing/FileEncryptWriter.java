package com.example.batchprocessing;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class FileEncryptWriter implements ItemWriter<CustomFile> {

    private final String outputDir;

    public FileEncryptWriter(String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void write(Chunk<? extends CustomFile> items) throws Exception {
        for (CustomFile item : items) {
            try (FileOutputStream fos = new FileOutputStream(outputDir + "/" + item.name())) {
                fos.write(item.content());
            }

            File originalFile = item.originalFile();
            if (originalFile.exists()) {
                boolean deleted = Files.deleteIfExists(originalFile.toPath());
                if (!deleted) {
                    System.err.println("⚠️ Could not delete: " + originalFile.getAbsolutePath());
                } else {
                    System.out.println("🗑️ Deleted input file: " + originalFile.getName());
                }
            }
        }

    }
}
