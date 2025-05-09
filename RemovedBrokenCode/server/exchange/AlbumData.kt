package ru.imagebook.server.exchange

enum class AlbumState {
    Editing,

    Rendering,
    Rendered,
    RenderFailed;
}

data class AlbumData(
        var sheets: List<SheetData> = listOf(),
        val tags: List<String> = listOf()
)