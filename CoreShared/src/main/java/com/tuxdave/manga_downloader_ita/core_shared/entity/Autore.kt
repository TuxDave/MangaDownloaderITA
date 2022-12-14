package com.tuxdave.manga_downloader_ita.core_shared.entity

import kotlinx.serialization.Serializable
import java.net.URI

@Serializable
data class Autore(
    val nome: String,
    @Serializable(with = URISerializer::class)
    val ref: URI?
    )