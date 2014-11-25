

package com.yunhuwifi.vcard;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VCardSourceDetector implements VBuilder {
    static final int TYPE_UNKNOWN = 0;
    static final int TYPE_APPLE = 1;
    static final int TYPE_JAPANESE_MOBILE_PHONE = 2;  
    static final int TYPE_FOMA = 3;  
    static final int TYPE_WINDOWS_MOBILE_JP = 4;

    private static Set<String> APPLE_SIGNS = new HashSet<String>(Arrays.asList(
            "X-PHONETIC-FIRST-NAME", "X-PHONETIC-MIDDLE-NAME", "X-PHONETIC-LAST-NAME",
            "X-ABADR", "X-ABUID"));
    
    private static Set<String> JAPANESE_MOBILE_PHONE_SIGNS = new HashSet<String>(Arrays.asList(
            "X-GNO", "X-GN", "X-REDUCTION"));
    
    private static Set<String> WINDOWS_MOBILE_PHONE_SIGNS = new HashSet<String>(Arrays.asList(
            "X-MICROSOFT-ASST_TEL", "X-MICROSOFT-ASSISTANT", "X-MICROSOFT-OFFICELOC"));
    private static Set<String> FOMA_SIGNS = new HashSet<String>(Arrays.asList(
            "X-SD-VERN", "X-SD-FORMAT_VER", "X-SD-CATEGORIES", "X-SD-CLASS", "X-SD-DCREATED",
            "X-SD-DESCRIPTION"));
    private static String TYPE_FOMA_CHARSET_SIGN = "X-SD-CHAR_CODE";
    
    private int mType = TYPE_UNKNOWN;
    private boolean mNeedParseSpecifiedCharset;
    private String mSpecifiedCharset;
    
    public void start() {
    }
    
    public void end() {
    }

    public void startRecord(String type) {
    }    

    public void startProperty() {
        mNeedParseSpecifiedCharset = false;
    }
    
    public void endProperty() {
    }

    public void endRecord() {
    }

    public void propertyGroup(String group) {
    }
    
    public void propertyName(String name) {
        if (name.equalsIgnoreCase(TYPE_FOMA_CHARSET_SIGN)) {
            mType = TYPE_FOMA;
            mNeedParseSpecifiedCharset = true;
            return;
        }
        if (mType != TYPE_UNKNOWN) {
            return;
        }
        if (WINDOWS_MOBILE_PHONE_SIGNS.contains(name)) {
            mType = TYPE_WINDOWS_MOBILE_JP;
        } else if (FOMA_SIGNS.contains(name)) {
            mType = TYPE_FOMA;
        } else if (JAPANESE_MOBILE_PHONE_SIGNS.contains(name)) {
            mType = TYPE_JAPANESE_MOBILE_PHONE;
        } else if (APPLE_SIGNS.contains(name)) {
            mType = TYPE_APPLE;
        }
    }

    public void propertyParamType(String type) {
    }

    public void propertyParamValue(String value) {
    }

    public void propertyValues(List<String> values) {
        if (mNeedParseSpecifiedCharset && values.size() > 0) {
            mSpecifiedCharset = values.get(0);
        }
    }

    int getType() {
        return mType;
    }
    

    public String getEstimatedCharset() {
        if (mSpecifiedCharset != null) {
            return mSpecifiedCharset;
        }
        switch (mType) {
        case TYPE_WINDOWS_MOBILE_JP:
        case TYPE_FOMA:
        case TYPE_JAPANESE_MOBILE_PHONE:
            return "SHIFT_JIS";
        case TYPE_APPLE:
            return "UTF-8";
        default:
            return null;
        }
    }
}
