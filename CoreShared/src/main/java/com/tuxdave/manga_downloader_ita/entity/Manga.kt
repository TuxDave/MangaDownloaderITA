package com.tuxdave.manga_downloader_ita.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URI

@Serializable
data class Manga(
    val titolo: String,
    @Serializable(with = URISerializer::class)
    val ref: URI?,
    val tipo: String,
    val stato: Stato,
    val autore: Autore?,
    val artista: Artista?,
    val generi: List<Genere>,
    val storia: String,
    @Serializable(with = URISerializer::class)
    val imgLink: URI? = null
){
    var volumiTotali: Int? = null
        private set(value) {field = value}
    var capitoliTotali: Int? = null
        private set(value) {field = value}

    var open: Boolean = false
        private set(value) {
            field = value
        }

    fun open(cap: Int, vol: Int): Unit {
        capitoliTotali = cap
        volumiTotali = vol
        open = true
    }
}

class URISerializer : KSerializer<URI> {
    override val descriptor: SerialDescriptor = Manga.serializer().descriptor

    override fun deserialize(decoder: Decoder): URI {
        return URI(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: URI) {
        encoder.encodeString(value.toString())
    }
}