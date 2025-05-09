package ru.minogin.core.client.gwt.crypto;

public class UUID {
	private String value;

	public UUID() {
		value = randomUUID();
	}

	private native String randomUUID() /*-{
		UUIDgetIntegerBits = function(val,start,end){
			var base16 = UUIDreturnBase(val,16);
			var quadArray = new Array();
			var quadString = '';
			var i = 0;
			for(i=0;i<base16.length;i++){
				quadArray.push(base16.substring(i,i+1));
			}
			for(i=Math.floor(start/4);i<=Math.floor(end/4);i++){
				if(!quadArray[i] || quadArray[i] == '') quadString += '0';
				else quadString += quadArray[i];
			}
			return quadString;
		}

		UUIDreturnBase = function(number, base){
			return (number).toString(base).toUpperCase();
		}

		UUIDrand = function(max){
			return Math.floor(Math.random() * (max + 1));
		}

		var dg = new Date(1582, 10, 15, 0, 0, 0, 0);
		var dc = new Date();
		var t = dc.getTime() - dg.getTime();
		var h = '-';
		var tl = UUIDgetIntegerBits(t,0,31);
		var tm = UUIDgetIntegerBits(t,32,47);
		var thv = UUIDgetIntegerBits(t,48,59) + '1'; // version 1, security version is 2
		var csar = UUIDgetIntegerBits(UUIDrand(4095),0,7);
		var csl = UUIDgetIntegerBits(UUIDrand(4095),0,7);

		// since detection of anything about the machine/browser is far to buggy,
		// include some more random numbers here
		// if NIC or an IP can be obtained reliably, that should be put in
		// here instead.
		var n = UUIDgetIntegerBits(UUIDrand(8191),0,7) +
				UUIDgetIntegerBits(UUIDrand(8191),8,15) +
				UUIDgetIntegerBits(UUIDrand(8191),0,7) +
				UUIDgetIntegerBits(UUIDrand(8191),8,15) +
				UUIDgetIntegerBits(UUIDrand(8191),0,15); // this last number is two octets int
		return tl + h + tm + h + thv + h + csar + csl + h + n;
	}-*/;

	@Override
	public String toString() {
		return value;
	}
}