package com.tuxdave.manga_downloader_ita.core_shared.export

import com.tuxdave.manga_downloader_ita.core_shared.view.Raccolta
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.ByteArrayInputStream
import java.io.File

fun PDPageContentStream.print(s: String, x: Int, y: Int, h: Float) {
    beginText()
    newLineAtOffset(x.toFloat(), h - y.toFloat())
    showText(s)
    endText()
}

fun exportPDF(manga: Raccolta, file: File) {

    file.createNewFile()

    val doc = PDDocument()
    var page = PDPage()
    doc.addPage(page)
    val h = page.mediaBox.height

    var cs = PDPageContentStream(doc, page)
    cs.setFont(PDType1Font.COURIER, 28F)
    cs.print("Manga:       " + manga.manga.titolo, 25, 50, h)
    cs.setFont(PDType1Font.COURIER, 22F)
    cs.print("Autore:           " + manga.manga.autore?.nome, 25, 75, h)
    cs.print("Artista:          " + manga.manga.artista?.nome, 25, 100, h)
    cs.setFont(PDType1Font.COURIER, 16F)
    cs.print("FONTE: https://github.com/TuxDave/MangaDownloaderITA", 100, -50, 0F)
    var s = ""
    for (vol in manga.elencoVolumi) {
        s += "$vol°, "
    }
    s = try {
        s.substring(0, s.length - 2)
    } catch (_: Exception) {
        "NONE"
    }
    cs.print("Volumi Scaricati: " + s, 25, 125, h)
    cs.close()

    for (volume in manga.volumi) {
        page = PDPage()
        cs = PDPageContentStream(doc, page)
        cs.setFont(PDType1Font.COURIER, 28F)
        cs.print("Volume: ${volume.numero}", 25, 50, page.mediaBox.height)
        cs.close()
        doc.addPage(page)

        for (capitolo in volume.capitoli) {
            page = PDPage()
            cs = PDPageContentStream(doc, page)
            cs.setFont(PDType1Font.COURIER, 24F)
            cs.print("Capitolo: ${capitolo.numero}", 25, 100, page.mediaBox.height)
            cs.close()
            doc.addPage(page)

            var image: PDImageXObject
            for (pagina in capitolo.pagine) {
                page = PDPage()
                cs = PDPageContentStream(doc, page)
                try {
                    image = JPEGFactory.createFromStream(doc, ByteArrayInputStream(pagina))
                    cs.drawImage(image, 0F, 0F, page.mediaBox.width, page.mediaBox.height)
                } catch (_: Exception) {
                    cs.setFont(PDType1Font.COURIER, 24F)
                    cs.print("PAGINA NON TROVATA", 30, 200, page.mediaBox.height)
                }
                cs.close()
                doc.addPage(page)
            }
        }
    }

    doc.save(file)
    doc.close()
}

//https://www.tutorialspoint.com/pdfbox/pdfbox_adding_text.htm