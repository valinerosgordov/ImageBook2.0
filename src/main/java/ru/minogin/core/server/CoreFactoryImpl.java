package ru.minogin.core.server;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.format.Formatter;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.client.observer.Event;
import ru.minogin.core.client.observer.Observable;
import ru.minogin.core.client.observer2.Observable2;
import ru.minogin.core.client.security.XRandom;
import ru.minogin.core.client.serialization.Serializer;
import ru.minogin.core.client.text.Char;
import ru.minogin.core.client.text.GWTChar;
import ru.minogin.core.client.timer.Timer;
import ru.minogin.core.client.util.DateUtil;
import ru.minogin.core.server.crypto.HasherImpl;
import ru.minogin.core.server.format.FormatterImpl;
import ru.minogin.core.server.observer.RemoteObservable;
import ru.minogin.core.server.observer2.RemoteObservable2;
import ru.minogin.core.server.security.XRandomImpl;
import ru.minogin.core.server.serialization.SerializerImpl;
import ru.minogin.core.server.timer.TimerImpl;
import ru.minogin.core.server.util.DateUtilImpl;

public class CoreFactoryImpl extends CoreFactory {
	private Char xChar;
	private Hasher hasher;
	private Serializer serializer;
	private DateUtil dateUtil;

	@Override
	public <E extends Event> Observable<E> createObservable() {
		return new RemoteObservable<E>();
	}

	@Override
	public <E extends Event> Observable2<E> createObservable2() {
		return new RemoteObservable2<E>();
	}

	@Override
	public Char getChar() {
		if (xChar == null)
			xChar = new GWTChar();

		return xChar;
	}

	@Override
	public Timer createTimer(Runnable runnable) {
		return new TimerImpl(runnable);
	}

	@Override
	public Hasher getHasher() {
		if (hasher == null)
			hasher = new HasherImpl();

		return hasher;
	}

	@Override
	public Serializer getSerializer() {
		if (serializer == null)
			serializer = new SerializerImpl();

		return serializer;
	}

	@Override
	public DateUtil getDateUtil() {
		if (dateUtil == null)
			dateUtil = new DateUtilImpl();

		return dateUtil;
	}

	@Override
	public XRandom createRandom() {
		return new XRandomImpl();
	}

	@Override
	public Compiler createCompiler() {
		return new Compiler(this);
	}

	@Override
	public Formatter createFormatter() {
		return new FormatterImpl();
	}
}
