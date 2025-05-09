package ru.minogin.core.client.lang;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.lang.parser.AndExpression;
import ru.minogin.core.client.lang.parser.AssignmentStatement;
import ru.minogin.core.client.lang.parser.Block;
import ru.minogin.core.client.lang.parser.DiffExpression;
import ru.minogin.core.client.lang.parser.DivExpression;
import ru.minogin.core.client.lang.parser.DotExpression;
import ru.minogin.core.client.lang.parser.EqualsExpression;
import ru.minogin.core.client.lang.parser.Executor;
import ru.minogin.core.client.lang.parser.Expression;
import ru.minogin.core.client.lang.parser.ExpressionStatement;
import ru.minogin.core.client.lang.parser.FalseExpression;
import ru.minogin.core.client.lang.parser.FunctionCallExpression;
import ru.minogin.core.client.lang.parser.GreaterEqualsExpression;
import ru.minogin.core.client.lang.parser.GreaterExpression;
import ru.minogin.core.client.lang.parser.IfStatement;
import ru.minogin.core.client.lang.parser.LessEqualsExpression;
import ru.minogin.core.client.lang.parser.LessExpression;
import ru.minogin.core.client.lang.parser.MulExpression;
import ru.minogin.core.client.lang.parser.NameExpression;
import ru.minogin.core.client.lang.parser.NegExpression;
import ru.minogin.core.client.lang.parser.NotEqualsExpression;
import ru.minogin.core.client.lang.parser.NotExpression;
import ru.minogin.core.client.lang.parser.NumberExpression;
import ru.minogin.core.client.lang.parser.ObjectExpression;
import ru.minogin.core.client.lang.parser.OrExpression;
import ru.minogin.core.client.lang.parser.Script;
import ru.minogin.core.client.lang.parser.Statement;
import ru.minogin.core.client.lang.parser.StringExpression;
import ru.minogin.core.client.lang.parser.SumExpression;
import ru.minogin.core.client.lang.parser.TrueExpression;
import ru.minogin.core.client.lang.parser.WhileStatement;

public class ScriptExecutor extends Executor {
	public static final int INCOMPATIBLE_OPERAND_TYPES = 300;
	public static final int UNDEFINED_FUNCTION = 301;
	public static final int UNDEFINED_VARIABLE_OR_FUNCTION = 302;
	public static final int ASSIGNMENT_ILLEGAL_VAR = 303;
	public static final int ILLEGAL_CONDITION = 304;
	public static final int ILLEGAL_OBJECT_FIELD = 305;
	private static final int ASSIGNMENT_PARENT_IS_NOT_AN_OBJECT = 306;
	private static final int ASSIGNMENT_ILLEGAL_FIELD = 307;
	private static final int PARENT_IS_NOT_AN_OBJECT = 308;
	private static final int ILLEGAL_FIELD = 309;
	private static final int UNDEFINED_FIELD = 310;
	private static final int OUT_OF_MEMORY = 311;
	private static final int OBJECT_SIZE_UNKNOWN = 312;

	private Bean context;
	private Functions functions;
	private List<Object> results = new ArrayList<Object>();
	private int decimalScale = 2;
	private long memoryLimit = 0;
	private long memoryUsed;

	public ScriptExecutor() {
		this(new Functions(), new BaseBean());
	}

	public ScriptExecutor(Functions functions, Bean context) {
		this.functions = functions;
		this.context = context;
	}

	public Object get(String name) {
		return context.get(name);
	}

	public void set(String name, Object value) {
		context.set(name, value);
	}

	@Override
	public void execute(Script script) {
		execute(script.getBlock());
	}

	@Override
	public void execute(Block block) {
		for (Statement statement : block.getStatements()) {
			statement.execute(this);
		}
	}

	@Override
	public void execute(ExpressionStatement statement) {
		results.add(statement.getExpression().eval(this));
	}

	@Override
	public void execute(AssignmentStatement statement) {
		Expression valueExpression = statement.getValue();
		Object value = valueExpression.eval(this);

		Expression var = statement.getVar();
		if (var instanceof DotExpression) {
			DotExpression dot = (DotExpression) var;
			Object parent = dot.getOp1().eval(this);
			if (!(parent instanceof Bean))
				throw new LangError(ASSIGNMENT_PARENT_IS_NOT_AN_OBJECT);
			Bean bean = (Bean) parent;
			String field = getName(dot.getOp2(), ASSIGNMENT_ILLEGAL_FIELD);
			allocate(value);
			bean.set(field, value);
		} else {
			String name = getName(var, ASSIGNMENT_ILLEGAL_VAR);

			allocate(value);

			context.set(name, value);
		}
	}

	@Override
	public void execute(IfStatement statement) {
		Object condition = statement.getCondition().eval(this);
		if (!(condition instanceof Boolean))
			throw new LangError(ILLEGAL_CONDITION);

		if ((Boolean) condition)
			statement.getThenBlock().execute(this);
		else
			statement.getElseBlock().execute(this);
	}

	@Override
	public void execute(WhileStatement statement) {
		while (true) {
			Object condition = statement.getCondition().eval(this);

			if (!(condition instanceof Boolean))
				throw new LangError(ILLEGAL_CONDITION);

			if (!((Boolean) condition))
				break;

			statement.getBody().execute(this);
		}
	}

	@Override
	public BigDecimal eval(NumberExpression expression) {
		BigDecimal value = new BigDecimal(expression.getValue());
		allocate(value);
		return value;
	}

	@Override
	public Object eval(StringExpression expression) {
		String value = expression.getValue();
		allocate(value);
		return value;
	}

	@Override
	public Object eval(TrueExpression expression) {
		allocate(true);
		return true;
	}

	@Override
	public Object eval(FalseExpression expression) {
		allocate(false);
		return false;
	}

	@Override
	public Object eval(SumExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);

		if (op1 == null || op2 == null)
			return null;
		else if (op1 instanceof Integer && op2 instanceof Integer) {
			int value = ((Integer) op1) + ((Integer) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof Double && op2 instanceof Double) {
			double value = ((Double) op1) + ((Double) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof BigDecimal && op2 instanceof BigDecimal) {
			BigDecimal value = ((BigDecimal) op1).add((BigDecimal) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof String && op2 instanceof String) {
			String value = ((String) op1) + ((String) op2);
			allocate(value);
			return value;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(DiffExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);

		if (op1 == null || op2 == null)
			return null;
		else if (op1 instanceof Integer && op2 instanceof Integer) {
			int value = ((Integer) op1) - ((Integer) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof Double && op2 instanceof Double) {
			double value = ((Double) op1) - ((Double) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof BigDecimal && op2 instanceof BigDecimal) {
			BigDecimal value = ((BigDecimal) op1).subtract((BigDecimal) op2);
			allocate(value);
			return value;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(MulExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);

		if (op1 == null || op2 == null)
			return null;
		else if (op1 instanceof Integer && op2 instanceof Integer) {
			int value = ((Integer) op1) * ((Integer) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof Double && op2 instanceof Double) {
			double value = ((Double) op1) * ((Double) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof BigDecimal && op2 instanceof BigDecimal) {
			BigDecimal value = ((BigDecimal) op1).multiply((BigDecimal) op2);
			allocate(value);
			return value;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(DivExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);

		if (op1 == null || op2 == null)
			return null;
		else if (op1 instanceof Integer && op2 instanceof Integer) {
			int value = ((Integer) op1) / ((Integer) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof Double && op2 instanceof Double) {
			double value = ((Double) op1) / ((Double) op2);
			allocate(value);
			return value;
		} else if (op1 instanceof BigDecimal && op2 instanceof BigDecimal) {
			BigDecimal value = ((BigDecimal) op1).divide((BigDecimal) op2,
					decimalScale, BigDecimal.ROUND_HALF_EVEN);
			allocate(value);
			return value;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(NameExpression expression) {
		if (context.getPropertyNames().contains(expression.getName()))
			return context.get(expression.getName());
		else {
			Function function = functions.get(expression.getName());
			if (function != null)
				return function.eval(new Object[0], context);
			else
				throw new LangError(UNDEFINED_VARIABLE_OR_FUNCTION,
						expression.getName());
		}
	}

	@Override
	public Object eval(DotExpression expression) {
		Expression parentExpr = expression.getOp1();
		Object parent = parentExpr.eval(this);
		if (parent == null)
			return null;

		if (!(parent instanceof Bean))
			throw new LangError(PARENT_IS_NOT_AN_OBJECT);
		Bean bean = (Bean) parent;

		Expression fieldExpr = expression.getOp2();
		String fieldName = getName(fieldExpr, ILLEGAL_FIELD);

		if (bean.has(fieldName))
			return bean.get(fieldName);
		else
			throw new LangError(UNDEFINED_FIELD);
	}

	@Override
	public Object eval(FunctionCallExpression expression) {
		Function function = functions.get(expression.getFunction());
		if (function == null)
			throw new LangError(UNDEFINED_FUNCTION, expression.getFunction());

		Collection<Object> args = new ArrayList<Object>();
		for (Expression arg : expression.getArgs()) {
			args.add(arg.eval(this));
		}
		return function.eval(args.toArray(), context);
	}

	@Override
	public Object eval(AndExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);
		if (op1 instanceof Boolean && op2 instanceof Boolean)
			return ((Boolean) op1) && ((Boolean) op2);
		else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(OrExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);
		if (op1 instanceof Boolean && op2 instanceof Boolean) {
			boolean value = ((Boolean) op1) || ((Boolean) op2);
			allocate(value);
			return value;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(EqualsExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);
		return op1 != null ? op1.equals(op2) : op2 == null;
	}

	@Override
	public Object eval(NotEqualsExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);
		return op1 != null ? !op1.equals(op2) : op2 != null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object eval(LessExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);
		if (op1.getClass().getName().equals(op2.getClass().getName())
				&& op1 instanceof Comparable && op2 instanceof Comparable) {
			return ((Comparable) op1).compareTo((Comparable) op2) < 0;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object eval(GreaterExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);
		if (op1.getClass().getName().equals(op2.getClass().getName())
				&& op1 instanceof Comparable && op2 instanceof Comparable) {
			return ((Comparable) op1).compareTo((Comparable) op2) > 0;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object eval(LessEqualsExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);
		if (op1.getClass().getName().equals(op2.getClass().getName())
				&& op1 instanceof Comparable && op2 instanceof Comparable) {
			return ((Comparable) op1).compareTo((Comparable) op2) <= 0;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object eval(GreaterEqualsExpression expression) {
		Object op1 = expression.getOp1().eval(this);
		Object op2 = expression.getOp2().eval(this);
		if (op1.getClass().getName().equals(op2.getClass().getName())
				&& op1 instanceof Comparable && op2 instanceof Comparable) {
			return ((Comparable) op1).compareTo((Comparable) op2) >= 0;
		} else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(NegExpression expression) {
		Object op = expression.getOp().eval(this);
		if (op instanceof Integer)
			return -((Integer) op);
		else if (op instanceof Double)
			return -((Double) op);
		else if (op instanceof BigDecimal)
			return ((BigDecimal) op).negate();
		else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(NotExpression expression) {
		Object op = expression.getOp().eval(this);
		if (op instanceof Boolean)
			return !((Boolean) op);
		else
			throw new LangError(INCOMPATIBLE_OPERAND_TYPES);
	}

	@Override
	public Object eval(ObjectExpression expression) {
		Bean object = new BaseBean();

		Map<Expression, Expression> values = expression.getValues();
		for (Expression var : values.keySet()) {
			String name = getName(var, ILLEGAL_OBJECT_FIELD);
			object.set(name, values.get(var).eval(this));
		}

		return object;
	}

	private String getName(Expression var, int errorCode) {
		String name = null;
		if (var instanceof NameExpression)
			name = ((NameExpression) var).getName();
		else {
			Object result = var.eval(this);
			if (result instanceof String)
				name = (String) result;
		}

		if (name == null)
			throw new LangError(errorCode);

		return name;
	}

	public List<Object> getResults() {
		return results;
	}

	public void setDecimalScale(int decimalScale) {
		this.decimalScale = decimalScale;
	}

	protected void allocate(long size) {
		memoryUsed += size;

		if (memoryLimit != 0 && memoryUsed > memoryLimit)
			throw new LangError(OUT_OF_MEMORY);
	}

	private void allocate(Object value) {
		allocate(sizeOf(value));
	}

	protected long sizeOf(Object value) {
		if (memoryLimit == 0)
			return 0;

		int overhead = 8;

		if (value == null)
			return 0;
		else if (value instanceof Integer)
			return 4 + overhead;
		else if (value instanceof Double)
			return 8 + overhead;
		else if (value instanceof Boolean)
			return 1 + overhead;
		else if (value instanceof String)
			return ((String) value).length() * 2 + 4;
		else if (value instanceof BigDecimal)
			return 100;
		else
			throw new LangError(OBJECT_SIZE_UNKNOWN, value.getClass().getName()); // TODO
	}

	public void setMemoryLimit(long memoryLimit) {
		this.memoryLimit = memoryLimit;
	}
}
