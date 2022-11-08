package com.tuxdave.manga_downloader_ita.telegram_ui.sessions

import com.tuxdave.manga_downloader_ita.telegram_ui.MangaBot
import com.tuxdave.manga_downloader_ita.telegram_ui.messageDispatcher
import kotlin.coroutines.coroutineContext

abstract class Session(_user: Long, protected val bot: MangaBot) {
    protected val user: Long
    private var step = -1

    init {
        user = _user
    }

    abstract val actions: List<Pair<String, (msg: String) -> Unit>>

    fun next(): Unit {
        if(step + 1 >= actions.size){
            stop()
        }
        bot.send(user, actions[++step].first)
    }

    fun start(): Unit{
        step = -1
        next()
    }

    private fun stop() {
        messageDispatcher.sessions.remove(this)
    }

    fun receive(response: String){
        if(step < actions.size)
        actions[step].second(response)
        next()
    }

    override fun equals(other: Any?): Boolean {
        return if(other is Long){
            user == other
        }else{
            super.equals(other)
        }
    }
}