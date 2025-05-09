package ru.imagebook.client.app.view.payment;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SuggestOracle;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author alexander.ov
 * @since 2014-09-15
 */
public abstract class MajorCitySuggestOracle extends SuggestOracle {

    public static final Function<String, Suggestion> STRING_TO_SUGGESTION = new Function<String, Suggestion>() {
        @Nullable
        @Override
        public Suggestion apply(@Nullable final String input) {
            assert input != null;
            return new Suggestion() {
                @Override
                public String getDisplayString() {
                    return input;
                }

                @Override
                public String getReplacementString() {
                    return input;
                }
            };
        }
    };

    private static final int REQUEST_DELAY = 300;
    private static final int REQUEST_LIMIT = 10;

    private final Timer requestTimer;
    private Request currentRequest;
    private Callback currentCallback;

    public MajorCitySuggestOracle() {
        super();
        requestTimer = new Timer() {
            @Override
            public void run() {
                if (currentRequest != null && currentCallback != null) {
                    doRequest(currentRequest, currentCallback);
                }
            }
        };
    }

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        currentRequest = request;
        currentCallback = callback;

        currentRequest.setLimit(REQUEST_LIMIT);

        // If the user keeps triggering this event (e.g., keeps typing),
        // cancel and restart the timer
        requestTimer.cancel();
        requestTimer.schedule(REQUEST_DELAY);
    }

    public abstract void doRequest(Request request, Callback callback);

    public static class Response extends SuggestOracle.Response {
        public Response() {
            super();
        }

        public void setSuggestions(List<String> list) {
            List<Suggestion> suggestions = Lists.transform(list, STRING_TO_SUGGESTION);
            super.setSuggestions(suggestions);
        }
    }
}
