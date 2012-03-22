package com.github.mlmoses.qencode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public final class Main {

    public static void main(String[] args) {
        // TODO: If no arguments are provided, read from stdin.
        // TODO: If reading from stdin, send output to stdout.
        final Charset charset = Charset.forName("ISO-8859-1");
        final QRCodeWriter writer = new QRCodeWriter();
        for (String pathname : args) {
            Path pathIn = Paths.get(pathname);
            Path pathOut = Paths.get(pathIn + ".png");
            byte[] contents;
            try {
                contents = Files.readAllBytes(Paths.get(pathname));
            } catch (IOException e) {
                System.out.printf("Could not read \"%s\", skipping.\n", pathIn);
                continue;
            }
            String contentsStr = charset.decode(ByteBuffer.wrap(contents)).toString();
            // TODO: Allow height and width to be configurable.
            BitMatrix m;
            try {
                m = writer.encode(contentsStr, BarcodeFormat.QR_CODE, 512, 512);
            } catch (WriterException e) {
                System.out.printf("Could not encode contents of \"%s\".\n", pathIn);
                continue;
            }

            try {
                MatrixToImageWriter.writeToFile(m, "png", pathOut.toFile());
            } catch (IOException e) {
                System.out.printf("Could not write \"%s\".\n", pathOut);
            }
        }
    }

    private Main() {}

}
