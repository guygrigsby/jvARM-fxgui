package com.guygrigsby.jvarm.fxgui;

import java.util.Comparator;

public class RegisterNameComparator implements Comparator<String>{

	@Override
	public int compare(String o1, String o2) {
		if (isFlag(o1) && isFlag(o2)) {
			return o1.compareTo(o2);
		}
		if (isFlag(o1)) {
			return 1;
		} else if (isFlag(o2)) {
			return -1;
		}
		Integer i1 = Integer.parseInt(o1.substring(1));
		Integer i2 = Integer.parseInt(o2.substring(1));
		return i1.compareTo(i2);
	}
	
	private boolean isFlag(String name) {
		return name.equals("N") || name.equals("Z") || name.equals("C") || name.equals("V");
	}

}
