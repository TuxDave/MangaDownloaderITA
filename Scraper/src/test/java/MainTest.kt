import com.tuxdave.manga_downloader_ita.*
import com.tuxdave.manga_downloader_ita.export.exportPDF
import com.tuxdave.manga_downloader_ita.view.Raccolta
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun main() {
//    val mangas = search(
//        "mirai nikk",
//        SearchOrderParam.MOST_READ,
//        listeners = listOf(
//            PercentageListener("search", { percentage, id -> println("${percentage}%") })
//        )
//    )
//    openManga(mangas[0])
//    val rac = downloadManga(
//        mangas[0],
//        listeners = listOf(
//            PercentageListener("Manga", { p, id -> println("$id: $p%") })
//        ),
//        volumiListeners = listOf(
//            PercentageListener("Volume", { p, id -> println("\t$id: $p%") })
//        ),
//        capitoliListeners = listOf(
//            PercentageListener("Capitolo", { p, id -> println("\t\t$id: $p%") })
//        )
//    )

//    val writer = FileOutputStream(File(".miraiNikkiSerial.bin"))
//    writer.write(Cbor.encodeToByteArray(rac))
//    writer.close()

    val reader = FileInputStream(File(".miraiNikkiSerial.bin"))
    val rac: Raccolta = Cbor.decodeFromByteArray<Raccolta>(reader.readAllBytes())
    println(rac.elencoVolumi)
    exportPDF(
        rac,
        File("/home/tuxdave/Scrivania/miraiNikki.pdf")
    )
    reader.close()

//    println(Cbor.encodeToHexString(downloadManga(mangas[0]))) //TODO: verificare se funziona
}