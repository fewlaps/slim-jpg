package com.fewlaps.slimjpg

import com.fewlaps.slimjpg.core.JpegOptimizer
import com.fewlaps.slimjpg.core.Result

data class RequestCreator(val image: ByteArray,
                          val maxVisualDiff: Double = 0.0,
                          val maxFileWeight: Long = -1,
                          val metadataPolicy: MetadataPolicy = MetadataPolicy.WHATEVER_GIVES_SMALLER_FILES) {

    fun optimize(): Result {
        if (metadataPolicy == MetadataPolicy.WHATEVER_GIVES_SMALLER_FILES) {
            return JpegOptimizer().optimize(image, maxVisualDiff, maxFileWeight)
        } else {
            return JpegOptimizer().optimize(image, maxVisualDiff, maxFileWeight, metadataPolicy == MetadataPolicy.KEEP_METADATA)
        }
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
        return this.copy(metadataPolicy = MetadataPolicy.KEEP_METADATA)
    }

    fun deleteMetadata(): RequestCreator {
        return this.copy(metadataPolicy = MetadataPolicy.DELETE_METADATA)
    }

    fun useOptimizedMetadata(): RequestCreator {
        return this.copy(metadataPolicy = MetadataPolicy.WHATEVER_GIVES_SMALLER_FILES)
    }

    enum class MetadataPolicy {
        KEEP_METADATA,
        DELETE_METADATA,
        WHATEVER_GIVES_SMALLER_FILES
    }
}
