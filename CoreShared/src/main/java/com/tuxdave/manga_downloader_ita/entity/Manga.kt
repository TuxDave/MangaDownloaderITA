package com.tuxdave.manga_downloader_ita.entity

import java.net.URL

class Manga(
    val titolo: String,
    val tipo: String,
    val stato: Boolean,
    val autore: Autore,
    val artista: Artista,
    val generi: List<Genere>,
    val storia: String,
    val imgLink: URL? = null
)