package com.tuxdave.manga_downloader_ita.desktop_ui

import com.tuxdave.manga_downloader_ita.core_shared.entity.Manga
import java.util.*

fun sortByCorrispondenza(source: List<Manga>, searchCriteria: String): List<Manga>{
    val source = source.toMutableList()
    source.sortWith(kotlin.Comparator { t, t2 ->
        (t2.titolo somiglianza searchCriteria) - (t.titolo somiglianza searchCriteria)
    })
    return source.toList()
}

private infix fun String.somiglianza(criteria: String): Int{
    var ret = 0
    val criteria = criteria.lowercase(Locale.getDefault())
    val self = this.lowercase(Locale.getDefault())
    for(c in 0 until Math.min(criteria.length, length)){
        if(self[c] == criteria[c]){
            ret++
        }else{
            break
        }
    }
    return ret
}

private fun <E> MutableList<E>.switch(x: Int, y: Int): Unit {
    val temp: E = this[x]
    this[x] = this[y]
    this[y] = temp
}