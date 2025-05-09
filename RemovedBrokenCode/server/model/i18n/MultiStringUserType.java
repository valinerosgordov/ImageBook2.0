package ru.imagebook.server.model.i18n;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.TextType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import ru.imagebook.shared.model.i18n.I18nStaticModel;
import ru.minogin.core.client.i18n.MultiString;

public class MultiStringUserType implements CompositeUserType {
	@Override
	public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner)
			throws HibernateException {
		return cached;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public Serializable disassemble(Object value, SharedSessionContractImplementor session)
			throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y)
			return true;
		if (x == null || y == null)
			return false;
		return x.equals(y);
	}

	@Override
	public String[] getPropertyNames() {
		return I18nStaticModel.getLocales();
	}

	@Override
	public Type[] getPropertyTypes() {
		return new Type[] { TextType.INSTANCE, TextType.INSTANCE };
	}

	@Override
	public Object getPropertyValue(Object component, int property) throws HibernateException {
		MultiString ms = (MultiString) component;
		String locale = I18nStaticModel.getLocales()[property];
		return ms != null ? ms.get(locale) : null;
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x != null ? x.hashCode() : 0;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		MultiString ms = new MultiString();
		int i = 0;
		for (String locale : I18nStaticModel.getLocales()) {
			ms.set(locale, rs.getString(names[i]));
			i++;
		}
		return ms;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if (value != null) {
			MultiString ms = (MultiString) value;
			int i = 0;
			for (String locale : I18nStaticModel.getLocales()) {
				st.setString(index + i, ms.get(locale));
				i++;
			}
		}
		else {
			for (int i = 0; i < I18nStaticModel.getLocales().length; i++) {
				st.setNull(index + i, Types.CLOB);
			}
		}
	}

	@Override
	public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner)
			throws HibernateException {
		return original;
	}

	@Override
	public Class<?> returnedClass() {
		return MultiString.class;
	}

	@Override
	public void setPropertyValue(Object component, int property, Object value)
			throws HibernateException {
		throw new UnsupportedOperationException("MultiString is immutable.");
	}
}
