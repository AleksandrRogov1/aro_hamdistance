package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.utils;

import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.txt.UniversalEncodingDetector;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiscoveryUtils {
    public static BufferedWriter getBufferedWriter(final String fileToCreate) throws IOException {
        return getBufferedWriter(Paths.get(fileToCreate));
    }

    public static BufferedWriter getBufferedWriter(final Path filePath) throws IOException {
        return Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);
    }

    public static Charset detectCharset(final String pathToFile) {
        Charset charset = null;
        try (InputStream stream = TikaInputStream.get(Paths.get(pathToFile))) {
            UniversalEncodingDetector encoding = new UniversalEncodingDetector();
            charset = encoding.detect(stream, new Metadata());
        } catch (Exception ex) {
        }

        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return charset;
    }
}
