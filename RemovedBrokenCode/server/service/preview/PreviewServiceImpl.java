package ru.imagebook.server.service.preview;

import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.FlashRepository;
import ru.imagebook.server.service.SecurityService;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.flash.FlashService;
import ru.imagebook.server.service.pickbook.PickbookClient;
import ru.imagebook.server.service.site.PageNotFoundError;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;
import ru.imagebook.shared.model.common.preview.PreviewUtils;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.pageflip.server.PageFlip;


@Service("previewService")
public class PreviewServiceImpl implements PreviewService {
    private static final int EXT_FLASH_USER_ID = 15;
    private static final String PREVIEW_TITLE_TEMPLATE = "Заказ № %s";

    @Autowired
    private FlashService flashService;

    @Autowired
    private FlashRepository flashRepository;

    @Autowired
    private PickbookClient pickbookClient;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Override
    public String renderOrderFlipper(Order<?> order, Vendor vendor, AlbumPreviewParams previewParams) {
        PageFlip flip = new PageFlip("album", previewParams.getPageWidth(), previewParams.getPageHeight(),
            previewParams.getnPages(), previewParams.isHardOrEverflat(),
            "http://" + vendor.getOnlineEditorUrl() + "/app/albumSheetPreviewImage?albumId=" + order.getImportId()
                + "&i=${page}");
        return flip.render();
    }

    @Override
    public String renderOrderFlipperByPublishedCode(int code, Vendor vendor, AlbumPreviewParams previewParams) {
        PageFlip flip = new PageFlip("album", previewParams.getPageWidth(), previewParams.getPageHeight(),
            previewParams.getnPages(), previewParams.isHardOrEverflat(),
            "http://" + vendor.getOnlineEditorUrl() + "/app/albumPageImage?code=" + code + "&i=${page}");
        return flip.render();
    }

    @Override
    public String renderOrderFlashFlipper(Order<?> order, Vendor vendor, FlashParams flashParams) {
        String imageUrlPattern = "http://127.0.0.1:8888/flash/image?a=${a}&b=${b}&c=${c}&d=${d}";
        // TODO id = album_ + albumId;
        PageFlip flip = new PageFlip("album", flashParams.getFlashWidth()/2-20, flashParams.getFlashHeight()-79,
            flashParams.getnPages(), false, imageUrlPattern);
        return flip.render();
    }

    @Transactional
    @Override
    public void showOrderPreview(int orderId, int userId, Writer writer) {
        User user = userService.getUser(userId);
        Order<?> order = flashRepository.getOrder(orderId);
        if (order == null) {
            throw new PageNotFoundError();
        }
        checkAccess(order, user);

        showOrderPreview(writer, order, false);
    }

    private void checkAccess(Order<?> order, User user) {
        if (securityService.hasRole(user, Role.OPERATOR)
                || securityService.hasRole(user, Role.FINISHING_MANAGER))
            return;

        if (!order.getUser().equals(user)) {
            throw new AccessDeniedError();
        }
    }

    @Transactional
    @Override
    public void showOrderPreviewExt(String number, Writer writer) {
        Order<?> order = flashRepository.findOrder(number);
        if (order == null) {
            throw new PageNotFoundError();
        }
        // TODO checkAccess(order, user); ???

        showOrderPreview(writer, order, true);
    }

    private void showOrderPreview(Writer writer, Order<?> order, boolean isExternal) {
//        if (order.isExternalOrder()) {
////            AlbumPreviewParams previewParams = pickbookClient.previewAlbum(order.getImportId());
//
//            AlbumPreviewParams previewParams = new AlbumPreviewParams();
//            previewParams.setPageHeight(328);
//            previewParams.setPageWidth(480);
//            previewParams.setnPages(28);
//            previewParams.setHardOrEverflat(true);
//
//            showOrderFlipper(order, previewParams, writer, isExternal);
//        } else {
        showOrderFlash(order, writer, isExternal);
//        }
    }

    // todo remove
    private void showOrderFlipper(Order<?> order, AlbumPreviewParams previewParams, Writer writer, boolean isExternal) {
        if (isExternal && order.getPublishCode() == null) {
            throw new PageNotFoundError();
        }

        Vendor vendor = order.getUser().getVendor();
        String flipper;
        if (isExternal) {
            flipper = renderOrderFlipperByPublishedCode(order.getPublishCode(), vendor, previewParams);
        } else {
            flipper = renderOrderFlipper(order, vendor, previewParams);
        }

        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("title", String.format(PREVIEW_TITLE_TEMPLATE, order.getNumber()));
        freeMarker.set("flipper", flipper);

        // TODO use renderOrderFlipper
        freeMarker.set("id", "pageflip_" + "albumId");
        freeMarker.set("albumWidth", previewParams.getPageWidth() * 2);
        freeMarker.set("albumHeight", previewParams.getPageHeight()); //pageHeight);
        freeMarker.set("nPages", previewParams.getnPages()); //nPages);
        final int N_INITIAL_PAGES = 3;
        freeMarker.set("nInitialPages", N_INITIAL_PAGES);
        freeMarker.set("hard", previewParams.isHardOrEverflat());
        String imageUrlPattern = "http://" + vendor.getOnlineEditorUrl() + "/app/albumSheetPreviewImage?albumId="
            + order.getImportId() + "&i=${page}";
        freeMarker.set("imageUrlPattern", imageUrlPattern);

        freeMarker.process("pageflip.ftl", Locales.RU, writer);
    }

    private void showOrderFlash(Order<?> order, Writer writer, boolean isExternal) {
        User user = order.getUser();
        Vendor vendor = user.getVendor();

        FlashParams flashParams;
        if (isExternal) {
            if (user.getId() == EXT_FLASH_USER_ID) {
                flashParams = flashService.getFlashPreviewParams(order, "/ext/albomchik_by.jpg");
            } else if (order.isPublishFlash() || order.isWebFlash()) {
                flashParams = flashService.getFlashPreviewParams(order, null);
            } else {
                throw new PageNotFoundError();
            }
        } else {
            flashParams = flashService.getFlashPreviewParams(order, null);
        }

        FreeMarker freeMarker = new FreeMarker(getClass());
        // TODO use renderOrderFlashFlipper
        freeMarker.set("title", String.format(PREVIEW_TITLE_TEMPLATE, order.getNumber()));
        freeMarker.set("id", "pageflip_" + "albumId");
        freeMarker.set("albumWidth", flashParams.getFlashWidth()); //pageWidth * 2);
        freeMarker.set("albumHeight", flashParams.getFlashHeight()-79); //pageHeight);
        freeMarker.set("nPages", flashParams.getnPages());
        final int N_INITIAL_PAGES = 3;
        freeMarker.set("nInitialPages", N_INITIAL_PAGES);
        freeMarker.set("sessionId", flashParams.getSessionId());
        boolean isSeparateCover = ((Album) order.getProduct()).isSeparateCover();
        freeMarker.set("isSeparateCover", isSeparateCover);

        Album album = (Album) order.getProduct();
        freeMarker.set("hard", album.isHardOrEverflat());
        String imageUrlPattern  = "http://flash." + vendor.getEnglishDomain() + "/image?a=" + flashParams.getSessionId()
            + "&b=${b}&c=${c}&d=${d}";
//        String imageUrlPattern = "http://127.0.0.1:8888/flash/image?a=" + flashParams.getSessionId()
//            + "&b=${b}&c=${c}&d=${d}";
        freeMarker.set("imageUrlPattern", imageUrlPattern);
        freeMarker.process("pageflip.ftl", Locales.RU, writer);
    }

    @Override
    public void showPageNotFound(Writer writer) {
        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("title", "Макет альбома");
        freeMarker.process("pageNotFound.ftl", Locales.RU, writer);
    }
}
