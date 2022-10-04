package com.tuxdave.manga_downloader_ita.exception

import com.tuxdave.manga_downloader_ita.entity.Manga

open class MangaException(message: String) : RuntimeException(message)

class VolumeOutOfRangeException(message: String) : MangaException(message)