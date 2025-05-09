package ru.imagebook.server.service;

import java.net.IDN;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.VendorRepository;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.imagebook.shared.service.admin.vendor.VendorAuthException;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.server.spring.SpringUtil;

public class VendorServiceImpl implements VendorService {
	public static final String DOT_RF_PUNYCODE = ".xn--p1ai";

	private final VendorRepository repository;
	@Autowired
	private CoreFactory coreFactory;

	public VendorServiceImpl(VendorRepository repository) {
		this.repository = repository;
	}

	@Transactional
	@Override
	public List<Vendor> loadVendors() {
		return repository.loadVendors();
	}

	@Override
	public void saveVendor(Vendor vendor) {
		setOrderImportPasswdHash(vendor, vendor.getOrderImportPasswd());
		repository.saveVendor(vendor);
	}

	@Override
	public void updateVendor(Vendor modified) {
		Vendor vendor = repository.getVendorById(modified.getId());

		vendor.setAccount(modified.getAccount());
		vendor.setAdminEmail(modified.getAdminEmail());
		vendor.setBank(modified.getBank());
		vendor.setBik(modified.getBik());
		vendor.setColor(modified.getColor());
		vendor.setCompanyName(modified.getCompanyName());
		vendor.setCorrAccount(modified.getCorrAccount());
		vendor.setCustomerId(modified.getCustomerId());
		vendor.setEmail(modified.getEmail());
		vendor.setInn(modified.getInn());
		vendor.setKey(modified.getKey());
		vendor.setKpp(modified.getKpp());
		vendor.setName(modified.getName());
		vendor.setPhone(modified.getPhone());
		vendor.setPrinter(modified.isPrinter());
		vendor.setReceiver(modified.getReceiver());
		vendor.setRoboLogin(modified.getRoboLogin());
		vendor.setRoboPassword1(modified.getRoboPassword1());
		vendor.setRoboPassword2(modified.getRoboPassword2());
		vendor.setSite(modified.getSite());
		vendor.setSmsFrom(modified.getSmsFrom());
		vendor.setType(modified.getType());
		vendor.setYandexShopId(modified.getYandexShopId());
		vendor.setYandexScid(modified.getYandexScid());
		vendor.setYandexShopPassword(modified.getYandexShopPassword());
		setOrderImportPasswdHash(vendor, modified.getOrderImportPasswd());

		repository.updateVendor(vendor);
	}

	private void setOrderImportPasswdHash(Vendor vendor, String password) {
		Hasher hasher = coreFactory.getHasher();

		if (password != null) {
			String passwordHash = hasher.hash(password);
			vendor.setOrderImportPasswd(passwordHash);
		}
	}

	@Override
	public Vendor getVendorByCustomerId(String customerId) {
		return repository.getVendorByCustomerId(customerId);
	}

    @Transactional
	@Override
	public Vendor getMainVendor() {
		return repository.getVendorByType(VendorType.IMAGEBOOK);
	}

	@Transactional
	@Override
	public Vendor getVendorByCurrentSite() {
		String serverName = SpringUtil.getRequest().getServerName();

		if (serverName.endsWith(DOT_RF_PUNYCODE)) {
            serverName = IDN.toUnicode(serverName);
        }

		Vendor vendor = repository.getVendorBySite(serverName);
		if (vendor == null) {
			int i = serverName.indexOf(".");
			if (i != -1) {
				serverName = serverName.substring(i + 1);
				vendor = repository.getVendorBySite(serverName);
			}

			if (vendor == null) {
                vendor = getMainVendor();
            }
		}

		return vendor;
	}

	@Override
	public Vendor getVendorById(Integer vendorId) {
		return repository.getVendorById(vendorId);
	}

	@Override
	public Vendor authenticateVendor(String key, String password) {
		Vendor vendor = repository.getVendorByKey(key);

		if (vendor == null)
			throw new NullPointerException();

		Hasher hasher = coreFactory.getHasher();
		if (!hasher.check(password.trim(), vendor.getOrderImportPasswd()))
			throw new VendorAuthException();

		return vendor;
	}

    @Override
    public Vendor getVendorByKey(String key) {
        return repository.getVendorByKey(key);
    }
}
