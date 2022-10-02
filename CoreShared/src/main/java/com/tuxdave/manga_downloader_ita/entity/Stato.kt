package com.tuxdave.manga_downloader_ita.entity

enum class Stato(value: Boolean) {
    FINITO(true),
    IN_CORSO(false);

    companion object{
        fun get(value: String): Stato{
            return if (value != "In corso") IN_CORSO else FINITO
        }
    }
}