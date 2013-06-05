package com.github.mlmoses.qencode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public final class Main {

    private static final int DEFAULT_HEIGHT = 512;
    private static final int DEFAULT_WIDTH = 512;

    public static void main(String[] args) {
        // TODO: If no arguments are provided, read from stdin.
        // TODO: If reading from stdin, send output to stdout.
        final Config config = parseArgs(args);
        final Charset charset = Charset.forName("ISO-8859-1");
        final QRCodeWriter writer = new QRCodeWriter();
        final int height = config.getHeight();
        final int width = config.getWidth();
        for (String pathname : config.getPaths()) {
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
            BitMatrix m;
            try {
                m = writer.encode(contentsStr, BarcodeFormat.QR_CODE, width, height);
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

    private static Config parseArgs(String[] args) {
        Config c = new Config().setHeight(DEFAULT_HEIGHT).setWidth(DEFAULT_WIDTH);
        Collection<String> paths = c.getPaths();
        State state = State.NONE;
        boolean noMoreArgs = false;
        for (String a : args) {
            if ("-h".equals(a) || "--height".equals(a)) {
                state = State.HEIGHT;
            } else if ("-w".equals(a) || "--width".equals(a)) {
                state = State.WIDTH;
            } else if (a.startsWith("-")) {
                if (noMoreArgs) {
                    paths.add(a);
                } else if ("--".equals(a)) {
                    noMoreArgs = true;
                }
            } else {
                switch (state) {
                    case HEIGHT:
                        c.setHeight(Integer.parseInt(a));
                        break;
                    case WIDTH:
                        c.setWidth(Integer.parseInt(a));
                        break;
                    default:
                        paths.add(a);
                        break;
                }
                state = State.NONE;
            }
        }
        return c;
    }

    private Main() {}

    private static enum State {
        NONE,
        HEIGHT,
        WIDTH
    }

}
