package com.yunhuwifi.vcard;

public interface InputFilter
{
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend);
    public static class LengthFilter implements InputFilter {
        public LengthFilter(int max) {
            mMax = max;
        }

        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int keep = mMax - (dest.length() - (dend - dstart));

            if (keep <= 0) {
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                return source.subSequence(start, start + keep);
            }
        }

        private int mMax;
    }
}
