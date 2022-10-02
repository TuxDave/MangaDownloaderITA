package com.tuxdave.manga_downloader_ita.entity

import java.net.URI

data class Manga(
    val titolo: String,
    val ref: URI?,
    val tipo: String,
    val stato: Stato,
    val autore: Autore,
    val artista: Artista,
    val generi: List<Genere>,
    val storia: String,
    val imgLink: URI? = null
)