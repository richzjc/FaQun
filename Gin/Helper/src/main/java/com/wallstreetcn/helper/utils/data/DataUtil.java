package com.wallstreetcn.helper.utils.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class DataUtil {
	public static final String EMPTY_STRING = "";
	public static final String NULL_STRING = "null";

	/** notEmpty */
	public static boolean notEmpty(String string) {
		return null != string && !EMPTY_STRING.equals(string) && !NULL_STRING.equals(string);
	}

	public static boolean notEmpty(Collection<?> collection) {
		if (null != collection) {
			Iterator<?> iterator = collection.iterator();
			if (null != iterator) {
				while (iterator.hasNext()) {
					if (null != iterator.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean notEmpty(Map<?, ?> map) {
		return null != map && !map.isEmpty();
	}

	public static boolean notEmpty(byte[] array) {
		return null != array && array.length > 0;
	}

	public static boolean notEmpty(short[] array) {
		return null != array && array.length > 0;
	}

	public static boolean notEmpty(int[] array) {
		return null != array && array.length > 0;
	}

	public static boolean notEmpty(long[] array) {
		return null != array && array.length > 0;
	}

	public static <T> boolean notEmpty(T[] array) {
		return null != array && array.length > 0;
	}
	
	
	/** contains */
	public static boolean contains(long value, long[] values) {
		if (notEmpty(values)) {
			for (long v : values) {
				if (v == value) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean contains(short value, short[] values) {
		if (notEmpty(values)) {
			for (long v : values) {
				if (v == value) {
					return true;
				}
			}
		}
		return false;
	}

	public static <E> boolean contains(E one, List<E> list) {
		if (notEmpty(list) && null != one) {
			for (E item : list) {
				if (one.equals(item)) {
					return true;
				}
			}
		}
		return false;
	}

	public static <E> boolean contains(E one, E[] many) {
		if (notEmpty(many) && null != one) {
			return Arrays.asList(many).contains(one);
		}
		return false;
	}


}
