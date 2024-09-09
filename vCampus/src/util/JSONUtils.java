package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JSON相关工具函数。仅在.net包调用
 */
public class JSONUtils {

	protected final static List<Class> BASIC_CLASSES = new ArrayList<>(
			Arrays.asList(String.class, int.class, Integer.class, boolean.class, Boolean.class, double.class, Double.class,
					float.class, Float.class, long.class, Long.class));

	public final static boolean isBasicClass(Class<?> clazz) {
		return JSONUtils.BASIC_CLASSES.contains(clazz);
	}

}