package ru.imagebook.server.exchange

data class SheetData(
        val key: String = "",
        val caption: String = "",
        val widthMm: Double = 0.0,
        val heightMm: Double = 0.0
) {
    companion object {
        val COVER_FRONT = "cover-front"
        val COVER_BACK = "cover-back"
        val COVER = "cover"

        fun PAGE(p: Int) = "page-$p"

        fun PAGES(p1: Int, p2: Int) = "pages-$p1-$p2"
    }
}