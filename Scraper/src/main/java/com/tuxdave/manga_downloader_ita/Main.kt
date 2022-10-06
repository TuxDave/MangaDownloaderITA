package com.tuxdave.manga_downloader_ita

import com.tuxdave.manga_downloader_ita.view.Capitolo
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToHexString

fun main() {
    println(Cbor.encodeToHexString(Capitolo(listOf(), 1)))
}