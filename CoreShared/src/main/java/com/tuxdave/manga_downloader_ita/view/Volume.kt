package com.tuxdave.manga_downloader_ita.view

import kotlinx.serialization.Serializable

@Serializable
data class Volume(val capitoli: List<Capitolo>, val numero: Int)