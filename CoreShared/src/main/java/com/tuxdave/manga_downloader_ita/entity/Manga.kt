package com.tuxdave.manga_downloader_ita.entity

import java.net.URI

data class Manga(
    val titolo: String,
    val ref: URI?,
    val tipo: String,
    val stato: Stato,
    val autore: Autore?,
    val artista: Artista?,
    val generi: List<Genere>,
    val storia: String,
    val imgLink: URI? = null
){
    var volumiTotali: Int? = null
        private set(value) {field = value}
    var capitoliTotali: Int? = null
        private set(value) {field = value}

    var open: Boolean = false
        private set(value) { field = value }

    fun open(cap: Int, vol: Int): Unit {
        capitoliTotali = cap
        volumiTotali = vol
        open = true
    }
}