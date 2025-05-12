package com.naulian.anhance

fun String.paragraph(): String {
    return trimIndent().replace("\n", " ")
}

object Lorem {
    @Suppress("MayBeConstant", "RedundantSuppression")
    val short = "Lorem ipsum dolor sit amet"

    val normal = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut
        labore et dolore magna aliqua.
    """.paragraph()

    val medium = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut
        labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
        laboris nisi ut aliquip ex ea commodo consequat.
    """.paragraph()

    val long = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut
        labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
        laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in
        voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat
        cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum
    """.paragraph()

    val full = """
        |$normal
        |$long
        |$medium
    """.trimMargin()
}

fun main() {
    println(Lorem.short)
    println(Lorem.normal)
    println(Lorem.medium)
    println(Lorem.long)
    println(Lorem.full)
}