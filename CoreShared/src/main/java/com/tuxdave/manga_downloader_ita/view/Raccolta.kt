package com.tuxdave.manga_downloader_ita.view

import com.tuxdave.manga_downloader_ita.entity.Manga
import kotlinx.serialization.Serializable

/**
 * classe mapper di Manga.kt ma che non presuppone che tutti i volumi siano scaricati
 */
@Serializable
class Raccolta(
    val manga: Manga, private var _volumi: List<Volume>
) {

    var volumi: List<Volume> = listOf()
        private set(value) {field = value }

    var elencoVolumi: List<Int> = listOf()
        private set(value) {
            field = value
        }

    init {
        volumi = _volumi.sortedBy { it.numero }
        _volumi = listOf() //puliamo la memoria
        val temp = mutableListOf<Int>()
        for (volume in volumi) {
            temp.add(volume.numero)
        }
        elencoVolumi = temp.toList()
    }

}