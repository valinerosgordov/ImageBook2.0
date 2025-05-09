package ru.imagebook.client.admin.ctl.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.service.ProductImageRemoteServiceAsync;
import ru.imagebook.client.admin.view.product.ProductImagePresenter;
import ru.imagebook.client.admin.view.product.ProductImageView;
import ru.imagebook.shared.model.ProductImage;
import ru.imagebook.shared.service.admin.product.ProductImageFileUploadedMessage;
import ru.minogin.core.client.gxt.grid.LoadResult;
import ru.minogin.core.client.push.PushMessage;
import ru.minogin.core.client.push.mvp.PushEvent;
import ru.minogin.core.client.push.mvp.PushEventHandler;
import ru.minogin.util.client.rpc.XAsyncCallback;

@Singleton
public class ProductImageController implements ProductImagePresenter, PushEventHandler {
    @Inject
    private ProductImageRemoteServiceAsync service;
    @Inject
    private EventBus eventBus;

    private ProductImageView view;
    private ProductImage image;
    private Integer productId;
    private Mode mode;
    private HandlerRegistration handlerRegistration;

    private enum Mode {
        ADD, EDIT
    }

    @Inject
    public ProductImageController(ProductImageView view) {
        view.setPresenter(this);
        this.view = view;
    }

    @Override
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public void photosLoaded(final int offset, final int limit) {
        image = new ProductImage();
        mode = Mode.ADD;
        if (productId == null) {
            view.showPhotos(Collections.<ProductImage>emptyList(), 0, 0);
        } else {
            service.loadPhotos(productId,  offset,  limit, new XAsyncCallback<LoadResult<ProductImage>>() {
                @Override
                public void onSuccess(final LoadResult<ProductImage> imageLoadResult) {
                    view.showPhotos(imageLoadResult.getObjects(), imageLoadResult.getOffset(),
                        (int) imageLoadResult.getTotal());
                }
            });
        }
    }

    @Override
    public void addEditFormSave() {
        view.fetch(image);
        if (mode == Mode.ADD) {
            service.addPhoto(productId, image, new XAsyncCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    view.reload();
                }
            });
        } else if (mode == Mode.EDIT) {
            service.updateImage(image, new XAsyncCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    view.reload();
                }
            });
        }
    }

    @Override
    public void photoSelected() {
        image = view.getSelectedPhoto();
        if (image != null && image.getId() != null) {
            view.updatePhoto(image.getPath());
//            service.getPhotoPath(image, new XAsyncCallback<String>() {
//                @Override
//                public void onSuccess(String s) {

//                }
//            });
            mode = Mode.EDIT;
            view.setFormValues(image);
        } else {
            image = new ProductImage();
            mode = Mode.ADD;
            view.clearFields();
        }
    }

    @Override
    public void show(String productName) {
        handlerRegistration = eventBus.addHandler(PushEvent.TYPE, this);

        view.show(productName);
        view.reload();
    }

    @Override
    public void onHide() {
        handlerRegistration.removeHandler();
    }

    @Override
    public void onPush(PushMessage pushMessage) {
        if (pushMessage instanceof ProductImageFileUploadedMessage) {
            ProductImageFileUploadedMessage message = (ProductImageFileUploadedMessage) pushMessage;
            String filePath = message.getPhotoPath();
            if (filePath != null) {
                image.setSourceFile(message.getSourceFile());
                image.setImage(filePath);
            }

            addEditFormSave();
        }
    }

    @Override
    public void deleteButtonClicked() {
        List<ProductImage> photos = view.getSelectedPhotos();
        if (!photos.isEmpty()) {
            view.confirmDelete();
        } else {
            view.emptySelection();
        }
    }

    @Override
    public void deletionConfirmed() {
        List<Integer> ids = new ArrayList<Integer>();
        for (ProductImage image :  view.getSelectedPhotos()) {
            ids.add(image.getId());
        }
        service.deletePhotos(ids, new XAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.reload();
            }
        });
    }
}
