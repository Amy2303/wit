// Copyright (c) 2013-2014, Webit Team. All Rights Reserved.
package webit.script.util;

import java.util.List;

/**
 *
 * @author Zqq
 */
public class StringUtil {

    public static String concat(String s1, String s2, String s3) {
        return s1.concat(s2).concat(s3);
    }

    public static String concat(String... strings) {
        int len = 0;
        int i;
        final int size;
        i = size = strings.length;
        while (i != 0) {
            --i;
            len += strings[i].length();
        }
        StringBuilder sb = new StringBuilder(len);
        for (i = 0; i < size; i++) {
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    public static String concat(String s1, String s2, String s3, String s4) {
        int len = s1.length() + s2.length() + s3.length() + s4.length();
        return new StringBuilder(len)
                .append(s1)
                .append(s2)
                .append(s3)
                .append(s4)
                .toString();
    }

    public static String concat(String string, int number) {
        return string.concat(Integer.toString(number));
    }

    public static String concatObjectClass(String string, Object object) {
        return string.concat(object != null ? object.getClass().getName() : "[null]");
    }

    public static String concat(String string, Object object) {
        return string.concat(object != null ? object.toString() : "null");
    }

    /**
     * Replaces all occurrences of a character in a string.
     *
     * @param s input string
     * @param sub character to replace
     * @param with character to replace with
     * @return String
     */
    public static String replaceChar(String s, char sub, char with) {
        int startIndex;
        if ((startIndex = s.indexOf(sub)) < 0) {
            return s;
        }
        char[] str = s.toCharArray();
        for (int i = startIndex; i < str.length; i++) {
            if (str[i] == sub) {
                str[i] = with;
            }
        }
        return new String(str);
    }

    /**
     * Tests if this string ends with the specified suffix.
     *
     * @param src String to test
     * @param subS suffix
     *
     * @return <code>true</code> if the character sequence represented by the
     * argument is a suffix of the character sequence represented by this
     * object; <code>false</code> otherwise.
     */
    public static boolean endsWithIgnoreCase(String src, String subS) {
        String sub = subS.toLowerCase();
        int sublen = sub.length();
        int j = 0;
        int i = src.length() - sublen;
        if (i < 0) {
            return false;
        }
        while (j < sublen) {
            char source = Character.toLowerCase(src.charAt(i));
            if (sub.charAt(j) != source) {
                return false;
            }
            j++;
            i++;
        }
        return true;
    }

    /**
     * Cuts prefix if exists.
     */
    public static String cutPrefix(String string, String prefix) {
        if (string.startsWith(prefix)) {
            string = string.substring(prefix.length());
        }
        return string;
    }

    /**
     *
     * @since 1.4.0
     */
    public static String cutFieldName(final String string, final int from) {
        final int nextIndex = from + 1;
        if (string.length() > nextIndex
                && CharUtil.isUppercaseAlpha(string.charAt(nextIndex))) {
            return string.substring(from);
        } else {
            return String.valueOf(CharUtil.toLowerAscii(string.charAt(from))).concat(string.substring(from + 1));
        }
    }

    /**
     * Joins list of iterable elements. Separator string may be
     * <code>null</code>.
     */
    public static String join(List elements, String separator) {
        if (elements == null) {
            return "";
        }
        int size = elements.size();
        if (size == 0) {
            return "";
        } else if (size == 1) {
            return String.valueOf(elements.get(0));
        }

        int len = separator.length() * (size - 1);
        String[] strings = new String[size];
        int i = 0;
        String str;
        for (Object element : elements) {
            str = String.valueOf(element);
            len += str.length();
            strings[i] = str;
            i++;
        }

        StringBuilder sb = new StringBuilder(len);
        boolean notfirst = false;
        for (int j = 0; j < size; j++) {
            if (notfirst) {
                sb.append(separator);
            } else {
                notfirst = true;
            }
            sb.append(strings[j]);
        }
        return sb.toString();
    }

    public static String prefixChar(String string, char prefix) {
        if (string != null && string.length() > 0) {
            return string.charAt(0) == prefix
                    ? string
                    : prefix + string;
        } else {
            return String.valueOf(prefix);
        }
    }

    public static void trimAll(final String[] strings) {
        if (strings != null) {
            String item;
            for (int i = 0, len = strings.length; i < len; i++) {
                if ((item = strings[i]) != null) {
                    strings[i] = item.trim();
                }
            }
        }
    }

    /**
     * Splits a string in several parts (tokens) that are separated by single
     * delimiter characters. Delimiter is always surrounded by two strings.
     *
     * @param src source to examine
     * @param delimiter delimiter character
     *
     * @return array of tokens
     */
    public static String[] splitc(String src, char delimiter) {
        if (src.length() == 0) {
            return new String[]{""};
        }
        char[] srcc = src.toCharArray();

        int maxparts = srcc.length + 1;
        int[] start = new int[maxparts];
        int[] end = new int[maxparts];

        int count = 0;

        start[0] = 0;
        int s = 0, e;
        if (srcc[0] == delimiter) {	// string starts with delimiter
            end[0] = 0;
            count++;
            s = CharUtil.findFirstDiff(srcc, 1, delimiter);
            if (s == -1) {							// nothing after delimiters
                return new String[]{"", ""};
            }
            start[1] = s;							// new start
        }
        while (true) {
            // find new end
            e = CharUtil.findFirstEqual(srcc, s, delimiter);
            if (e == -1) {
                end[count] = srcc.length;
                break;
            }
            end[count] = e;

            // find new start
            count++;
            s = CharUtil.findFirstDiff(srcc, e, delimiter);
            if (s == -1) {
                start[count] = end[count] = srcc.length;
                break;
            }
            start[count] = s;
        }
        count++;
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = src.substring(start[i], end[i]);
        }
        return result;
    }

    public static String[] splitc(String src, char[] delimiters) {
        if ((delimiters.length == 0) || (src.length() == 0)) {
            return new String[]{src};
        }
        char[] srcc = src.toCharArray();

        int maxparts = srcc.length + 1;
        int[] start = new int[maxparts];
        int[] end = new int[maxparts];

        int count = 0;

        start[0] = 0;
        int s = 0, e;
        if (CharUtil.equalsOne(srcc[0], delimiters)) {	// string starts with delimiter
            end[0] = 0;
            count++;
            s = CharUtil.findFirstDiff(srcc, 1, delimiters);
            if (s == -1) {							// nothing after delimiters
                return new String[]{"", ""};
            }
            start[1] = s;							// new start
        }
        while (true) {
            // find new end
            e = CharUtil.findFirstEqual(srcc, s, delimiters);
            if (e == -1) {
                end[count] = srcc.length;
                break;
            }
            end[count] = e;

            // find new start
            count++;
            s = CharUtil.findFirstDiff(srcc, e, delimiters);
            if (s == -1) {
                start[count] = end[count] = srcc.length;
                break;
            }
            start[count] = s;
        }
        count++;
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = src.substring(start[i], end[i]);
        }
        return result;
    }

    private final static char[] DEFAULT_DELIMITERS = ",\n\r".toCharArray();

    public static String[] splitc(String src) {
        return splitc(src, DEFAULT_DELIMITERS);
    }

    public static String[] splitAndRemoveBlank(String src) {
        if (src == null) {
            return null;
        }
        final String[] array = splitc(src, DEFAULT_DELIMITERS);
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            String item = array[i].trim();
            if (item.length() == 0) {
                continue;
            }
            array[count++] = item;
        }
        if (count != array.length) {
            return ArrayUtil.subarray(array, 0, count);
        }
        return array;
    }

    public static String[] splitAndTrimAll(String src) {
        final String[] result = splitc(src, DEFAULT_DELIMITERS);
        trimAll(result);
        return result;
    }

    public static boolean isBlank(String string) {
        if (string == null) {
            return true;
        }
        for (int i = 0, size = string.length(); i < size; i++) {
            if (CharUtil.isWhitespace(string.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses string template and replaces macros with resolved values.
     */
    public static String format(String template, Object... array) {
        if (template == null) {
            return null;
        }
        if (template.indexOf('{') <0) {
            return template;
        }
        StringBuilder result = new StringBuilder(template.length());
        int i = 0;
        int len = template.length();
        int currentIndex = 0;
        int arrayLen = array != null ? array.length : 0;
        int index;
        int j;
        int escapeCharcount;
        String value;
        while (i < len) {
            int ndx = template.indexOf('{', i);
            if (ndx == -1) {
                result.append(i == 0 ? template : template.substring(i));
                break;
            }
            j = ndx - 1;
            while ((j >= 0) && (template.charAt(j) == '\\')) {
                j--;
            }
            escapeCharcount = ndx - 1 - j;
            if (escapeCharcount > 0) {
                result.append(template.substring(i, ndx - ((escapeCharcount + 1) >> 1)));
            } else {
                result.append(template.substring(i, ndx));
            }
            if ((escapeCharcount & 1) == 1) {
                result.append('{');
                i = ndx + 1;
                continue;
            }
            ndx += 1;
            int ndx_end = template.indexOf('}', ndx);
            if (ndx_end == -1) {
                throw new IllegalArgumentException(StringUtil.concat("Invalid string template, unclosed macro at: ", ndx - 1));
            }
            if (ndx == ndx_end) {
                index = currentIndex++;
            } else {
                try {
                    index = template.charAt(ndx) - '0';
                    for (int k = ndx + 1; k < ndx_end; k++) {
                        index = index * 10 + (template.charAt(k) - '0');
                    }
                } catch (Exception e) {
                    index = -1;
                }
            }
            if (index < arrayLen && index >= 0 && array[index] != null) {
                value = array[index].toString();
                result.append(value);
            }
            i = ndx_end + 1;
        }
        return result.toString();
    }
}
