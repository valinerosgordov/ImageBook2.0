package ru.minogin.core.client.lang;

import ru.minogin.core.client.lang.tokenizer.Syntax;
import ru.minogin.core.client.lang.tokenizer.Token.Type;

public class BaseSyntax extends Syntax {
	public BaseSyntax() {
		addType("true", Type.TRUE);
		addType("false", Type.FALSE);
		
		addType("not", Type.NOT);
		addType("and", Type.AND);
		addType("or", Type.OR);

		addType("if", Type.IF);
		addType("else", Type.ELSE);
		addType("/if", Type.ENDIF);
		addType("endif", Type.ENDIF);

		addType("while", Type.WHILE);
		addType("/while", Type.ENDWHILE);
		addType("endwhile", Type.ENDWHILE);

		addType("for", Type.WHILE);
		addType("/for", Type.ENDFOR);
		addType("endfor", Type.ENDFOR);
		
		addType("да", Type.TRUE);
		addType("нет", Type.FALSE);

		addType("не", Type.NOT);
		addType("и", Type.AND);
		addType("или", Type.OR);

		addType("если", Type.IF);
		addType("иначе", Type.ELSE);
		addType("/если", Type.ENDIF);

		addType("пока", Type.WHILE);
		addType("/пока", Type.ENDWHILE);

		addType("цикл", Type.WHILE);
		addType("/цикл", Type.ENDFOR);
	}
}
