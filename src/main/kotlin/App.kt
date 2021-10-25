package com.salamanderlive.qencode

import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

private const val IMAGE_FORMAT = "png"

fun main(args: Array<String>) {
    val config = Config(args)
    when {
        config.showVersion -> {
            val pkg = Config::class.java.getPackage()
            println(
                """
                |${pkg.specificationTitle} (${pkg.name})
                |Version ${pkg.specificationVersion} by ${pkg.specificationVendor}
                """.trimMargin()
            )
        }
        config.unknownArgs.isNotEmpty() -> println(
            config.unknownArgs.joinToString(", ", prefix = "Unknown arguments: ")
        )
        else -> encode(config.width, config.height, config.paths)
    }
}

private fun encode(width: Int, height: Int, paths: List<String>) {
    val writer = QRCodeWriter()

    for (path in paths) {
        val pathIn: Path = Path.of(path).toAbsolutePath()
        val pathOut = pathIn.resolveSibling(pathIn.fileName.toString() + ".png")

        println("Encoding $pathIn\n      to $pathOut.")

        val contents = try {
            Files.readAllBytes(pathIn)
        } catch (e: IOException) {
            println("Could not read $pathIn")
            continue
        }

        val contentsStr = StandardCharsets.ISO_8859_1.decode(ByteBuffer.wrap(contents)).toString()
        val m = try {
            writer.encode(contentsStr, BarcodeFormat.QR_CODE, width, height)
        } catch (e: WriterException) {
            println("Could not encode $pathIn.")
            continue
        }

        try {
            MatrixToImageWriter.writeToPath(m, IMAGE_FORMAT, pathOut)
        } catch (e: IOException) {
            println("Could not write $pathOut")
        }
    }
}

