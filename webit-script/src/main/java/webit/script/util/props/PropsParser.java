// Copyright (c) 2003-2013, Jodd Team (jodd.org). All Rights Reserved.
package webit.script.util.props;

import java.util.ArrayList;

/**
 * {@link Props} parser.
 */
final class PropsParser /* implements Cloneable*/ {

    private static final String PROFILE_LEFT = "<";
    private static final String PROFILE_RIGHT = ">";
    final PropsData propsData;
    /**
     * Value that will be inserted when escaping the new line.
     */
    private final static String escapeNewLineValue = "";
//	/**
//	 * Trims left the value.
//	 */
//	private final static boolean valueTrimLeft = true;
//
//	/**
//	 * Trims right the value.
//	 */
//	private final static boolean valueTrimRight = true;
    /**
     * Defines if starting whitespaces when value is split in the new line
     * should be ignored or not.
     */
    private final static boolean ignorePrefixWhitespacesOnNewLine = true;
    /**
     * Defines if multi-line values may be written using triple-quotes as in
     * python.
     */
    private final static boolean multilineValues = true;
    /**
     * Don't include empty properties.
     */
    private final static boolean skipEmptyProps = true;

    PropsParser() {
        this.propsData = new PropsData();
    }

//	public PropsParser(final PropsData propsData) {
//		this.propsData = propsData;
//	}
//
//	public PropsData getPropsData() {
//		return propsData;
//	}
//
//	@Override
//	public PropsParser clone() {
//		final PropsParser pp = new PropsParser(this.propsData.clone());
//
//		pp.escapeNewLineValue = escapeNewLineValue;
//		pp.valueTrimLeft = valueTrimLeft;
//		pp.valueTrimRight = valueTrimRight;
//		pp.ignorePrefixWhitespacesOnNewLine = ignorePrefixWhitespacesOnNewLine;
//		pp.skipEmptyProps = skipEmptyProps;
//		pp.multilineValues = multilineValues;
//
//		return pp;
//	}
    /**
     * Parsing states.
     */
    private enum ParseState {

        TEXT,
        ESCAPE,
        ESCAPE_NEWLINE,
        COMMENT,
        VALUE
    }

    /**
     * Loads properties.
     */
    void parse(final String in) {
        ParseState state = ParseState.TEXT;
        boolean insideSection = false;
        String currentSection = null;
        String key = null;
        boolean quickAppend = false;
        final StringBuilder sb = new StringBuilder();

        final int len = in.length();
        int ndx = 0;
        while (ndx < len) {
            final char c = in.charAt(ndx);
            ndx++;

            if (state == ParseState.COMMENT) {
                // comment, skip to the end of the line
                if (c == '\n') {
                    state = ParseState.TEXT;
                }
            } else if (state == ParseState.ESCAPE) {
                state = ParseState.VALUE;
                switch (c) {
                    case '\r':
                    case '\n':
                        // if the EOL is \n or \r\n, escapes both chars
                        state = ParseState.ESCAPE_NEWLINE;
                        break;
                    // encode UTF character
                    case 'u':
                        int value = 0;

                        for (int i = 0; i < 4; i++) {
                            final char hexChar = in.charAt(ndx++);
                            //if (CharUtil.isDigit(hexChar)) {
                            if (hexChar >= '0' && hexChar <= '9') {
                                value = (value << 4) + hexChar - '0';
                            } else if (hexChar >= 'a' && hexChar <= 'f') {
                                value = (value << 4) + 10 + hexChar - 'a';
                            } else if (hexChar >= 'A' && hexChar <= 'F') {
                                value = (value << 4) + 10 + hexChar - 'A';
                            } else {
                                throw new IllegalArgumentException("Malformed \\uXXXX encoding.");
                            }
                        }
                        sb.append((char) value);
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    default:
                        sb.append(c);
                }
            } else if (state == ParseState.TEXT) {
                switch (c) {
                    // start section
                    case '[':
                        sb.setLength(0);
                        insideSection = true;
                        break;

                    // end section
                    case ']':
                        if (insideSection) {
                            currentSection = sb.toString().trim();
                            sb.setLength(0);
                            insideSection = false;
                            if (currentSection.length() == 0) {
                                currentSection = null;
                            }
                        } else {
                            sb.append(c);
                        }
                        break;

                    case '#':
                    case ';':
                        state = ParseState.COMMENT;
                        break;

                    // assignment operator
                    case '+':
                        if (ndx == len || in.charAt(ndx) != '=') {
                            sb.append(c);
                            break;
                        }
                        // fall down
                        quickAppend = true;
                        ndx++;
                    case '=':
                    case ':':
                        if (key == null) {
                            key = sb.toString().trim();
                            sb.setLength(0);
                        } else {
                            sb.append(c);
                        }
                        state = ParseState.VALUE;
                        break;

                    case '\r':
                    case '\n':
                        add(currentSection, key, sb, true, quickAppend);
                        sb.setLength(0);
                        key = null;
                        quickAppend = false;
                        break;

                    case ' ':
                    case '\t':
                        // ignore whitespaces
                        break;
                    default:
                        sb.append(c);
                }
            } else {
                switch (c) {
                    case '\\':
                        // escape char, take the next char as is
                        state = ParseState.ESCAPE;
                        break;

                    case '\r':
                    case '\n':
                        if ((state == ParseState.ESCAPE_NEWLINE) && (c == '\n')) {
                            sb.append(escapeNewLineValue);
                            if (ignorePrefixWhitespacesOnNewLine == false) {
                                state = ParseState.VALUE;
                            }
                        } else {
                            add(currentSection, key, sb, true, quickAppend);
                            sb.setLength(0);
                            key = null;
                            quickAppend = false;

                            // end of value, continue to text
                            state = ParseState.TEXT;
                        }
                        break;

                    case ' ':
                    case '\t':
                        if ((state == ParseState.ESCAPE_NEWLINE)) {
                            break;
                        }
                    default:
                        sb.append(c);
                        state = ParseState.VALUE;

                        if (multilineValues) {
                            if (sb.length() == 3) {

                                // check for ''' beginning
                                if (sb.toString().equals("'''")) {
                                    sb.setLength(0);
                                    int endIndex = in.indexOf("'''", ndx);
                                    if (endIndex == -1) {
                                        endIndex = in.length();
                                    }
                                    sb.append(in, ndx, endIndex);

                                    // append
                                    add(currentSection, key, sb, false, quickAppend);
                                    sb.setLength(0);
                                    key = null;
                                    quickAppend = false;

                                    // end of value, continue to text
                                    state = ParseState.TEXT;
                                    ndx = endIndex + 3;
                                }
                            }
                        }
                }
            }
        }

        if (key != null) {
            add(currentSection, key, sb, true, quickAppend);
        }
    }

    /**
     * Adds accumulated value to key and current section.
     */
    private void add(
            final String section, final String key,
            final StringBuilder value, final boolean trim, final boolean append) {

        // ignore lines without : or =
        if (key == null) {
            return;
        }
        String fullKey = key;

        if (section != null) {
            fullKey = section + '.' + fullKey;
        }
        String v = value.toString();

        if (trim) {
//			if (valueTrimLeft && valueTrimRight) {
            v = v.trim();
//			} else if (valueTrimLeft) {
//				v = StringUtil.trimLeft(v);
//			} else {
//				v = StringUtil.trimRight(v);
//			}
        }

        if (v.length() == 0 && skipEmptyProps) {
            return;
        }

        add(fullKey, v, append);
    }

    /**
     * Adds key-value to properties and profiles.
     */
    private void add(final String key, final String value, final boolean append) {
        String fullKey = key;
        int ndx = fullKey.indexOf(PROFILE_LEFT);
        if (ndx == -1) {
            propsData.putBaseProperty(fullKey, value, append);
            return;
        }

        // extract profiles
        ArrayList<String> keyProfiles = new ArrayList<String>();

        while (true) {
            ndx = fullKey.indexOf(PROFILE_LEFT);
            if (ndx == -1) {
                break;
            }

            final int len = fullKey.length();

            int ndx2 = fullKey.indexOf(PROFILE_RIGHT, ndx + 1);
            if (ndx2 == -1) {
                ndx2 = len;
            }

            // remember profile
            final String profile = fullKey.substring(ndx + 1, ndx2);
            keyProfiles.add(profile);

            // extract profile from key
            ndx2++;
            final String right = (ndx2 == len) ? "" : fullKey.substring(ndx2);
            fullKey = fullKey.substring(0, ndx) + right;
        }

        // add value to extracted profiles
        for (final String p : keyProfiles) {
            propsData.putProfileProperty(fullKey, value, p, append);
        }
    }
}