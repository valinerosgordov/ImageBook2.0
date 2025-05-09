package ru.imagebook.shared.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
@Table(name = "vendor")
public class Vendor extends BaseEntityImpl {
	private static final long serialVersionUID = -6000532255977455023L;

	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String PRINTER = "printer";
	public static final String CUSTOMER_ID = "customerId";
	public static final String KEY = "key";
	public static final String ORDER_IMPORT_PASSWD = "orderImport_passwd";
	public static final String COMPANY_NAME = "companyName";
	public static final String EMAIL = "email";
	public static final String PHONE = "phone";
	public static final String SITE = "site";
	public static final String ENGLISH_DOMAIN = "englishDomain";
	public static final String COLOR = "color";
	public static final String ADMIN_EMAIL = "adminEmail";
	public static final String RECEIVER = "receiver";
	public static final String INN = "inn";
	public static final String KPP = "kpp";
	public static final String ACCOUNT = "account";
	public static final String CORR_ACCOUNT = "corr_account";
	public static final String BANK = "bank";
	public static final String BIK = "bik";

	private String name;
	private int type;
	private boolean printer;
	private String customerId;
	private String key;
	private String orderImportPasswd;
	private String companyName;
	private String email;
	private String phone;
	private String site;
	private String englishDomain;
	private String color;
	private String adminEmail;
	private String receiver;
	private String inn;
	private String kpp;
	private String account;
	private String corrAccount;
	private String bank;
	private String bik;
	private String roboLogin;
	private String roboPassword1;
	private String roboPassword2;
	private String smsFrom;
	private boolean noBonusCheck;
	private Long yandexShopId;
	private Long yandexScid;
	private String yandexShopPassword;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@NotNull
	public boolean isPrinter() {
		return printer;
	}

	public void setPrinter(boolean printer) {
		this.printer = printer;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Column(name = "`key`")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOrderImportPasswd() {
		return orderImportPasswd;
	}

	public void setOrderImportPasswd(String orderImportPasswd) {
		this.orderImportPasswd = orderImportPasswd;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public String getKpp() {
		return kpp;
	}

	public void setKpp(String kpp) {
		this.kpp = kpp;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCorrAccount() {
		return corrAccount;
	}

	public void setCorrAccount(String corrAccount) {
		this.corrAccount = corrAccount;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBik() {
		return bik;
	}

	public void setBik(String bik) {
		this.bik = bik;
	}

	@Transient
	public String getFlashUrl() {
		return "flash." + getSite();
	}

	@Transient
	public String getOfficeUrl() {
		return "lk." + getSite();
	}
	
	@Transient
    public String getEditorUrl() {
        return "editor." + getSite();
    }

    @Transient
    public String getOnlineEditorUrl() {
        return "online." + getSite();
    }

	public String getRoboLogin() {
		return roboLogin;
	}

	public void setRoboLogin(String roboLogin) {
		this.roboLogin = roboLogin;
	}

	public String getRoboPassword1() {
		return roboPassword1;
	}

	public void setRoboPassword1(String roboPassword1) {
		this.roboPassword1 = roboPassword1;
	}

	public String getRoboPassword2() {
		return roboPassword2;
	}

	public void setRoboPassword2(String roboPassword2) {
		this.roboPassword2 = roboPassword2;
	}

	public String getSmsFrom() {
		return smsFrom;
	}

	public void setSmsFrom(String smsFrom) {
		this.smsFrom = smsFrom;
	}

	public String getEnglishDomain() {
		return englishDomain;
	}

	public void setEnglishDomain(String englishDomain) {
		this.englishDomain = englishDomain;
	}

	public boolean isNoBonusCheck() {
		return noBonusCheck;
	}
	
	public void setNoBonusCheck(boolean noBonusCheck) {
		this.noBonusCheck = noBonusCheck;
	}

	public Long getYandexShopId() {
		return yandexShopId;
	}

	public void setYandexShopId(Long yandexShopId) {
		this.yandexShopId = yandexShopId;
	}

	public Long getYandexScid() {
		return yandexScid;
	}

	public void setYandexScid(Long yandexScid) {
		this.yandexScid = yandexScid;
	}

	public String getYandexShopPassword() {
		return yandexShopPassword;
	}

	public void setYandexShopPassword(String yandexShopPassword) {
		this.yandexShopPassword = yandexShopPassword;
	}
}
