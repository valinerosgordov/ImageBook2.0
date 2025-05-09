package ru.imagebook.server.exchange

enum class Tag(val value: String) {
    ALBUM("Альбом"),
    CALENDAR("Календарь"),
    HORIZONTAL("Горизонтальный"),
    VERTICAL("Вертикальный"),
    SQUARE("Квадратный"),
    QUARTER("Квартальный"),
    LOOSE_LEAF("Перекидной")
}