package com.naulian.anhance.objects

object AnText {
    private val stars = hashMapOf(
        3 to "***",
        4 to "****",
        5 to "*****",
        6 to "******",
        7 to "*******",
        8 to "********",
        9 to "*********",
        10 to "**********",
    )
    private val words = arrayListOf(
        "fuck",
        "sex",
        "porn"
    )

    fun censor(text: String): String {
        var output = text
        words.forEach { word ->
            if (text.contains(word, true)) {
                val star = stars[word.length] ?: "***"
                output = output.replace(word, star)
            }
        }
        return output
    }

}