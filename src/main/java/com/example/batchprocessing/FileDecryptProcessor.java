package com.example.batchprocessing;

import java.io.File;
import java.nio.file.Files;

import javax.crypto.SecretKey;

import org.springframework.batch.item.ItemProcessor;

public class FileDecryptProcessor implements ItemProcessor<File, CustomFile> {

    private final SecretKey secretKey;

    public FileDecryptProcessor(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public CustomFile process(File file) throws Exception {
        byte[] encrypted = Files.readAllBytes(file.toPath());
        byte[] decrypted = AESUtil.decrypt(encrypted, secretKey);

        String outputName = file.getName();
        String extension = AESUtil.getFileExtension(file.getName());
        // if (extension.equalsIgnoreCase("enc")) {
        // outputName = outputName.substring(0, outputName.length() - extension.length()
        // + 1);
        // }

        return new CustomFile(outputName, decrypted, file, extension);
    }
}