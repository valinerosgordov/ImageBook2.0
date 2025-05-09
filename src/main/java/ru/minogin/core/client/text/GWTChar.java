package ru.minogin.core.client.text;

public class GWTChar extends Char {
	// TODO other locales or maybe Google will solve charset problem.
	public boolean isLetter(char c) {
		if (Character.isLetter(c))
			return true;
		
		if ((c >= 'а' && c <= 'я') || (c >= 'А' && c <= 'Я'))
			return true;
		
		return false;
	}
}
