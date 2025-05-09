package ru.imagebook.shared.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ru.minogin.core.client.bean.BaseEntityBean;

public class BonusAction extends BaseEntityBean {
	private static final long serialVersionUID = -6814248212502094395L;

	public static final String NAME = "name";
	public static final String DATE_START = "dateStart";
	public static final String DATE_START_FIELD = "dateStartField";
	public static final String DATE_END_FIELD = "dateEndField";
	public static final String DATE_END   = "dateEnd";
	public static final String CODES = "codes";
	public static final String CODE_LENGTH = "codeLength";
	// First time order level
	public static final String LEVEL1 = "level1";
	// All other times order level
	public static final String LEVEL2 = "level2";
	// User permanent level
	public static final String PERMANENT_LEVEL = "permanentLevel";
	// First time order discount
	public static final String DISCOUNT1 = "discount1";
	// User permanent discount
	public static final String DISCOUNT2 = "discount2";
	public static final String REPEATAL = "repeatal";
	
	public static final String VENDOR = "vendor";

	public static final String DISCOUNT_PCENTER = "discountPCenter";
	public static final String ALBUMS = "albums";

	private Integer discountSum;

	public BonusAction() {
		setDateStart(new Date());
		setDateEnd(null);
		setCodes(new TreeSet<BonusCode>());
		setLevel1(0);
		setLevel2(0);
		setPermanentLevel(0);
		setDiscount1(0);
		setDiscount2(0);
		setRepeatal(false);
		setAlbums(new TreeSet<Album>());
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public Date getDateStart() {
		return get(DATE_START);
	}

	public void setDateStart(final Date date) {
		set(DATE_START, date);
	}
	
	public Date getDateEnd() {
		return get(DATE_END);
	}

	public void setDateEnd(final Date date) {
		set(DATE_END, date);
	}

	public Set<BonusCode> getCodes() {
		return get(CODES);
	}

	public void setCodes(Set<BonusCode> codes) {
		set(CODES, codes);
	}
	
	public Integer getCodeLength() {
		return get(CODE_LENGTH);
	}
	
	public void setCodeLength(Integer length) {
		set(CODE_LENGTH, length);
	}

	public void addCode(BonusCode code) {
		getCodes().add(code);
		code.setAction(this);
	}

	public int getLevel1() {
		return (Integer) get(LEVEL1);
	}

	public void setLevel1(int level1) {
		set(LEVEL1, level1);
	}

	public int getLevel2() {
		return (Integer) get(LEVEL2);
	}

	public void setLevel2(int level2) {
		set(LEVEL2, level2);
	}

	public int getPermanentLevel() {
		return (Integer) get(PERMANENT_LEVEL);
	}

	public void setPermanentLevel(int permanentLevel) {
		set(PERMANENT_LEVEL, permanentLevel);
	}

	public int getDiscount1() {
		return (Integer) get(DISCOUNT1);
	}

	public void setDiscount1(int discount1) {
		set(DISCOUNT1, discount1);
	}

	public int getDiscount2() {
		return (Integer) get(DISCOUNT2);
	}

	public void setDiscount2(int discount2) {
		set(DISCOUNT2, discount2);
	}

	public boolean isRepeatal() {
		return (Boolean) get(REPEATAL);
	}

	public void setRepeatal(boolean repeatal) {
		set(REPEATAL, repeatal);
	}

	public Integer getDiscountSum() {
		return discountSum;
	}

	public void setDiscountSum(Integer discountSum) {
		this.discountSum = discountSum;
	}

	public Integer getDiscountPCenter() {
		return get(DISCOUNT_PCENTER);
	}

	public void setDiscountPCenter(Integer discountPC) {
		set(DISCOUNT_PCENTER, discountPC);
	}

	public Vendor getVendor() {
		return get(VENDOR);
	}
	
	public void setVendor(Vendor vendor) {
		set(VENDOR, vendor);
	}

	public Set<Album> getAlbums() {
		return get(ALBUMS);
	}

	public void setAlbums(Set<Album> albums) {
		set(ALBUMS, albums);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof BonusAction))
			return false;

		BonusAction action = (BonusAction) obj;
		if (action.getId() == null || getId() == null)
			return false;

		return action.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
