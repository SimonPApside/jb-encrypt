package com.example.batchprocessing;

import java.io.File;
import java.nio.file.Files;

import javax.crypto.SecretKey;

import org.springframework.batch.item.ItemProcessor;

public class FileEncryptProcessor implements ItemProcessor<File, CustomFile> {

    private final SecretKey secretKey;

    public FileEncryptProcessor(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public CustomFile process(File file) throws Exception {
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] encrypted = AESUtil.encrypt(data, secretKey);
        String extension = AESUtil.getFileExtension(file.getName());
        // String outputName = file.getName().substring(0, file.getName().length() -
        // extension.length() + 1) + ".enc";
        return new CustomFile(file.getName(), encrypted, file, extension);
    }
}