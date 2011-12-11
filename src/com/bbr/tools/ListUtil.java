package com.bbr.tools;

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * List的集合运算工具函数
 * User: chenjx
 * Date: 2011-5-9
 */
public class ListUtil {

    public final static String EMPTY_STRING = "";
    public final static String DEFAULT_DELIMITER = ",";

    /**
     * 求交集
     */
    public static <T> List<T> intersect(List<T> a, List<T> b) {
        List<T> ret = new ArrayList<T>(20);
        if (a == null || b == null || a.size() < 1 || b.size() < 1) return ret;
        Map<T, T> inter = null; //循环体内比较map，应该是项目多的那个列表
        List<T> outer = null;
        if (a.size() >= b.size()) {
            inter = genMap(a);
            outer = b;
        } else {
            inter = genMap(b);
            outer = a;
        }
        for (T t : outer) {
            if (inter.containsKey(t)) ret.add(t);
        }
        return ret;
    }

    /**
     * 求差集，即a-b
     */
    public static <T> List<T> subtract(List<T> a, List<T> b) {
        List<T> ret = new ArrayList<T>(a);
        if (a == null || b == null || a.size() < 1 || b.size() < 1) return ret;
        for (T t : a) {
            if (b.contains(t)) ret.remove(t);
        }
        return ret;
    }

    /**
     * 求并集，即a+b
     */
    public static <T> List<T> union(List<T> a, List<T> b) {
        List<T> ret = new ArrayList<T>(20);
        if (a == null) a = new ArrayList<T>(0);
        if (b == null) b = new ArrayList<T>(0);
        ret.addAll(a);
        for (T t : b) {
            if (!a.contains(t)) ret.add(t);
        }
        return ret;
    }

    /**
     * 把List生成Map，用于内部需要对list内容快速查找
     */
    public static <T> Map<T, T> genMap(List<T> list) {
        Map<T, T> ret = new HashMap<T, T>(128);
        for (T t : list) {
            ret.put(t, t);
        }
        return ret;
    }

    /**
     * List to String
     * join a list by delimiter
     *
     * @param list      the list
     * @param delimiter delimiter
     * @param <T>       Data type of the list
     * @return a string
     */
    public static <T> String join(List<T> list, String delimiter) {
        if (null == list || list.size() == 0) {
            return EMPTY_STRING;
        }
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = DEFAULT_DELIMITER;
        }
        StringBuilder ids = new StringBuilder();
        for (int i = 0, endIndex = list.size() - 1; ; i++) {
            ids.append(list.get(i));
            if (i == endIndex) {
                return ids.toString();
            }
            ids.append(delimiter);
        }
    }

    /**
     * List to String
     * join a list by the {@link ListUtil#DEFAULT_DELIMITER}
     *
     * @param list the list
     * @param <T>  Data type of the list
     * @return a string
     */
    public static <T> String join(List<T> list) {
        return join(list, DEFAULT_DELIMITER);
    }

    /**
     * String to List
     *
     * @param src       string
     * @param delimiter delimiter
     * @return a list
     */
    public static List<String> str2list(String src, String delimiter) {
        if (StringUtils.isEmpty(src)) {
            return Collections.emptyList();
        }
        if (StringUtils.isEmpty(delimiter)) {
            delimiter = DEFAULT_DELIMITER;
        }
        String[] a = src.split(delimiter);
        return Arrays.asList(a);
    }

    /**
     * String to List
     *
     * @param src string
     * @return a list
     */
    public static List<String> str2list(String src) {
        return str2list(src, DEFAULT_DELIMITER);
    }
}
