package com.bbr.tools;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Title: HTML相关的正则表达式工具类
 * </p>
 * <p>
 * Description: 包括过滤HTML标记，转换HTML标记，替换特定HTML标记
 * </p>
 *
 * @author : chenjx
 *         Date: 2011-6-9
 */
public class HtmlFilterUtil {
    private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
    private final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签
    private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性

    public final static String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\s\S]*?<\/script>
    public final static String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\s\S]*?<\/style>
    public final static String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

    private HtmlFilterUtil() {
    }

    /**
     * 基本功能：替换标记以正常显示
     *
     * @param input
     * @return String
     */
    public static String replaceTag(String input) {
        if (!hasSpecialChars(input)) {
            return input;
        }
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i <= input.length() - 1; i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt;");
                    break;
                case '>':
                    filtered.append("&gt;");
                    break;
                case '"':
                    filtered.append("&quot;");
                    break;
                case '&':
                    filtered.append("&amp;");
                    break;
                default:
                    filtered.append(c);
            }

        }
        return (filtered.toString());
    }

    /**
     * 基本功能：判断标记是否存在
     *
     * @param input
     * @return boolean
     */
    public static boolean hasSpecialChars(String input) {
        boolean flag = false;
        if ((input != null) && (input.length() > 0)) {
            char c;
            for (int i = 0; i <= input.length() - 1; i++) {
                c = input.charAt(i);
                switch (c) {
                    case '>':
                        flag = true;
                        break;
                    case '<':
                        flag = true;
                        break;
                    case '"':
                        flag = true;
                        break;
                    case '&':
                        flag = true;
                        break;
                }
            }
        }
        return flag;
    }

    /**
     * 基本功能：过滤所有以"<"开头以">"结尾的标签
     *
     * @param str
     * @return String
     */
    public static String filterAllHtmlTag(String str) {
        Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 比 filterAllHtmlTag 更智能，对script style 标签的处理更干净
     *
     * @param inputString 输入html
     * @return 过滤干净的字符串
     */
    public static String html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = htmlStr;
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try {
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("HtmlFilterUtil.html2Text ex: " + e.getMessage());
        }
        return textStr;// 返回文本字符串
    }

    public static String filterHtml(String src) {
        return StringEscapeUtils.escapeHtml(src);
    }

    /**
     * 基本功能：过滤指定标签
     *
     * @param str
     * @param tag 指定标签
     * @return String
     */
    public static String filterSpecialHtmlTag(String str, String tag) {
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 基本功能：替换指定的标签,如替换img标签的src属性值为[img]属性值[/img]
     * <p/>
     *
     * @param str
     * @param beforeTag 要替换的标签
     * @param tagAttrib 要替换的标签属性值
     * @param startTag  新标签开始标记
     * @param endTag    新标签结束标记
     * @return String
     */
    public static String replaceSpecialHtmlTag(String str, String beforeTag,
                                               String tagAttrib, String startTag, String endTag) {
        String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
        String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regxpForTag);
        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer();
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag
                    .group(1));
            if (matcherForAttrib.find()) {
                matcherForAttrib.appendReplacement(sbreplace, startTag
                        + matcherForAttrib.group(1) + endTag);
            }
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

    /**
     * 删除掉字符串中的某个标签
     */
    public static String removeTag(String tagName, String str) {
        if (str == null)
            return "";
        if (tagName == null)
            return str;
        tagName = tagName.toLowerCase();
        String beginTagPattern = "<" + tagName;
        String endTagPattern = "</" + tagName + ">";
        String endOfTag = "/>";

        int beginTagIndex = -1;
        int endTagIndex = -1;
        int endTagEndIndex = -1;
        while (true) {
            beginTagIndex = str.toLowerCase().indexOf(beginTagPattern);
            if (beginTagIndex < 0) {
                break;
            }
            endTagIndex = str.toLowerCase().indexOf(endTagPattern,
                    beginTagIndex + 1);
            if (endTagIndex < 0)/* 如果没有尾标签 */ {
                int endOfTagIndex = str.indexOf(endOfTag, beginTagIndex + 1);
                if (endOfTagIndex < 0)/* 如果连 /> 都没有 */ {
                    endTagEndIndex = str.indexOf(">", beginTagIndex + 1);
                } else {
                    endTagEndIndex = str.indexOf(endOfTag, beginTagIndex + 1) + 1;
                }
            } else {
                endTagEndIndex = str.indexOf(">", endTagIndex + 1);
            }

            str = str.substring(0, beginTagIndex) + str.substring(endTagEndIndex + 1);
        }
        return str;
    }

    public static String getSummarize(String s, String enc, int limit) {
        if (s == null)
            return null;

        String cont = s.replaceAll("\n", "").replaceAll("\r", "").replaceAll(
                "\n\r", "").replaceAll("\t", "").replaceAll("&nbsp;", "")
                .replaceAll("　", "").replaceAll(" ", "");
        String cont1 = removeTag("div", cont);
        cont1 = removeTag("a", cont1);
        cont1 = removeTag("img", cont1);
        // cont1 = ToolKit.removeTag("p",cont1); //notice: pls donot filter the "<p></p>"
        cont1 = removeTag("br", cont1);
        cont1 = cont1.trim();
        return summarize(cont1, enc, limit);
    }

    /**
     * 较完美的摘要函数。从text文本中，获取较完整的摘要，注意尽量以标点符号结尾。
     * @param s
     * @param enc
     * @param limit
     * @return
     */
    public static String summarize(String s, String enc, int limit) {
        String resstr = "...";
        int pos = limit;
        String tem_content = null;
        try {
            tem_content = s;
            int segment = 20;
            String[] seprator = {"。", ".", "！", "!", "？", "?", "：", ":", "；", ";", "，", ",", "、"};
            if (tem_content == null)
                return null;
            if (limit < 0)
                limit = 0;

            tem_content = delComment(tem_content, limit + segment);

            if (tem_content.length() <= limit)
                return tem_content;
            if (enc != null)
                for (int i = 0; i < seprator.length; i++)
                    seprator[i] = new String(seprator[i].getBytes(), enc);
            boolean finish = false;
            for (int last = limit, first = last - segment; !finish
                    && (first >= 0); last -= segment, first -= segment)
                for (int i = 0; i < seprator.length; i++) {
                    String substr = tem_content.substring(first, last);
                    pos = substr.lastIndexOf(seprator[i]);
                    if (pos != -1) {
                        finish = true;
                        pos += first;

                        break;
                    }
                }
            if (pos == -1) {
                resstr = tem_content.substring(0, limit);
            } else {
                resstr = tem_content.substring(0, pos);
            }
            return resstr;
        } catch (Exception e) {
            if (pos >= 0)
                resstr = tem_content.substring(0, pos);
            else
                resstr = tem_content.substring(0, limit);
            return resstr;
        }
    }

    public static String delComment(String s, int len) {
        String[] markstr = {"<", ">"};
        String resstr = "";
        String currentStr;
        if (s == null)
            return s;
        currentStr = s;
        int alllen = s.length();
        int currentLen = 0, beginIndex = 0, endIndex = 0, getLen = 0;

        while (getLen < len && currentLen < alllen) {
            currentLen = currentStr.indexOf(markstr[0], beginIndex);
            endIndex = currentLen;
            if (currentLen >= 0) {
                getLen = getLen + (endIndex - beginIndex);
                if (currentLen + 1 < alllen) {
                    if (currentStr.charAt(currentLen + 1) == 'p'
                            || currentStr.charAt(currentLen + 1) == 'P'
                            || currentStr.startsWith("br", currentLen + 1)
                            || currentStr.startsWith("BR", currentLen + 1))
                        // resstr =
                        // resstr+currentStr.substring(beginIndex,endIndex)+"<br>";
                        resstr = resstr + currentStr.substring(beginIndex, endIndex);
                    else
                        resstr = resstr + currentStr.substring(beginIndex, endIndex);
                } else
                    resstr = resstr + currentStr.substring(beginIndex, endIndex);
                endIndex = currentStr.indexOf(markstr[1], endIndex);
                if (endIndex < 0)
                    break;
                beginIndex = endIndex + 1;
            } else {
                int lens = len - getLen;
                if (lens > 0) {
                    endIndex = ((beginIndex + lens) >= alllen) ? alllen
                            : beginIndex + lens;

                    if (endIndex > beginIndex)
                        resstr = resstr
                                + currentStr.substring(beginIndex, endIndex);
                }
                break;
            }
        }
        return resstr;
    }
}

