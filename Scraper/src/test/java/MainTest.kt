import com.tuxdave.manga_downloader_ita.*
import com.tuxdave.manga_downloader_ita.export.exportPDF
import java.io.File
import java.time.LocalDateTime

fun main() {
    println(LocalDateTime.now())
    val mangas = search(
        "mirai nikk",
        SearchOrderParam.MOST_READ,
        listeners = listOf(
            PercentageListener("search", { percentage, id -> println("${percentage}%") })
        )
    )
    openManga(mangas[0])
    val rac = downloadManga(
        mangas[0],
        listeners = listOf(
            PercentageListener("Manga", { p, id -> println("$id: $p%") })
        ),
        volumiListeners = listOf(
            PercentageListener("Volume", { p, id -> println("\t$id: $p%") })
        ),
        capitoliListeners = listOf(
            PercentageListener("Capitolo", { p, id -> println("\t\t$id: $p%") })
        )
    )

    println(LocalDateTime.now())
    exportPDF(rac, File("out.pdf"))
    println(LocalDateTime.now())
}