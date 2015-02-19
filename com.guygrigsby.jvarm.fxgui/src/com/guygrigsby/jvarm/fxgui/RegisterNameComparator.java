package com.guygrigsby.jvarm.fxgui;

import java.util.Comparator;

public class RegisterNameComparator implements Comparator<String>{

	@Override
	public int compare(String o1, String o2) {
		if (o1.equals("CPSR")) {
			return 1;
		} else if (o2.equals("CPSR")) {
			return -1;
		}
		Integer i1 = Integer.parseInt(o1.substring(1));
		Integer i2 = Integer.parseInt(o2.substring(1));
		return i1.compareTo(i2);
	}

}
