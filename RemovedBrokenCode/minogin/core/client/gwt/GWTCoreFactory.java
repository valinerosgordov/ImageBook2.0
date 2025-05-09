package ru.minogin.core.client.gwt;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.format.Formatter;
import ru.minogin.core.client.format.GWTFormatter;
import ru.minogin.core.client.gwt.crypto.GWTHasher;
import ru.minogin.core.client.gwt.timer.GWTTimer;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.client.observer.Event;
import ru.minogin.core.client.observer.LocalObservable;
import ru.minogin.core.client.observer.Observable;
import ru.minogin.core.client.observer2.LocalObservable2;
import ru.minogin.core.client.observer2.Observable2;
import ru.minogin.core.client.security.GWTXRandom;
import ru.minogin.core.client.security.XRandom;
import ru.minogin.core.client.serialization.GWTSerializer;
import ru.minogin.core.client.serialization.Serializer;
import ru.minogin.core.client.text.Char;
import ru.minogin.core.client.text.GWTChar;
import ru.minogin.core.client.timer.Timer;
import ru.minogin.core.client.util.DateUtil;
import ru.minogin.core.client.util.GWTDateUtil;

public class GWTCoreFactory extends CoreFactory {
	private Hasher hasher;
	private Char xChar;
	private Serializer serializer;

	@Override
	public <E extends Event> Observable<E> createObservable() {
		return new LocalObservable<E>();
	}

	@Override
	public <E extends Event> Observable2<E> createObservable2() {
		return new LocalObservable2<E>();
	}

	@Override
	public Char getChar() {
		if (xChar == null)
			xChar = new GWTChar();

		return xChar;
	}

	@Override
	public Timer createTimer(Runnable runnable) {
		return new GWTTimer(runnable);
	}

	@Override
	public Hasher getHasher() {
		if (hasher == null)
			hasher = new GWTHasher();

		return hasher;
	}

	@Override
	public Serializer getSerializer() {
		if (serializer == null)
			serializer = new GWTSerializer();

		return serializer;
	}

	@Override
	public DateUtil getDateUtil() {
		return new GWTDateUtil();
	}

	@Override
	public XRandom createRandom() {
		return new GWTXRandom();
	}

	@Override
	public Compiler createCompiler() {
		return new Compiler(this);
	}

	@Override
	public Formatter createFormatter() {
		return new GWTFormatter();
	}
}
