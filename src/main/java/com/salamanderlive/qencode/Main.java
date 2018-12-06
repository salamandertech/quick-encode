package com.salamanderlive.qencode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Main {
    private static final String IMAGE_FORMAT = "png";

    public static void main(String[] args) {
        final var config = Config.parse(args);
        if (config.getShowVersion())
            showVersion();
        else
            System.exit(new Main(config).run());
    }

    private static void showVersion() {
        final var pkg = Main.class.getPackage();
        System.out.printf(
                "%s (%s)\nVersion %s by %s\n",
                pkg.getSpecificationTitle(),
                pkg.getName(),
                pkg.getSpecificationVersion(),
                pkg.getSpecificationVendor());
    }

    private final Config config;

    private Main(Config config) {
        this.config = config;
    }

    private void printf(String message, Object... args) {
        if (config.getVerbosity() > 0)
            System.err.printf(message, args);
    }

    private int run() {
        final var writer = new QRCodeWriter();

        final var height = config.getHeight();
        final var width = config.getWidth();

        for (var pathname : config.getPaths()) {
            var pathIn = Paths.get(pathname);
            var pathOut = Paths.get(pathIn + ".png");

            printf("Encoding contents of %s to %s.\n", pathIn, pathOut);

            byte[] contents;
            try {
                contents = Files.readAllBytes(pathIn);
            } catch (IOException e) {
                printf("Could not read \"%s\", skipping.\n", pathIn);
                continue;
            }
            var contentsStr = StandardCharsets.ISO_8859_1.decode(ByteBuffer.wrap(contents)).toString();
            BitMatrix m;
            try {
                m = writer.encode(contentsStr, BarcodeFormat.QR_CODE, width, height);
            } catch (WriterException e) {
                printf("Could not encode contents of \"%s\".\n", pathIn);
                continue;
            }

            try {
                MatrixToImageWriter.writeToPath(m, IMAGE_FORMAT, pathOut);
            } catch (IOException e) {
                printf("Could not write \"%s\".\n", pathOut);
            }
        }

        return 0;
    }
}
