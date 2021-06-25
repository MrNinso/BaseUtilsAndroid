package com.developer.base.utils.android.tools.data;

import java.util.regex.Pattern;

public class MaskString {
    private final String mMask;
    private final char mReplaceChar;
    private final Pattern mFilter;

    public MaskString(String mask) {
        this.mMask = mask;
        this.mReplaceChar = '#';
        this.mFilter = Pattern.compile(this.buildRegex(this.mMask));
    }

    public MaskString(String mask, char replaceChar) {
        this.mMask = mask;
        this.mReplaceChar = replaceChar;
        this.mFilter = Pattern.compile(this.buildRegex(this.mMask));
    }

    public String mask(String text) {
        if (text.length() == mMask.length())
            return text;

        String str = this.unmask(text);
        String m = this.mMask;

        for (int i = 0; i < str.length(); i++) {
            m = m.replaceFirst(String.format("[%s]", this.mReplaceChar), String.valueOf(str.charAt(i)));
        }

        m = m.replaceAll(String.format("%s.*", this.mReplaceChar), "");

        if (m.length() != 0)
            for (int i = m.length() - 1; i > 0; i--)
                if (this.mFilter.matcher(String.valueOf(m.charAt(i))).find()) {
                    m = m.substring(0, m.length() - 1);
                } else {
                    break;
                }

        return m;
    }

    public String unmask(String text) {
        return this.mFilter.matcher(text).replaceAll("");
    }

    private String buildRegex(String mask) {
        String filter = "";
        for (char m : this.mMask.toCharArray()) {
            if (m == this.mReplaceChar || filter.contains(String.valueOf(m))) {
                continue;
            }
            filter = String.format("%s|[%s]", filter, m);
        }

        return filter.replaceFirst("[|]", "");
    }
}
