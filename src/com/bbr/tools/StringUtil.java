package com.bbr.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: chenjx
 * Date: 2011-6-11
 */
public class StringUtil {
    private static final String[] hexdigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String md5(String s) {

        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            md.reset();
            md.update(s.getBytes());
            byte[] encodedStr = md.digest();
            StringBuffer buf = new StringBuffer();

            for (byte anEncodedStr : encodedStr) {
                int n = ((int) anEncodedStr & 0xff);
                int d1 = n / 16;
                int d2 = n % 16;
                buf.append(hexdigits[d1]).append(hexdigits[d2]);
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNumeric(String s) {
        if (s == null || s.equals("")) {
            return false;
        }
        for (int i = s.length(); --i >= 0;) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 空格过滤，包含全角空格和半角空格
     *
     * @param s 字符串
     * @return 过滤空格（包含全角空格）后的字符串
     */
    public static String trim(String s) {
        if (s == null) {
            return s;
        }
        return s.replaceAll("[ |　]+", "");
    }

    /**
     * 判断字符串是否为空
     *
     * @param s 字符串
     * @return true：null，""， 只包含空格（包括全角空格）
     */
    public static boolean isEmpty(String s) {
        return s == null || trim(s).length() == 0;
    }

    public static String join(List<?> list, String delimiter) {
        if (list == null || list.size() <= 0) {
            return "";
        }
        if (delimiter == null || delimiter.isEmpty()) {
            delimiter = " ";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(list.get(0).toString());
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) == null) {
                continue;
            }
            buffer.append(delimiter).append(list.get(i).toString());
        }
        return buffer.toString();
    }

    public static String join(Object[] array, String delimiter) {
        List<Object> list;
        if (array == null || array.length == 0) {
            list = Collections.emptyList();
        } else {
            list = Arrays.asList(array);
        }
        return join(list, delimiter);
    }

    public static List<String> splitStringByDelimiter(String toBeSplit, String delimiter, String matchRegex) {
        List<String> resultList = new ArrayList<String>();
        if (toBeSplit != null && !toBeSplit.isEmpty()) {
            if (delimiter == null) {
                delimiter = "";
            }

            String[] items = toBeSplit.split(delimiter);
            if (matchRegex == null) {
                Collections.addAll(resultList, items);
            } else {
                for (String item : items) {
                    if (item.matches(matchRegex)) {
                        resultList.add(item);
                    }
                }
            }
        }
        return resultList;
    }

    public static String capitalize(String srcString) {
        if (srcString == null || srcString.isEmpty()) {
            return srcString;
        }
        String firstChar = srcString.substring(0, 1);
        return firstChar.toUpperCase() + (srcString.length() > 1 ? srcString.substring(1) : "");
    }

    public static boolean isEmail(String email) {
        if (StringUtil.isEmpty(email)) {
            return false;
        }
        Pattern p = Pattern.compile("^[_a-z0-9\\.\\-]+@([\\._a-z0-9\\-]+\\.)+[a-z0-9]{2,4}$");
        Matcher m = p.matcher(email.toLowerCase());
        return m.matches();
    }


    /**
     * js特殊字符转义
     * ' ===> \'
     * " ===> \"
     * & ===> \&
     * \ ===> \\
     * \n ===> \\n
     * \r ===> \\r
     * \t ===> \\t
     * \b ===> \\b
     * \f ===> \\f
     *
     * @param str
     * @return string
     */
    public static String jsEscape(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(str.length() + (str.length() / 2));
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (chr == '\'') {
                sb.append("\\'");
            } else if (chr == '"') {
                sb.append("\\\"");
            } else if (chr == '&') {
                sb.append("\\&");
            } else if (chr == '\\') {
                sb.append("\\\\");
            } else if (chr == '\n') {
                sb.append("\\n");
            } else if (chr == '\r') {
                sb.append("\\r");
            } else if (chr == '\t') {
                sb.append("\\t");
            } else if (chr == '\b') {
                sb.append("\\b");
            } else if (chr == '\f') {
                sb.append("\\f");
            } else {
                sb.append(chr);
            }
        }
        return sb.toString();
    }

    public static String breakword(String s, int maxlength) {
        StringBuffer sb = new StringBuffer();
        int start = -1;
        int end;
        for (int i = 0; i < s.length(); i++) {
            int ascCode = (int) s.charAt(i);
            sb.append(s.charAt(i));
            //8 9  10 13 32分别对应退格 制表 换行 回车 空格
            if (ascCode < 128 && ascCode != 8 && ascCode != 9 && ascCode != 10 && ascCode != 13 && ascCode != 32) {
                if (start == -1) {
                    start = i;
                }
                end = i;
                if (end - start == (maxlength - 1)) {
                    sb.append(" ");
                    start = -1;
                }
            }
        }
        return sb.toString();
    }

    public static byte[] hexString2Bytes(String src) {
        int length = src.length() / 2;
        byte[] result = new byte[length];
        char[] array = src.toCharArray();
        for (int i = 0; i < length; i++) {
            result[i] = uniteBytes(array[2 * i], array[2 * i + 1]);
        }
        return result;
    }

    public static String byte2HexString(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static String feedReplace(String content) {
        if(content == null) return "";
        content = content.replaceAll("(^\\s*)|(\\s*$)", "").replaceAll("(^　*)|(　*$)", "");
        content = content.replace("<", "&lt;");
        content = content.replace(">", "&gt;");
        content = content.replace("'", "&#39;");
        content = content.replace("\"", "&quot;");
        content = content.replace(" ", "&nbsp;");
        content = content.replaceAll("\r\n", "&nbsp;");
        content = content.replaceAll("\n", "&nbsp;");
        return content;
    }

    private static byte uniteBytes(char mostChar, char secondChar) {
        byte b0 = Byte.decode("0x" + String.valueOf(mostChar));
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + String.valueOf(secondChar));
        return (byte) (b0 | b1);
    }
}
