package com.fewlaps.slimjpg.core

data class InternalResult(
        val picture: ByteArray,
        val jpegQualityUsed: Int,
        val iterationsMade: Int,
        val internalError: Exception? = null)