package ru.imagebook.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.imagebook.server.model.web.PageModel;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.SecurityService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.flash.FlashService;
import ru.imagebook.server.service.pickbook.PickbookClient;
import ru.imagebook.server.service.preview.PreviewService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.common.preview.AlbumPreviewParams;
import ru.imagebook.shared.model.common.preview.FlashParams;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.gwt.ClientParametersWriter;

@Controller
public class PublishController extends MainController {
    @Autowired
    private VendorService vendorService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FlashService flashService;

    @Autowired
    private PreviewService previewService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PickbookClient pickbookClient;

	@Override
	protected void layout(Model model, PageModel page) {
        super.layout(model, page);
        page.setWide(true);

        model.addAttribute("fullWidth", true);
    }

	@RequestMapping(value = "/publish/{code}", method = RequestMethod.GET)
	public String publishOrder(@PathVariable int code, Model model) {
        layout(model, new PageModel());

        Vendor vendor = vendorService.getVendorByCurrentSite();
        Order<?> order = orderService.getOrderByPublishCode(code);
        User currentUser = authService.getCurrentUser();

        model.addAttribute("vendor", vendor);
        model.addAttribute("url", "http://" + vendor.getSite() + "/publish/" + code);
        if (currentUser != null && order.getUser().equals(currentUser) && currentUser.isPhotographer()) {
            model.addAttribute("customerUrl", "http://albumpreview.ru/?id=" + order.getNumber());
        }
        model.addAttribute("imageUrl", "todo");
//      TODO  model.addAttribute("imageUrl", "http://" + vendor.getDomain() + "/app/albumPageImage?code="

//        if (order.isExternalOrder()) {
//            showPageFlip(code, model, vendor, order);
//        } else {
//            // TODO implement with new preview
//            throw new UnsupportedOperationException("Flash publication is not supported");
         showFlash(code, model, vendor, order, currentUser);
//        }

		return "publish";
	}

	@RequestMapping(value = "/previewJS", method = RequestMethod.GET)
	public String previewJS(@RequestParam("src") String orderNumber, Model model) {
        String previewContent = flashService.showWebFlash(orderNumber);
        model.addAttribute("previewContent", previewContent);
        return "embeddedPreviewJS";
    }

    private void showPageFlip(int code, Model model, Vendor vendor, Order<?> order) {
        AlbumPreviewParams previewParams = pickbookClient.previewAlbum(order.getImportId());
        model.addAttribute("name", previewParams.getAlbumName());
        model.addAttribute("flipper", previewService.renderOrderFlipperByPublishedCode(code, vendor, previewParams));
    }

    private void showFlash(int code, Model model, Vendor vendor, Order<?> order, User currentUser) {
        FlashParams flashParams = flashService.getFlashPreviewParams(order, null);

        // TODO use renderOrderFlashFlipper
        model.addAttribute("id", "pageflip_" + "albumId");
        model.addAttribute("albumWidth", flashParams.getFlashWidth()); //pageWidth * 2);
        model.addAttribute("albumHeight", flashParams.getFlashHeight()-79); //pageHeight);
        model.addAttribute("nPages", flashParams.getnPages());
        final int N_INITIAL_PAGES = 3;
        model.addAttribute("nInitialPages", N_INITIAL_PAGES);
        model.addAttribute("sessionId", flashParams.getSessionId());
        boolean isSeparateCover = ((Album) order.getProduct()).isSeparateCover();
        model.addAttribute("isSeparateCover", isSeparateCover);

        Album album = (Album) order.getProduct();
        model.addAttribute("hard", album.isHardOrEverflat());
        String imageUrlPattern  = "http://flash." + vendor.getEnglishDomain() + "/image?a=" + flashParams.getSessionId()
            + "&b=${b}&c=${c}&d=${d}";
//        String imageUrlPattern = "http://127.0.0.1:8888/flash/image?a=" + flashParams.getSessionId()
//                + "&b=${b}&c=${c}&d=${d}";
        model.addAttribute("imageUrlPattern", imageUrlPattern);

//        if (currentUser != null && (securityService.hasRole(currentUser, Role.OPERATOR)
//            || order.getUser().equals(currentUser))) {
//            showFlashGenerationBlock(code, model);
//        }
    }

    private void showFlashGenerationBlock(int code, Model model) {
        model.addAttribute("showFlashes", true);

        ClientParametersWriter writer = new ClientParametersWriter();
        writer.setParam("code", code);
        writer.write(model);
    }
}