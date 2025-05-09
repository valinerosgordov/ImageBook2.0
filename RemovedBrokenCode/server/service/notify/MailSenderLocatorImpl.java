package ru.imagebook.server.service.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;

/**

 * @since 11.02.2015
 */
@Service
public class MailSenderLocatorImpl implements MailSenderLocator {

    @Autowired
    @Qualifier("vendorMailSender")
    private JavaMailSender vendorMailSender;

    @Autowired
    @Qualifier("imagebookMailSender")
    private JavaMailSender imagebookMailSender;

    @Override
    public JavaMailSender getMailSender(Vendor vendor) {
        return vendor.getType() == VendorType.IMAGEBOOK ? imagebookMailSender : vendorMailSender;
    }
}
