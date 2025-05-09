package ru.imagebook.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.server.model.feedback.FeedbackView;
import ru.imagebook.server.repository.FeedbackRepository;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.site.Feedback;
import ru.imagebook.shared.model.site.FeedbackAnonymousUser;
import ru.imagebook.shared.model.site.FeedbackUser;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zinchenko on 07.09.14.
 */
public class FeedbackServiceImpl extends BaseGenericService<Feedback, Integer> implements FeedbackService {

    public static final String FEEDBACK_NOTIFICATION_TEMPLATE = "feedbackNotification.ftl";
    public static final String FEEDBACK_NOTIFICATION_SUBJECT = "feedbackNotificationSubject";
    @Autowired
    private FeedbackConfig feedbackConfig;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackUserService feedbackUserService;

    private FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository repository) {
        super(repository);
        feedbackRepository = repository;
    }

    @Override
    @Transactional
    public List<Feedback> getRange(Integer from, Integer to) {
        return feedbackRepository.getRange(from, to);
    }

    @Override
    @Transactional
    public List<Feedback> getFirstPage() {
        return getRange(0, feedbackConfig.getPageSize());
    }

    @Override
    @Transactional
    public Feedback save(FeedbackView feedbackView) {
        Feedback feedback = convert(feedbackView);
        save(feedback);
        notifyAdmin(feedbackView);
        return feedback;
    }

    private void notifyAdmin(FeedbackView feedbackView) {
        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("user", feedbackView.getName());
        freeMarker.set("email", feedbackView.getEmail());
        freeMarker.set("message", feedbackView.getMessage());
        String subject = messages.getMessage(FEEDBACK_NOTIFICATION_SUBJECT, null, new Locale(Locales.RU));
        String html = freeMarker.process(FEEDBACK_NOTIFICATION_TEMPLATE, Locales.RU);
        notifyService.notifyAdmin(subject, html);
    }

    private Feedback convert(FeedbackView feedbackView) throws NoEntityPermissionException {
        FeedbackUser feedbackUser = convertFeedbackUser(feedbackView);

        Feedback feedback = new Feedback();
        feedback.setMessage(feedbackView.getMessage());
        feedback.setCreateDate(new Date());
        feedback.setFeedbackUser(feedbackUser);
        return feedback;
    }

    private FeedbackUser convertFeedbackUser(FeedbackView feedbackView) throws NoEntityPermissionException {
        FeedbackUser feedbackUser = feedbackView.getFeedbackUser();
        if (feedbackView.getInternalUserId() == null) {
            FeedbackAnonymousUser feedbackAnonymousUser = new FeedbackAnonymousUser();
            feedbackAnonymousUser.setEmail(feedbackView.getEmail());
            feedbackAnonymousUser.setName(feedbackView.getName());
            feedbackAnonymousUser.setPhone(feedbackView.getPhone());
            feedbackUser.setFeedbackAnonymousUser(feedbackAnonymousUser);
            feedbackUserService.save(feedbackUser);
            return feedbackUser;
        } else {
            User user = userService.getUser(feedbackView.getInternalUserId());
            feedbackUser.setFeedbackInternalUser(user);
            return feedbackUserService.merge(feedbackUser);
        }
    }

    public FeedbackConfig getFeedbackConfig() {
        return feedbackConfig;
    }

    public void setFeedbackConfig(FeedbackConfig feedbackConfig) {
        this.feedbackConfig = feedbackConfig;
    }

    public FeedbackRepository getFeedbackRepository() {
        return feedbackRepository;
    }

    public void setFeedbackRepository(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public NotifyService getNotifyService() {
        return notifyService;
    }

    public void setNotifyService(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    public MessageSource getMessages() {
        return messages;
    }

    public void setMessages(MessageSource messages) {
        this.messages = messages;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public FeedbackUserService getFeedbackUserService() {
        return feedbackUserService;
    }

    public void setFeedbackUserService(FeedbackUserService feedbackUserService) {
        this.feedbackUserService = feedbackUserService;
    }
}
