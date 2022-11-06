package com.tuxdave.manga_downloader_ita.core_shared.exception

open class MangaException(message: String) : RuntimeException(message)

class VolumeOutOfRangeException(message: String) : MangaException(message)