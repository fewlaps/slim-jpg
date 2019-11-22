package com.fewlaps.slimjpg.core

import com.fewlaps.slimjpg.core.util.ReadableUtils

data class Result(
        val picture: ByteArray,
        val elapsedTime: Long,
        val savedBytes: Long,
        val savedRatio: Double,
        val jpegQualityUsed: Int,
        val iterationsMade: Int,
        val internalError: Exception?) {

    override fun toString(): String {
        val sb = StringBuilder();
        sb.append("Size: " + ReadableUtils.formatFileSize(picture.size.toLong()))
        sb.append("Saved size: " + ReadableUtils.formatFileSize(savedBytes))
        sb.append("Saved ratio: " + ReadableUtils.formatPercentage(savedRatio))
        sb.append("JPEG quality used: $jpegQualityUsed%")
        sb.append("Iterations made: $iterationsMade")
        sb.append("Time: " + ReadableUtils.formatElapsedTime(elapsedTime))
        sb.append("Error: " + (internalError ?: "No"))
        return sb.toString()
    }
}