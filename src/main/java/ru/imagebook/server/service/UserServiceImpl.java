package ru.imagebook.server.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.ctl.user.UserMessages;
import ru.imagebook.server.repository.AuthRepository;
import ru.imagebook.server.repository.UserRepository;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.InvitationState;
import ru.imagebook.shared.model.Module;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.RegisterType;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.RoleImpl;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.security.PasswordGenerator;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.flow.remoting.MessageError;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.hibernate.updater.CollectionUpdater;
import ru.minogin.core.server.hibernate.updater.Updater;

public class UserServiceImpl implements UserService {
    public static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

	public static final String USER_ID_PARAM = "a";
	public static final String CODE_PARAM = "b";
	public static final String SECRET = "a222bbb1-86ba-46df-8722-c54bc64ac23d";

    private static final String USER_EMAIL_EXPORT_FILENAME_TEMPLATE = "Email адреса %1$s %2$s %3$td-%3$tm-%3$tY.xlsx";

	@Autowired
	private UserRepository repository;

	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private MessageSource messages;

	@Autowired
	private CoreFactory coreFactory;

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private VendorService vendorService;

	@Override
	public List<User> loadUsers(int offset, int limit, String query) {
		List<User> users = repository.loadUsers(offset, limit, query);
		List<UserAccount> accounts = authRepository.loadAccounts(users);
		Map<User, UserAccount> accountsMap = new HashMap<User, UserAccount>();
		for (UserAccount account : accounts) {
			accountsMap.put(account.getUser(), account);
		}
		for (User user : users) {
			UserAccount account = accountsMap.get(user);
			user.setActive(account.isActive());
		}
		return users;
	}

	@Override
	public long countUsers(String query) {
		return repository.countUsers(query);
	}

	@Override
	public Integer addUser(User user) {
		user.getRoles().clear();
		user.getRoles().add(new RoleImpl(Role.USER));
		return repository.addUser(user);
	}

	@Override
	public void updateUser(User prototype) {		
		User user = repository.getUser(prototype.getId());
		
		user.setVendor(prototype.getVendor());
		user.setActive(prototype.isActive());
		user.setUserName(prototype.getUserName());
		user.setLastName(prototype.getLastName());
		user.setName(prototype.getName());
		user.setSurname(prototype.getSurname());
		user.setLevel(prototype.getLevel());
		user.setPhotographer(prototype.isPhotographer());
		user.setSkipMailing(prototype.isSkipMailing());
		CollectionUpdater.update(user.getEmails(), prototype.getEmails(),
				new Updater<Email>() {
					@Override
					public void update(Email value, Email prototype) {
						value.setActive(prototype.isActive());
						value.setEmail(prototype.getEmail());
					}
				});		
		CollectionUpdater.update(user.getPhones(), prototype.getPhones(),
				new Updater<Phone>() {
					@Override
					public void update(Phone value, Phone prototype) {
						value.setPhone(prototype.getPhone());
					}
				});
		CollectionUpdater.update(user.getAddresses(), prototype.getAddresses(),
				new Updater<Address>() {
					@Override
					public void update(Address value, Address prototype) {
						value.setCountry(prototype.getCountry());
						value.setIndex(prototype.getIndex());
						value.setRegion(prototype.getRegion());
						value.setCity(prototype.getCity());
						value.setStreet(prototype.getStreet());
						value.setHome(prototype.getHome());
						value.setBuilding(prototype.getBuilding());
						value.setOffice(prototype.getOffice());
						value.setComment(prototype.getComment());
						value.setLastName(prototype.getLastName());
						value.setName(prototype.getName());
						value.setSurname(prototype.getSurname());
						value.setPhone(prototype.getPhone());
					}
				});
		user.setLocale(prototype.getLocale());
		user.setInvitationState(prototype.getInvitationState());
		user.setInfo(prototype.getInfo());
		user.setUrgentOrders(prototype.isUrgentOrders());
		user.setAdvOrders(prototype.isAdvOrders());
		user.setAccessedProducts(prototype.getAccessedProducts());
		user.getAlbumDiscounts().clear();
		user.getAlbumDiscounts().addAll(prototype.getAlbumDiscounts());
		user.setEditorSourcesStoragePeriod(prototype.getEditorSourcesStoragePeriod());
		repository.saveUser(user);
	}

	@Override
	public void deleteUsers(List<Integer> userIds) {
		repository.deleteUsers(userIds);
	}

    @Override
    public void deleteUser(Integer userId) {
        repository.deleteUsers(Collections.singletonList(userId));
    };

	@Transactional
	@Override
	public User getUserLite(int userId) {
		return repository.getUserLite(userId);
	}

	@Transactional
	@Override
	public User getUser(int userId) {
		return repository.getUser(userId);
	}

    @Transactional
    @Override
    public User getUser(String username) {
        return repository.getUser(username);
    }

    @Transactional
    @Override
    public User getUser(String username, Vendor vendor) {
        return repository.getUser(username, vendor);
    }

    @Override
	public List<User> loadUsers(List<Integer> userIds) {
		return repository.loadUsers(userIds);
	}

	@Override
	public Email getEmail(int emailId) {
		return repository.getEmail(emailId);
	}

	@Override
	public void recoverPassword(String email, Module module) {
		Hasher hasher = coreFactory.getHasher();
		Vendor siteVendor = vendorService.getVendorByCurrentSite();

        List<User> users = repository.getUsersByEmail(email, siteVendor);
        for (User user : users) {
            Vendor vendor = user.getVendor();

            Locale locale = new Locale(user.getLocale());
            String subject = messages.getMessage("recoverPasswordSubject",
                    new Object[] { vendor.getName() }, locale);

            FreeMarker freeMarker = new FreeMarker(getClass());
            freeMarker.set("userName", user.getUserName());
            String code = hasher.md5(SECRET + user.getId());

            String baseUrl;
            String template;
            if (module == Module.App) {
                baseUrl = vendor.getOfficeUrl();
                template = "recoverPassword.ftl";
            } else {
                baseUrl = vendor.getEditorUrl();
                template = "editorRecoverPassword.ftl";
            }

            String url = "http://" + baseUrl + "/recoverPassword?"
                + USER_ID_PARAM + "=" + user.getId() + "&" + CODE_PARAM + "="
                + code;
            freeMarker.set("url", url);
            freeMarker.set("data", vendor);
            String html = freeMarker.process(template, user.getLocale());
            notifyService.notifyUser(user, subject, html);
        }
	}

	@Override
	public void recoverPassword(int userId, String code, Writer writer) {
		User user = repository.getUser(userId);
		String password = recoverPassword(userId, code);		
		passwordChangeNotify(writer, user, password, null);
	}
	
	@Override
	public void recoverPassword(int userId, String code, Writer writer, String module) {
		User user = repository.getUser(userId);
		String password = recoverPassword(userId, code);		
		passwordChangeNotify(writer, user, password, module);
	}
	
	private String recoverPassword(int userId, String code) {		 
		Vendor siteVendor = vendorService.getVendorByCurrentSite();
		
		Hasher hasher = coreFactory.getHasher();
		String correctCode = hasher.md5(SECRET + userId);
		if (!code.equals(correctCode))
			throw new AccessDeniedError();

		User user = repository.getUser(userId);
		if (!user.getVendor().equals(siteVendor)) {
			throw new AccessDeniedError();
		}
		PasswordGenerator generator = coreFactory.createPasswordGenerator();
		String password = generator.generate();
		UserAccount account = authRepository.findAccount(user);
		String hash = hasher.hash(password);
		account.setPasswordHash(hash);

		return password;
	}

	private void passwordChangeNotify(Writer writer, User user, String password, String module) {
		Vendor vendor = user.getVendor();
		Locale locale = new Locale(user.getLocale());
		String subject = messages.getMessage("passwordChangedSubject",
				new Object[] { vendor.getName() }, locale);

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("userName", user.getUserName());
		freeMarker.set("password", password);
		freeMarker.set("data", vendor);
		
		String html = null;
		if (module == null || Module.App.name().equals(module)) {
			freeMarker.set("url", vendor.getOfficeUrl());
			html = freeMarker.process("passwordChanged.ftl", user.getLocale());			
		} else {
			freeMarker.set("url", vendor.getEditorUrl());
			html = freeMarker.process("editorPasswordChanged.ftl", user.getLocale());			
		}

		notifyService.notifyUser(user, subject, html);

		freeMarker = new FreeMarker(getClass());
		freeMarker.set("serviceName", vendor.getName());
		freeMarker.process("passwordChanged2.ftl", Locales.RU, writer);
	}

	@Override
	public void changePassword(int userId, String password) {
		User user = repository.getUser(userId);

		Hasher hasher = coreFactory.getHasher();
		String passwordHash = hasher.hash(password);

		UserAccount account = authRepository.findAccount(user);
		account.setPasswordHash(passwordHash);

		authRepository.saveAccount(account);
	}


	@Deprecated
	@Override
	public void sendInvitation(List<Integer> userIds) {
		List<User> users = loadUsers(userIds);

		for (User user : users) {
			int state = user.getInvitationState();
			if (state == InvitationState.SENT || state == InvitationState.CONFIRMED) {
                throw new MessageError(UserMessages.INVITATION_ALREADY_SENT);
            }
		}

        for (User user : users) {
            Locale locale = new Locale(user.getLocale());
            Vendor vendor = user.getVendor();

            String subject = messages.getMessage("invitationSubject",
                new Object[] { vendor.getName() }, locale);

            String postFix = "";
            if (user.getRegisterType() == RegisterType.OFFICE) {
                postFix = "_office";
            }

            Integer userId = user.getId();
            try {
                for (Email email : user.getEmails()) {
                    FreeMarker freeMarker = new FreeMarker(getClass());
                    Integer emailId = email.getId();
                    String code = ActivationUtil.getActivationCode(coreFactory.getHasher(), userId, emailId);
                    String url = "http://" + vendor.getOfficeUrl() + "/activation?"
                        + ActivationService.USER_ID_PARAM + "=" + userId + "&"
                        + ActivationService.EMAIL_ID_PARAM + "=" + emailId + "&"
                        + ActivationService.CODE_PARAM + "=" + code;
                    freeMarker.set("url", url);
                    freeMarker.set("data", vendor);
                    String text = freeMarker.process("invitation" + postFix + ".ftl", user.getLocale());

                    notifyService.notifyUserToSeparateEmail(user, subject, text, email.getEmail());
                }
                user.setInvitationState(InvitationState.SENT);
            } catch (Exception e) {
                ServiceLogger.log(e);
                user.setInvitationState(InvitationState.ERROR_WHEN_SENDING);
            }
        }
	}

	@Override
	public void sendRegistrationMail(User user, String email) {
		String postFix = "";
		if (user.getRegisterType() == RegisterType.OFFICE) {
			postFix = "_office";
		}
		sendRegistrationMail(postFix, user, email);
	}
	
	@Override
	public void sendRegistrationMail(User user, String email, String module) {
		String postFix = "";
		if (module != null && Module.Editor.name().equals(module)) {
			postFix = "_editor";
			sendRegistrationMail(postFix, user, email); 
		} else {
			sendRegistrationMail(user, email);
		}		
	}	
		
	private void sendRegistrationMail(String tplPostFix, User user, String email) {
		Locale locale = new Locale(user.getLocale());
		Vendor vendor = user.getVendor();

		String subject = messages.getMessage("officeSubject",
                new Object[]{vendor.getName()}, locale);

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("data", vendor);
		freeMarker.set("userName", user.getUserName());
		freeMarker.set("password", user.getPassword());
		String html = freeMarker.process("registered" + tplPostFix + ".ftl",
				user.getLocale());

		user.clearPassword();

		try {
			notifyService.notifyUserToSeparateEmail(user, subject, html, email);			
		} catch (Exception e) {
			ServiceLogger.log(e);
		}
	}

	@Override
	public void updateAccount(User user) {
		Hasher hasher = coreFactory.getHasher();

		String password = user.getPassword();

		UserAccount account = authRepository.findAccount(user);
		if (account != null) {
			account.setActive(user.isActive());
			account.setUserName(user.getUserName());

			if (password != null) {
				String passwordHash = hasher.hash(password);
				account.setPasswordHash(passwordHash);
			}
		}
		else {
			String passwordHash = hasher.hash(password);
			account = new UserAccount(user.getUserName(), passwordHash,
					user.isActive(), user);
		}
		authRepository.saveAccount(account);

		// TODO why? because of invitation sending? should clear after invitation is
		// sent or if it is not sent at all.
		// user.setPassword(null);
	}

	@Override
	public User loadUser(int userId) {
		return repository.loadUser(userId);
	}

    @Override
    @Transactional
    public void updateAddress(Address modified) {
        Address address = repository.getAddress(modified.getId());

        address.setLastName(modified.getLastName());
        address.setHome(modified.getHome());
        address.setIndex(modified.getIndex());
        address.setBuilding(modified.getBuilding());
        address.setCity(modified.getCity());
        address.setComment(modified.getComment());
        address.setCountry(modified.getCountry());
        address.setFloor(modified.getFloor());
        address.setIntercom(modified.getIntercom());
        address.setName(modified.getName());
        address.setOffice(modified.getOffice());
        address.setPhone(modified.getPhone());
        address.setPorch(modified.getPorch());
        address.setRegion(modified.getRegion());
        address.setStreet(modified.getStreet());
        address.setSurname(modified.getSurname());
    }

    @Override
	public void flush() {
		repository.flush();
	}

    @Override
    public void detach(User user) {
        repository.detach(user);
    }

    @Override
    public void exportUserEmailsToExcel(Vendor vendor, boolean commonUsers, boolean photographers,
                                        HttpServletResponse response) {
        LOGGER.debug(String.format(
            ">> Exporting user emails to excel [vendorId=%s, commonUsers=%s, photographers=%s]",
            vendor.getId(), commonUsers, photographers));

        final SXSSFWorkbook workbook = new SXSSFWorkbook(500);

        Sheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(30);

        Row row = sheet.createRow(0);
        int idx = 0;
        row.createCell(idx++, Cell.CELL_TYPE_STRING).setCellValue("Фамилия");
        row.createCell(idx++, Cell.CELL_TYPE_STRING).setCellValue("Имя");
        row.createCell(idx++, Cell.CELL_TYPE_STRING).setCellValue("Отчество");
        row.createCell(idx++, Cell.CELL_TYPE_STRING).setCellValue("E-mail");
        row.createCell(idx, Cell.CELL_TYPE_STRING).setCellValue("Тип");

        ScrollableResults results = null;
        try {
            results = repository.loadActiveUserEmails(vendor, commonUsers, photographers);
            while (results.next()) {
                User user = (User) results.get(0);
                row = sheet.createRow(results.getRowNumber());
                idx = 0;
                row.createCell(idx++, Cell.CELL_TYPE_STRING).setCellValue(user.getLastName());
                row.createCell(idx++, Cell.CELL_TYPE_STRING).setCellValue(user.getName());
                row.createCell(idx++, Cell.CELL_TYPE_STRING).setCellValue(user.getSurname());
                row.createCell(idx++, Cell.CELL_TYPE_STRING).setCellValue(user.getFirstEmail() == null ? null : user.getFirstEmail().getEmail());
                row.createCell(idx, Cell.CELL_TYPE_STRING)
                    .setCellValue((user.isPhotographer() ? "фотограф" : "обычный"));
            }
        } finally {
            if (results != null) {
                results.close();
            }
        }

        String userTypeFilter = "все";
        if (commonUsers && !photographers) {
            userTypeFilter = "обычные";
        } else if (!commonUsers && photographers) {
            userTypeFilter = "фотографы";
        }
        String filename = String.format(USER_EMAIL_EXPORT_FILENAME_TEMPLATE, vendor.getName(), userTypeFilter,
            new Date());

        try (final OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/octet-stream");
            filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            // used for jquery fileDownload plugin on client
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            workbook.write(outputStream);
        } catch (IOException ex) {
            Exceptions.rethrow(ex);
        } finally {
            workbook.dispose();
            LOGGER.debug("<< Export user emails to excel done");
        }
    }

    @Transactional
	@Override
	public void updateLogonDate(int userId) {
        User user = repository.getUser(userId);
        user.setLogonDate(new Date());
        repository.saveUser(user);
	}
}
