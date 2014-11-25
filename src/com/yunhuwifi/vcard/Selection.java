
package com.yunhuwifi.vcard;



public class Selection {
    private Selection() {  }

    public static final int getSelectionStart(CharSequence text) {
        if (text instanceof Spanned)
            return ((Spanned) text).getSpanStart(SELECTION_START);
        else
            return -1;
    }
   
    public static final int getSelectionEnd(CharSequence text) {
        if (text instanceof Spanned)
            return ((Spanned) text).getSpanStart(SELECTION_END);
        else
            return -1;
    }

    public static void setSelection(Spannable text, int start, int stop) {

        int ostart = getSelectionStart(text);
        int oend = getSelectionEnd(text);
    
        if (ostart != start || oend != stop) {
            text.setSpan(SELECTION_START, start, start,
                         Spanned.SPAN_POINT_POINT|Spanned.SPAN_INTERMEDIATE);
            text.setSpan(SELECTION_END, stop, stop,
                         Spanned.SPAN_POINT_POINT);
        }
    }

    public static final void setSelection(Spannable text, int index) {
        setSelection(text, index, index);
    }

    public static final void selectAll(Spannable text) {
        setSelection(text, 0, text.length());
    }

    public static final void extendSelection(Spannable text, int index) {
        if (text.getSpanStart(SELECTION_END) != index)
            text.setSpan(SELECTION_END, index, index, Spanned.SPAN_POINT_POINT);
    }

    public static final void removeSelection(Spannable text) {
        text.removeSpan(SELECTION_START);
        text.removeSpan(SELECTION_END);
    }

    private static final class START implements NoCopySpan { };
    private static final class END implements NoCopySpan { };
    

    public static final Object SELECTION_START = new START();
    public static final Object SELECTION_END = new END();
}
