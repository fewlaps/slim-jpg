package com.fewlaps.slimjpg

object SlimJpg {

    @JvmStatic
    fun file(image: ByteArray): RequestCreator {
        return RequestCreator(image)
    }
}
