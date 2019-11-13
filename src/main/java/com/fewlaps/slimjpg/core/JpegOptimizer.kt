package com.fewlaps.slimjpg.core

import com.fewlaps.slimjpg.core.util.BufferedImageComparator
import com.fewlaps.slimjpg.core.util.JpegChecker
import com.fewlaps.slimjpg.core.util.JpegCompressor
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.floor

class JpegOptimizer {

    private val compressor: JpegCompressor = JpegCompressor()
    private val comparator: BufferedImageComparator = BufferedImageComparator()
    private val checker: JpegChecker = JpegChecker()

    @Throws(IOException::class)
    fun optimize(source: ByteArray, maxVisualDiff: Double, maxWeight: Long): Result {
        val withMetadata = optimize(source, maxVisualDiff, maxWeight, true)
        val withoutMetadata = optimize(source, maxVisualDiff, maxWeight, false)

        return if (withMetadata.savedBytes > withoutMetadata.savedBytes) {
            withMetadata
        } else {
            withoutMetadata
        }
    }

    @Throws(IOException::class)
    fun optimize(source: ByteArray, maxVisualDiff: Double, maxWeight: Long, keepMetadata: Boolean): Result {
        require(!(maxVisualDiff < 0 || maxVisualDiff > 100)) { "maxVisualDiff should be a percentage between 0 and 100" }

        val start = System.currentTimeMillis()
        val (picture, jpegQualityUsed, iterationsMade) = getOptimizedPicture(source, maxVisualDiff, maxWeight, keepMetadata)
        val end = System.currentTimeMillis()

        val elapsedTime = end - start
        val calculator = ResultStatisticsCalculator(source, picture)
        return Result(
                picture,
                elapsedTime,
                calculator.getSavedBytes().toLong(),
                calculator.getSavedRatio()!!,
                jpegQualityUsed,
                iterationsMade
        )
    }

    @Throws(IOException::class)
    private fun getOptimizedPicture(source: ByteArray, maxVisualDiff: Double, maxWeight: Long, keepMetadata: Boolean): InternalResult {
        var iterationsMade = 0

        val jpegSource = if (!checker.isJpeg(source)) {
            compressor.writeJpg(source, MAX_JPEG_QUALITY, keepMetadata)
        } else {
            source
        }

        val sourceBufferedImage = ImageIO.read(ByteArrayInputStream(jpegSource))

        if (maxVisualDiff == 0.0) {
            val quality = 100
            var result = compressor.writeJpg(jpegSource, quality, keepMetadata)
            if (keepMetadata) {
                if (result.size > jpegSource.size) {
                    result = jpegSource
                }
            }
            return InternalResult(
                    result,
                    quality,
                    iterationsMade
            )
        }

        var minQuality = MIN_JPEG_QUALITY
        var maxQuality = MAX_JPEG_QUALITY
        var quality = 0

        while (minQuality <= maxQuality) {
            quality = floor((minQuality + maxQuality) / 2.0).toInt()

            if (isThisQualityTooHigh(jpegSource, sourceBufferedImage, quality, maxVisualDiff, maxWeight, keepMetadata)) {
                maxQuality = quality - 1
            } else {
                minQuality = quality + 1
            }

            iterationsMade++
        }

        var result: ByteArray
        if (quality < MAX_JPEG_QUALITY || !keepMetadata) {
            result = compressor.writeJpg(jpegSource, quality, keepMetadata)
            if (maxWeightIsDefined(maxWeight) && result.size > maxWeight && quality > 0) {
                quality -= 1
                result = compressor.writeJpg(jpegSource, quality, keepMetadata)
            }
            if (result.size > jpegSource.size && keepMetadata) {
                result = jpegSource
                quality = MAX_JPEG_QUALITY
            }
        } else {
            result = compressor.writeJpg(jpegSource, quality, keepMetadata)
            if (result.size > jpegSource.size) {
                result = jpegSource
            }
        }

        return InternalResult(
                result,
                quality,
                iterationsMade
        )
    }

    @Throws(IOException::class)
    private fun isThisQualityTooHigh(source: ByteArray, sourceBufferedImage: BufferedImage, quality: Int, maxVisualDiffPorcentage: Double, maxWeight: Long, keepMetadata: Boolean): Boolean {
        val optimizedPicture = compressor.writeJpg(source, quality, keepMetadata)
        if (maxWeightIsDefined(maxWeight) && optimizedPicture.size > maxWeight) {
            return true
        }

        val bufferedOptimizedPicture = ImageIO.read(ByteArrayInputStream(optimizedPicture))

        if (maxVisualDiffPorcentage == 0.0) {
            return comparator.isSameContent(sourceBufferedImage, bufferedOptimizedPicture)
        } else {
            var diff = comparator.getDifferencePercentage(sourceBufferedImage, bufferedOptimizedPicture)
            diff *= 100.0
            return diff < maxVisualDiffPorcentage
        }
    }

    private fun maxWeightIsDefined(maxWeight: Long): Boolean {
        return maxWeight > 0
    }

    companion object {
        private const val MIN_JPEG_QUALITY = 0
        private const val MAX_JPEG_QUALITY = 100
    }
}