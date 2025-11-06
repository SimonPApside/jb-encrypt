package com.example.batchprocessing;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;

public class DirectoryFileReader implements ItemReader<File> {

    private final Iterator<File> fileIterator;

    public DirectoryFileReader(String inputDir) {
        File dir = new File(inputDir);
        List<File> files = Arrays.asList(dir.listFiles());
        this.fileIterator = files.iterator();
    }

    @Override
    public File read() {
        return fileIterator.hasNext() ? fileIterator.next() : null;
    }
}