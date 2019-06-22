package com.fewlaps.slimjpg

import com.fewlaps.slimjpg.core.util.InputStreamToByteArray
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
}
