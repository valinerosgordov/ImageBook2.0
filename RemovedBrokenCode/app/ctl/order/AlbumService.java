package ru.imagebook.client.app.ctl.order;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.imagebook.client.app.service.OrderRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;

@Singleton
public class AlbumService {
    private OrderRemoteServiceAsync orderService;

    @Inject
    public AlbumService(OrderRemoteServiceAsync orderService) {
        this.orderService = orderService;
    }

    public void editAlbum(final String albumId) {
        if (albumId == null)
            throw new NullPointerException();

        orderService.getAuthToken(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String token) {
                String url;
                if (GWT.isProdMode())
                    url = "http://book.imagebook.ru/#/editor/" + albumId + "/" + token;   // FIXME Get URL from server config
                else
                    url = "http://localhost/#/editor/" + albumId + "/" + token;
                Window.Location.assign(url);
            }
        });
    }
}
