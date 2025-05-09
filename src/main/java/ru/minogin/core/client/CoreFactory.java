package ru.minogin.core.client;

import ru.minogin.core.client.common.Builder;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.format.Formatter;
import ru.minogin.core.client.i18n.MultiString;
import ru.minogin.core.client.i18n.Noun;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.client.observer.Event;
import ru.minogin.core.client.observer.Observable;
import ru.minogin.core.client.observer2.Observable2;
import ru.minogin.core.client.security.PasswordGenerator;
import ru.minogin.core.client.security.PasswordGeneratorImpl;
import ru.minogin.core.client.security.XRandom;
import ru.minogin.core.client.serialization.Serializer;
import ru.minogin.core.client.text.Char;
import ru.minogin.core.client.timer.Timer;
import ru.minogin.core.client.util.DateUtil;

public abstract class CoreFactory {
	public CoreFactory() {
		getSerializer().registerBuilder(MultiString.TYPE_NAME, new Builder<MultiString>() {
			@Override
			public MultiString newInstance() {
				return new MultiString();
			}
		});
		getSerializer().registerBuilder(Noun.TYPE_NAME, new Builder<Noun>() {
			@Override
			public Noun newInstance() {
				return new Noun();
			}
		});
	}

	public abstract <E extends Event> Observable<E> createObservable();

	public abstract <E extends Event> Observable2<E> createObservable2();

	public abstract Char getChar();

	public abstract Timer createTimer(Runnable runnable);

	public abstract Hasher getHasher();

	public abstract Serializer getSerializer();

	public abstract DateUtil getDateUtil();

	public abstract XRandom createRandom();

	public PasswordGenerator createPasswordGenerator() {
		return new PasswordGeneratorImpl(createRandom());
	}

	public abstract Compiler createCompiler();

	public abstract Formatter createFormatter();
}
