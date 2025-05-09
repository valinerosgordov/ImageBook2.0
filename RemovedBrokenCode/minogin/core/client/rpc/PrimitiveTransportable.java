package ru.minogin.core.client.rpc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import ru.minogin.core.client.i18n.MultiString;
import ru.minogin.core.client.model.IdMap;

@SuppressWarnings( { "serial", "unused" })
public class PrimitiveTransportable implements Transportable {
	private String string;
	private Integer integer;
	private Long longValue;
	private Boolean booleanValue;
	private Float floatValue;
	private Double doubleValue;
	private Date date;
	private Timestamp timestamp;
	private BigDecimal decimal;
	private MultiString ms;
	private ArrayList<Transportable> arrayList;
	private HashMap<Transportable, Transportable> hashMap;
	private LinkedHashMap<Transportable, Transportable> linkedHashMap;
	private HashSet<Transportable> hashSet;
	private TreeSet<Transportable> treeSet;
	private LinkedHashSet<Transportable> linkedHashSet;
	private IdMap<IdTransportable> idList;
}
