package com.naulian.anhance

data class SelectableItem<T>(
    val item: T,
    val selected: Boolean = false
)

fun <T> List<T>.toSelectableItemList() = map {
    SelectableItem(item = it)
}

fun <T> List<SelectableItem<T>>.selectBy(
    selector: (T) -> Boolean,
) = map { it.copy(selected = selector(it.item)) }

fun <T> List<SelectableItem<T>>.selectAll(value: Boolean) = map { it.copy(selected = value) }

fun <T> List<SelectableItem<T>>.multiSelectBy(
    selector: (T) -> Boolean,
) = map {
    val selected = selector(it.item)
    it.copy(selected = it.selected xor selected)
}

fun main() {
    val list = listOf("apple", "banana", "cherry", "date")

    val selectableList = list.toSelectableItemList()
    println(selectableList)
}