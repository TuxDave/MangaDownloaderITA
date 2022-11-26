package com.tuxdave.manga_downloader_ita.desktop_ui.components

import java.awt.Color
import javax.swing.JLabel

class JBlinkingLabel(_defaultOpacity: Int) : JLabel() {
    var blinking = false
        set(value) {
            field = value
            blink()
        }

    /**Millisecondi*/
    var duration: Long = 1000
    var defaultOpacity: Int = 1000
        set(value) {
            field = minOf(255, maxOf(0, value))
        }

    private lateinit var blinker: Thread

    init {
        defaultOpacity = _defaultOpacity
        blink()
    }

    fun blink(): Unit {
        if (blinking) {
            blinker = Thread() {
                val colorOn = Color(foreground.red, foreground.green, foreground.blue, 255)
                val colorOff = Color(foreground.red, foreground.green, foreground.blue, 0)
                while (true) {
                    for (i in 0..255) {
                        foreground = Color(foreground.red, foreground.green, foreground.blue, i)
                        try{
                            Thread.sleep(duration / 510)
                        }catch (e: InterruptedException){}
                    }
                    for (i in 255 downTo 0) {
                        foreground = Color(foreground.red, foreground.green, foreground.blue, i)
                        try{
                            Thread.sleep(duration / 510)
                        }catch (e: InterruptedException){}
                    }
                }
            }
            blinker.start()
        } else {
            try{
                blinker.stop()
            }catch (e: UninitializedPropertyAccessException){}
            foreground = Color(foreground.red, foreground.green, foreground.blue, defaultOpacity)
        }
    }
}