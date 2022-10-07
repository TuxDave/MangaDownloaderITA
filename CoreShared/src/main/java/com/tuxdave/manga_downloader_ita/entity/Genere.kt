package com.tuxdave.manga_downloader_ita.entity

import kotlinx.serialization.Serializable
import java.net.URI
import java.net.URL

@Serializable
data class Genere(
    val nome: String,
    @Serializable(with = URISerializer::class)
    val ref: URI?
){
    companion object{
        //piccola memoization
        private val generi = mutableListOf<Genere>()
        fun getGenere(genere: Genere): Genere{
            if(genere in generi){
                for(g in generi){
                    if(g == genere) return g
                }
                return genere
            }else{
                generi.add(genere)
                return genere
            }
        }
    }
}