package com.fewlaps.slimjpg

import com.fewlaps.slimjpg.core.util.InputStreamToByteArray
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object SlimJpg {

    @JvmStatic
    fun file(image: ByteArray): RequestCreator {
        return RequestCreator(image)
    }

    @JvmStatic
    fun file(image: InputStream): RequestCreator {
        val bytes = InputStreamToByteArray().toByteArray(image)
        return file(bytes)
    }

    @JvmStatic
    fun file(image: File): RequestCreator {
        val inputStream = FileInputStream(image)
        return file(inputStream)
    }
}
