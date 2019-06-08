package com.fewlaps.slimjpg.core

data class Result(
        val picture: ByteArray,
        val elapsedTime: Long,
        val savedBytes: Long,
        val savedRatio: Double,
        val jpegQualityUsed: Int)