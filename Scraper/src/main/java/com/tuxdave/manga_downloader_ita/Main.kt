package com.tuxdave.manga_downloader_ita

import java.net.URI

fun main() {
    val mangas = search(
        "mirai nikk",
        SearchOrderParam.MOST_READ,
        listeners = listOf(
            PercentageListener("search", { percentage, id -> println("${percentage}%") })
        )
    )
    openManga(mangas[0])
    downloadManga(
        mangas[0],
        listeners = listOf(
            PercentageListener("Manga", { p, id -> println("$id: $p%")})
        ),
        volumiListeners = listOf(
            PercentageListener("Volume", { p, id -> println("\t$id: $p%")})
        ),
        capitoliListeners = listOf(
            PercentageListener("Capitolo", { p, id -> println("\t\t$id: $p%")})
        )
    )
}