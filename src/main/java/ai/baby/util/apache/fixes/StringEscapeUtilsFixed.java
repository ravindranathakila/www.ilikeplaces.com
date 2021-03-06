package ai.baby.util.apache.fixes;

import ai.baby.util.apache.StringEscapeUtils;
import ai.scribble.License;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Mar 8, 2010
 * Time: 11:15:43 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class StringEscapeUtilsFixed extends StringEscapeUtils {

    public static boolean checkescapeJava(final String str) {
        return str.equals(escapeJava(str));
    }

    public static boolean checkescapeJavaScript(final String str) {
        return str.equals(escapeJavaScript(str));
    }

    public static boolean checkescapeHtml(final String str) {
        return str.equals(escapeHtml(str));
    }

    public static boolean checkescapeXml(final String str) {
        return str.equals(escapeXml(str));
    }

    public static String escapeXSS(final String str) {
        return escapeJavaScript(escapeXml(escapeHtml(str)));
    }
}
