package com.tuxdave.manga_downloader_ita.telegram_ui

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

class MangaBot(private val tk: String) : TelegramLongPollingBot() {
    override fun getBotToken(): String {
        return tk
    }

    override fun getBotUsername(): String {
        return "MangaDownloader_ITA_BOT"
    }

    override fun onUpdateReceived(data: Update?) {
        if (data == null) return
        if (data.message.isUserMessage) {
            TODO("gestire il dispatch dei messaggi")
        } else {
            execute(SendMessage(data.message.chatId.toString(), "Scrivimi direttamente se hai coraggio..."))
        }
    }

}