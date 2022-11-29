package com.tuxdave.manga_downloader_ita.desktop_ui.components

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import javax.swing.JTextField

class JSearchField : JTextField(), MouseMotionListener {
    var placeHolder = ""

    init {
        val self = this

        minimumSize = Dimension(-1, 32)
        preferredSize = minimumSize
        maximumSize = minimumSize

        addMouseMotionListener(this)
        addMouseListener(
            object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    super.mouseClicked(e)
                    if (e!!.x > width - (img.getWidth(self) + 5)) {
                        actionListeners.forEach { actionListener ->
                            actionListener.actionPerformed(
                                ActionEvent(
                                    self,
                                    0,
                                    ""
                                )
                            )
                        }
                    }
                }
            }
        )
    }

    private val img = Toolkit.getDefaultToolkit().getImage(
        javaClass.getResource(
            "/com/tuxdave/manga_downloader_ita/desktop_ui/assets/imgs/search_icon.png"
        )
    )

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.font = font
        g.color = Color.GRAY
        if (text == "") {
            g.drawString(placeHolder, 5, height - (height / 2 - 5))
        }
        g.drawImage(
            img,
            width - (img.getWidth(this) + 5),
            5,
            this
        )
    }

    override fun mouseDragged(p0: MouseEvent?) {}

    private var handIn: Boolean = false
    override fun mouseMoved(p: MouseEvent?) {
        if (p!!.x > width - (img.getWidth(this) + 5)) {
            if (!handIn) {
                handIn = true
                cursor = Cursor(Cursor.HAND_CURSOR)
            }
        } else {
            if (handIn) {
                handIn = false
                cursor = Cursor.getDefaultCursor()
            }
        }
    }
}