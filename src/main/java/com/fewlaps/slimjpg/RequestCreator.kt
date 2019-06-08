package com.fewlaps.slimjpg

import core.JpegOptimizer
import core.Result

data class RequestCreator(val image: ByteArray,
                          val maxVisualDiff: Double = 0.0,
                          val maxFileWeight: Long = -1,
                          val keepMetadata: Boolean = false) {

    fun optimize(): Result {
        return JpegOptimizer().optimize(image, maxVisualDiff, maxFileWeight, keepMetadata)
    }

    fun maxVisualDiff(maxVisualDiff: Double): RequestCreator {
        return this.copy(maxVisualDiff = maxVisualDiff)
    }

    fun maxFileWeight(sizeInBytes: Long): RequestCreator {
        return this.copy(maxFileWeight = sizeInBytes)
    }

    fun maxFileWeightInKB(sizeInKiloBytes: Long): RequestCreator {
        return maxFileWeight(sizeInKiloBytes * 1024)
    }

    fun maxFileWeightInMB(sizeInMegaBytes: Double): RequestCreator {
        return maxFileWeight((sizeInMegaBytes * 1024 * 1024).toLong())
    }

    fun keepMetadata(): RequestCreator {
        return this.copy(keepMetadata = true)
    }

    fun deleteMetadata(): RequestCreator {
        return this.copy(keepMetadata = false)
    }
}
