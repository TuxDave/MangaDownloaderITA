package com.tuxdave.manga_downloader_ita.scraper

val SITE_BASE = "https://www.mangaworld.in/"

open class PercentageListener(
    val id: String = "",
    val activationFunction: (percentage: Int, id: String) -> Unit
){
    fun pock(perc: Int): Unit {
        activationFunction(perc, id)
    }
}

/**
 * che figata le extensions functions
 */
fun List<PercentageListener>.pock(perc: Int): Unit{
    for(l in this){
        l.pock(perc)
    }
}