package com.example.batchprocessing;

import java.io.File;

public record CustomFile(String name, byte[] content, File originalFile, String mimeType) {

}
