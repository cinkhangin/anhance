package com.naulian.anhance

object AnText {
    private const val lowerCases = "abcdefghijklmnopqrstuvwxyz"
    private const val upperCases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val decimals = "0123456789"
    private const val symbols = "~`!@#\$%^&*()_-+={[}]|\\:;\"'<,>.?/"

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
            if (word in text) {
                val star = stars[word.length] ?: "***"
                output = output.replace(word, star)
            }
        }
        return output
    }

    fun generateRandomString(example: String): String {
        if (example.isEmpty()) return "ooh!oh!"

        val source = loopForString(example) { char ->
            when (char) {
                in lowerCases -> lowerCases
                in upperCases -> upperCases
                in decimals -> decimals
                in symbols -> symbols
                else -> ""
            }
        }

        val output = loopForString(example.length) {
            val rndIndex = source.length.random
            source.elementAt(rndIndex).toString()
        }

        return trim(output)
    }

    fun addMorePrefix(string: String, prefix : Char , desireLength : Int) : String{
        if(string.length >= desireLength) return string

        val requiredCount = desireLength - string.length
        var output = string
        repeat(requiredCount){
            output = "$prefix$output"
        }
        return output
    }

}