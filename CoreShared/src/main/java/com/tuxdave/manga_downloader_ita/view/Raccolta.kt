package com.tuxdave.manga_downloader_ita.view

import com.tuxdave.manga_downloader_ita.entity.Manga

/**
 * classe mapper di Manga.kt ma che non presuppone che tutti i volumi siano scaricati
 */
class Raccolta(val manga: Manga, _vols: List<Volume>) {
     var volumi: List<Volume> = listOf()
         private set(value) {field = value}

    var elencoVolumi: List<Int> = listOf()
        private set(value) {field = value}

    init {
        volumi = _vols.sortedBy { it.numero }

        var temp = mutableListOf<Int>()
        for(volume in volumi){
            temp.add(volume.numero)
        }
        elencoVolumi = temp.toList()
    }
}