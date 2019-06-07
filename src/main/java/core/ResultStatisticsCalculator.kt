package core

class ResultStatisticsCalculator(val source: ByteArray, val result: ByteArray) {

    fun getSavedBytes(): Int {
        return source.size - result.size
    }

    fun getSavedRatio(): Double? {
        return (1.0 - result.size / source.size.toDouble())
    }
}