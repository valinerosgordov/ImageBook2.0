package ru.imagebook.server.exchange

import com.minogin.graphics.BufferedImages
import com.minogin.graphics.save
import com.minogin.graphics.subimage
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.postForObject
import ru.imagebook.server.exchange.SheetData.Companion.COVER
import ru.imagebook.server.exchange.SheetData.Companion.COVER_BACK
import ru.imagebook.server.exchange.SheetData.Companion.COVER_FRONT
import ru.imagebook.server.exchange.SheetData.Companion.PAGE
import ru.imagebook.server.exchange.SheetData.Companion.PAGES
import ru.imagebook.server.repository.OrderRepository
import ru.imagebook.server.repository.ProductRepository
import ru.imagebook.server.service.auth.AuthService
import ru.imagebook.server.service.flash.FlashConfig
import ru.imagebook.server.service.flash.FlashPath
import ru.imagebook.server.service.render.RenderUtil.mmToPx
import ru.imagebook.shared.model.Album
import ru.imagebook.shared.model.AlbumOrder
import ru.imagebook.shared.model.OrderState
import ru.imagebook.shared.model.ProductType
import ru.imagebook.shared.model.User
import ru.minogin.core.client.i18n.locale.Locales
import ru.minogin.core.server.ftp.XFtpClient
import java.awt.image.BufferedImage
import java.io.File

const val MARGIN_MM = 5

interface IExchangeService {
    fun getAuthToken(user: User): String

    fun createAlbum(productId: Int, pageCount: Int, user: User): String

    fun copyAlbum(albumId: String): String

    fun deleteAlbum(albumId: String)

    fun renderAlbum(albumId: String)

    fun editAlbum(albumId: String)

    fun getAlbumState(albumId: String): AlbumState

    fun getAlbum(albumId: String): AlbumData

    fun loadRenderedAlbums()

    fun addSheets(albumId: String): AlbumData

    fun deleteSheets(albumId: String): AlbumData
}

@Service
class ExchangeService(
        val authService: AuthService,
        val productRepository: ProductRepository,
        val orderRepository: OrderRepository,
        val config: ExchangeConfig,
        flashConfig: FlashConfig
) : IExchangeService {
    val flashPath = FlashPath(flashConfig)

    data class CreateAlbumPayload(
            val user: UserData,
            val album: AlbumData
    )

    override fun getAuthToken(user: User): String =
            get("${config.url}/auth/token/${user.userName}")!!

    fun pageSizeWithoutMarginMm(album: Album) = Pair(album.blockWidth.toDouble(), album.blockHeight.toDouble())

    fun pageSizeWithMarginPx(album: Album) = Pair(
            mmToPx((album.blockWidth.toDouble() + MARGIN_MM * 2).toFloat()),
            mmToPx((album.blockHeight.toDouble() + MARGIN_MM * 2).toFloat()))

    private fun pageCount(nSheets: Int, product: Album) = when {
        isCalendar(product) -> nSheets
        product.isHardOrEverflat -> (nSheets - 2) * 2
        else -> (nSheets - 1) * 2
    }

    override fun createAlbum(productId: Int, pageCount: Int, user: User): String {
        val userModel = UserData(user.userName,
                when {
                    user.isPhotographer -> listOf(RoleData.USER, RoleData.PHOTOGRAPHER)
                    else -> listOf(RoleData.USER)
                }
        )

        val album = productRepository.getProduct(productId) as Album

        val sheets = generateSheets(album, pageCount)

        val tags = sequence {
            when {
                isCalendar(album) -> {
                    yield(Tag.CALENDAR)

                    when {
                        album.name[Locales.RU].contains("квартальный") -> yield(Tag.QUARTER)
                        album.name[Locales.RU].contains("перекидной") -> {
                            yield(Tag.LOOSE_LEAF)

                            when {
                                album.blockWidth > album.blockHeight -> yield(Tag.HORIZONTAL)
                                album.blockWidth < album.blockHeight -> yield(Tag.VERTICAL)
                            }
                        }
                    }
                }
                else -> {
                    yield(Tag.ALBUM)

                    when {
                        album.blockWidth > album.blockHeight -> yield(Tag.HORIZONTAL)
                        album.blockWidth < album.blockHeight -> yield(Tag.VERTICAL)
                        album.blockWidth == album.blockHeight -> yield(Tag.SQUARE)
                    }
                }
            }
        }

        val albumData = AlbumData(sheets.toList(), tags.map { it.value }.toList())

        return post("${config.url}/albums/create", CreateAlbumPayload(userModel, albumData))!!
    }

    private fun generateSheets(album: Album, pageCount: Int): Sequence<SheetData> {
        val (pageWidthMm, pageHeightMm) = pageSizeWithoutMarginMm(album)

        return sequence {
            when {
                isCalendar(album) -> {
                    yield(SheetData(
                            key = COVER_FRONT,
                            caption = "Обложка",
                            widthMm = pageWidthMm,
                            heightMm = pageHeightMm
                    ))

                    repeat(pageCount - 2) { i ->
                        val p = i + 1
                        yield(SheetData(
                                key = PAGE(p),
                                caption = "Страница $p",
                                widthMm = pageWidthMm,
                                heightMm = pageHeightMm
                        ))
                    }

                    yield(SheetData(
                            key = COVER_BACK,
                            caption = "Подложка",
                            widthMm = pageWidthMm,
                            heightMm = pageHeightMm
                    ))
                }

                album.isHardOrEverflat -> {
                    yield(SheetData(
                            key = COVER,
                            caption = "Обложка",
                            widthMm = album.coverWidth.toDouble(),
                            heightMm = album.coverHeight.toDouble()
                    )) // TODO margin?

                    yield(SheetData(
                            key = PAGE(1),
                            caption = "Страница 1",
                            widthMm = pageWidthMm,
                            heightMm = pageHeightMm
                    ))

                    repeat((pageCount - 2) / 2) { i ->
                        val p1 = i * 2 + 2
                        val p2 = i * 2 + 3
                        yield(SheetData(
                                key = PAGES(p1, p2),
                                caption = "Страницы $p1 - $p2",
                                widthMm = pageWidthMm * 2,
                                heightMm = pageHeightMm
                        ))
                    }

                    yield(SheetData(
                            key = PAGE(pageCount),
                            caption = "Страница $pageCount",
                            widthMm = pageWidthMm,
                            heightMm = pageHeightMm))
                }

                else -> {
                    yield(SheetData(
                            key = COVER_FRONT,
                            caption = "Обложка",
                            widthMm = pageWidthMm,
                            heightMm = pageHeightMm))

                    repeat((pageCount - 2) / 2) { i ->
                        val p1 = i * 2 + 2
                        val p2 = i * 2 + 3
                        yield(SheetData(
                                key = PAGES(p1, p2),
                                caption = "Страницы $p1 - $p2",
                                widthMm = pageWidthMm * 2,
                                heightMm = pageHeightMm
                        ))
                    }

                    yield(SheetData(
                            key = COVER_BACK,
                            caption = "Оборот обложки",
                            widthMm = pageWidthMm,
                            heightMm = pageHeightMm))
                }
            }
        }
    }

    private fun isCalendar(album: Album) = album.name[Locales.RU].contains("календарь", true)

    override fun copyAlbum(albumId: String): String =
        post("${config.url}/albums/$albumId/copy")!!

    override fun deleteAlbum(albumId: String) {
        post<Unit>("${config.url}/albums/$albumId/delete")
    }

    override fun renderAlbum(albumId: String) {
        post<Unit>("${config.url}/albums/$albumId/render")
    }

    override fun editAlbum(albumId: String) {
        put<Unit>("${config.url}/albums/$albumId/edit")
    }

    override fun getAlbumState(albumId: String) =
            get<AlbumState>("${config.url}/albums/$albumId/state")!!

    override fun getAlbum(albumId: String) =
            get<AlbumData>("${config.url}/albums/$albumId")!!

    @Transactional
    override fun loadRenderedAlbums() {
        orderRepository
                .findOrdersByState(OrderState.JPEG_BOOK_GENERATION)
                .forEach { order ->
                    when (getAlbumState(order.albumId)) {
                        // TODO: parallel
                        AlbumState.Rendered -> {
                            updateOrder(order as AlbumOrder)
                            loadRender(order.albumId)
                            convertRender(order)
                            deleteRender(order)
                            order.state = OrderState.FLASH_GENERATION

                            // TODO: change album state?
                        }

                        AlbumState.RenderFailed -> order.state = OrderState.JPEG_GENERATION_ERROR

                        // TODO: illegal state?
                    }
                }
    }

    override fun addSheets(albumId: String): AlbumData {
        val albumData = getAlbum(albumId)
        val order = orderRepository.findOrderbyAlbumId(albumId)
        val album = order.product

        if (isCalendar(album))
            return albumData

        val pageCount = pageCount(albumData.sheets.size, album)
        if (pageCount >= album.maxPageCount)
            return albumData

        val pagesToAdd = album.multiplicity.coerceAtLeast(2)
        albumData.sheets = generateSheets(album, pageCount + pagesToAdd).toList()
        return albumData
    }

    override fun deleteSheets(albumId: String): AlbumData {
        val albumData = getAlbum(albumId)
        val order = orderRepository.findOrderbyAlbumId(albumId)
        val album = order.product

        if (isCalendar(album))
            return albumData

        val pageCount = pageCount(albumData.sheets.size, album)
        if (pageCount <= album.minPageCount)
            return albumData

        val pagesToRemove = album.multiplicity.coerceAtLeast(2)
        albumData.sheets = generateSheets(album, pageCount - pagesToRemove).toList()
        return albumData
    }

    private fun updateOrder(order: AlbumOrder) {
        val albumData = getAlbum(order.albumId)
        order.pageCount = pageCount(albumData.sheets.size, order.product)
    }

    private fun loadRender(albumId: String) {
        val client = XFtpClient()
        try {
            client.connect(config.renderHost, config.renderUser, config.renderPassword)
            client.cd(albumId)

            File(albumRenderPath(albumId)).deleteRecursively()
            File(albumRenderPath(albumId)).mkdirs()

            client.listFiles().forEach { file ->
                client.loadFile(file.name, sheetRenderPath(albumId, file.name))
            }
        } finally {
            client.disconnect();
        }
    }

    private fun convertRender(order: AlbumOrder) {
        val album = order.product as Album
        val (pageWidthPx, pageHeightPx) = pageSizeWithMarginPx(album)

        File(flashPath.getJpegDir(order)).run {
            deleteRecursively()
            mkdirs()
        }

        var sheet = 0

        if (album.isHardOrEverflat) {
            File(sheetRenderPath(order.albumId, "$sheet.jpg")).copyTo(File(jpegPath(order, "c.jpg")))
            sheet++
        }

        var page = 1
        var pageImage = BufferedImages.load(sheetRenderPath(order.albumId, "$sheet.jpg"))
        pageImage = postProcess(pageImage, album, page % 2 == 1)
        pageImage.save(jpegPath(order, "$page.jpg"))
        page++
        sheet++

        while (page < order.pageCount) {
            val sheetImage = BufferedImages.load(sheetRenderPath(order.albumId, "$sheet.jpg"))

            pageImage = sheetImage.subimage(0, 0, pageWidthPx, pageHeightPx)
            pageImage = postProcess(pageImage, album, page % 2 == 1)
            pageImage.save(jpegPath(order, "$page.jpg"))
            page++

            if (!isCalendar(album)) {
                pageImage = sheetImage.subimage(sheetImage.width - pageWidthPx, 0, pageWidthPx, pageHeightPx)
                pageImage = postProcess(pageImage, album, page % 2 == 1)
                pageImage.save(jpegPath(order, "$page.jpg"))
                page++
            }

            sheet++
        }

        pageImage = BufferedImages.load(sheetRenderPath(order.albumId, "$sheet.jpg"))
        pageImage = postProcess(pageImage, album, page % 2 == 1)
        pageImage.save(jpegPath(order, "$page.jpg"))
    }

    private fun postProcess(src: BufferedImage, album: Album, odd: Boolean): BufferedImage {
        val srcWidth: Float = src.getWidth().toFloat()
        val srcHeight: Float = src.getHeight().toFloat()
        val type: Int = album.getType()
        var width: Int = srcWidth.toInt()
        var shift = 0
        if ((type == ProductType.HARD_COVER_FULL_PRINT)
                || (type == ProductType.TABLET)
                || (type == ProductType.HARD_COVER_WHITE_MARGINS)){
            shift = mmToPx(10f)
            width += shift
        } else if (type == ProductType.EVERFLAT_FULL_PRINT || type == ProductType.EVERFLAT_WHITE_MARGINS) {
            shift = mmToPx(2f)
            width += shift
        }
        val height: Int = srcHeight.toInt()

        // Preserve image type of original image
        val image = BufferedImage(width, height, src.getType())
        val graphics: java.awt.Graphics = image.getGraphics()
        graphics.setColor(java.awt.Color.WHITE)
        if ((type == ProductType.HARD_COVER_FULL_PRINT)
                || (type == ProductType.TABLET)
                || (type == ProductType.HARD_COVER_WHITE_MARGINS)){
            val margin: Int = mmToPx(10f)
            if (odd){
                graphics.drawImage(src, shift, 0, null)
                graphics.fillRect(0, 0, margin, height)
            } else {
                graphics.drawImage(src, 0, 0, null)
                graphics.fillRect(width - margin, 0, width, height)
            }
            return image
        } else if (type == ProductType.EVERFLAT_FULL_PRINT  || type == ProductType.EVERFLAT_WHITE_MARGINS) {
            val margin: Int = mmToPx(7f)
            if (odd){
                graphics.drawImage(src, shift, 0, null)
                graphics.fillRect(0, 0, margin, height)
            } else {
                graphics.drawImage(src, 0, 0, null)
                graphics.fillRect(width - margin, 0, width, height)
            }
            return image
        } else {
            return src
        }
    }

    private fun deleteRender(order: AlbumOrder) {
        File(albumRenderPath(order.albumId)).deleteRecursively()
    }

    private fun albumRenderPath(albumId: String) =
            "${config.renderPath}/$albumId"

    private fun sheetRenderPath(albumId: String, sheetFileName: String) =
            "${albumRenderPath(albumId)}/$sheetFileName"

    private fun jpegPath(order: AlbumOrder, fileName: String) =
            "${flashPath.getJpegDir(order)}/$fileName"

    private inline fun <reified T : Any> get(url: String): T? =
            RestTemplate().exchange<T>(url, HttpMethod.GET, HttpEntity<Any>(authHeaders()))?.body

    private inline fun <reified T : Any> post(url: String, data: Any? = null): T? =
            RestTemplate().postForObject(url, HttpEntity(data, authHeaders()))!!

    private inline fun <reified T : Any> post(url: String): T? =
            RestTemplate().exchange<T>(url, HttpMethod.POST, HttpEntity<Any>(authHeaders()))?.body

    private inline fun <reified T : Any> put(url: String): T? =
            RestTemplate().exchange<T>(url, HttpMethod.PUT, HttpEntity<Any>(authHeaders()))?.body

    private fun authHeaders() =
            HttpHeaders().apply {
                set(HttpHeaders.AUTHORIZATION, "Bearer ${config.token}")
            }
}
