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
        sb.append("Size: " + ReadableUtils.formatFileSize(picture.size.toLong()) + "\n")
        sb.append("Saved size: " + ReadableUtils.formatFileSize(savedBytes) + "\n")
        sb.append("Saved ratio: " + ReadableUtils.formatPercentage(savedRatio) + "\n")
        sb.append("JPEG quality used: $jpegQualityUsed%" + "\n")
        sb.append("Iterations made: $iterationsMade" + "\n")
        sb.append("Time: " + ReadableUtils.formatElapsedTime(elapsedTime) + "\n")
        sb.append("Error: " + (internalError ?: "No"))
        return sb.toString()
    }
}