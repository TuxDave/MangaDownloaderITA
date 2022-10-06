package com.tuxdave.manga_downloader_ita.view

import kotlinx.serialization.Serializable

@Serializable
data class Capitolo(
    val pagine: List<ByteArray>,
    val numero: Int
) : java.io.Serializable