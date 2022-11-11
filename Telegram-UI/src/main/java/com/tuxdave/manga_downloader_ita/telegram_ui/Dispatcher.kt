package com.tuxdave.manga_downloader_ita.telegram_ui

import com.tuxdave.manga_downloader_ita.telegram_ui.sessions.DownloadSession
import com.tuxdave.manga_downloader_ita.telegram_ui.sessions.InfoSession
import com.tuxdave.manga_downloader_ita.telegram_ui.sessions.SearchSession
import com.tuxdave.manga_downloader_ita.telegram_ui.sessions.Session
import org.telegram.telegrambots.meta.api.objects.Message

object messageDispatcher {

    private val COMMANDS = listOf<String>(
        "search",
        "download",
        "info"
    )
    private fun CONSTRUCTORS(command: String, chatId: Long) = when(command) {
        "search" -> SearchSession(chatId, bot!!)
        "info" -> InfoSession(chatId, bot!!)
        "download" -> DownloadSession(chatId, bot!!)
        else -> SearchSession(chatId, bot!!)
    }

    internal val sessions = mutableListOf<Session>()

    var bot: MangaBot? = null

    fun dispatch(msg: Message) {
        if(msg.text.startsWith("/")){
            launchCommand(msg.text, msg.chatId)
        }else{
            for(session in sessions){
                if(session.equals(msg.chatId)){
                    session.receive(msg.text)
                }
                return
            }
            bot?.send(msg.chatId, "Comando non riconosciuto...")
        }
    }

    private fun launchCommand(command: String, chatId: Long): Unit {
        val command = command.substring(1)
        if(command !in COMMANDS){
            bot?.send(chatId, "Comando non riconosciuto!")
        }else{
            for(session in sessions){
                if(session.equals(chatId)){
                    sessions.remove(session)
                    break;
                }
            }
            val s = CONSTRUCTORS(command, chatId)
            s.start()
            sessions.add(s)
        }
    }
}