
package com.yunhuwifi.vcard;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class VCardParser_V30 extends VCardParser_V21 {
    private static final String LOG_TAG = "VCardParser_V30";
    
    private static final HashSet<String> acceptablePropsWithParam = new HashSet<String>(
            Arrays.asList(
                    "BEGIN", "LOGO", "PHOTO", "LABEL", "FN", "TITLE", "SOUND", 
                    "VERSION", "TEL", "EMAIL", "TZ", "GEO", "NOTE", "URL",
                    "BDAY", "ROLE", "REV", "UID", "KEY", "MAILER", // 2.1
                    "NAME", "PROFILE", "SOURCE", "NICKNAME", "CLASS",
                    "SORT-STRING", "CATEGORIES", "PRODID")); // 3.0
    
    private static final HashSet<String> sAcceptableEncodingV30 = new HashSet<String>(
            Arrays.asList("7BIT", "8BIT", "BASE64", "B"));
    
    private static final HashSet<String> acceptablePropsWithoutParam = new HashSet<String>();

    private String mPreviousLine;
    
    @Override
    protected String getVersion() {
        return "3.0";
    }
    
    @Override
    protected boolean isValidPropertyName(String propertyName) {
        if (!(acceptablePropsWithParam.contains(propertyName) ||
                acceptablePropsWithoutParam.contains(propertyName) ||
                propertyName.startsWith("X-")) &&
                !mWarningValueMap.contains(propertyName)) {
            mWarningValueMap.add(propertyName);
            Log.w(LOG_TAG, "Property name unsupported by vCard 3.0: " + propertyName);
        }
        return true;
    }
    
    @Override
    protected boolean isValidEncoding(String encoding) {
        return sAcceptableEncodingV30.contains(encoding.toUpperCase());
    }
    
    @Override
    protected String getLine() throws IOException {
        if (mPreviousLine != null) {
            String ret = mPreviousLine;
            mPreviousLine = null;
            return ret;
        } else {
            return mReader.readLine();
        }
    }
    
    @Override
    protected String getNonEmptyLine() throws IOException, VCardException {
        String line;
        StringBuilder builder = null;
        while (true) {
            line = mReader.readLine();
            if (line == null) {
                if (builder != null) {
                    return builder.toString();
                } else if (mPreviousLine != null) {
                    String ret = mPreviousLine;
                    mPreviousLine = null;
                    return ret;
                }
                throw new VCardException("Reached end of buffer.");
            } else if (line.length() == 0) {
                if (builder != null) {
                    return builder.toString();
                } else if (mPreviousLine != null) {
                    String ret = mPreviousLine;
                    mPreviousLine = null;
                    return ret;
                }
            } else if (line.charAt(0) == ' ' || line.charAt(0) == '\t') {
                if (builder != null) {
                    builder.append(line.substring(1));
                } else if (mPreviousLine != null) {
                    builder = new StringBuilder();
                    builder.append(mPreviousLine);
                    mPreviousLine = null;
                    builder.append(line.substring(1));
                } else {
                    throw new VCardException("Space exists at the beginning of the line");
                }
            } else {
                if (mPreviousLine == null) {
                    mPreviousLine = line;
                    if (builder != null) {
                        return builder.toString();
                    }
                } else {
                    String ret = mPreviousLine;
                    mPreviousLine = line;
                    return ret;
                }
            }
        }
    }
    
    
   
    @Override
    protected boolean readBeginVCard(boolean allowGarbage) throws IOException, VCardException {
        return super.readBeginVCard(allowGarbage);
    }
    
    @Override
    protected void readEndVCard(boolean useCache, boolean allowGarbage)
            throws IOException, VCardException {
        super.readEndVCard(useCache, allowGarbage);
    }

    @Override
    protected void handleParams(String params) throws VCardException {
        try {
            super.handleParams(params);
        } catch (VCardException e) {
            String[] strArray = params.split("=", 2);
            if (strArray.length == 2) {
                handleAnyParam(strArray[0], strArray[1]);
            } else {
                throw new VCardException(
                        "Unknown params value: " + params);
            }
        }
    }
    
    @Override
    protected void handleAnyParam(String paramName, String paramValue) {
        super.handleAnyParam(paramName, paramValue);
    }
    
    @Override
    protected void handleType(String ptypevalues) {
        String[] ptypeArray = ptypevalues.split(",");
        mBuilder.propertyParamType("TYPE");
        for (String value : ptypeArray) {
            int length = value.length();
            if (length >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                mBuilder.propertyParamValue(value.substring(1, value.length() - 1));
            } else {
                mBuilder.propertyParamValue(value);
            }
        }
    }

    @Override
    protected void handleAgent(String propertyValue) throws VCardException {
        
        throw new VCardException("AGENT in vCard 3.0 is not supported yet.");
    }
    
  
    @Override
    protected String getBase64(String firstString) throws IOException, VCardException {
        StringBuilder builder = new StringBuilder();
        builder.append(firstString);
        
        while (true) {
            String line = getLine();
            if (line == null) {
                throw new VCardException(
                        "File ended during parsing BASE64 binary");
            }
            if (line.length() == 0) {
                break;
            } else if (!line.startsWith(" ") && !line.startsWith("\t")) {
                mPreviousLine = line;
                break;
            }
            builder.append(line);
        }
        
        return builder.toString();
    }
    
  
    @Override
    protected String maybeUnescapeText(String text) {
        StringBuilder builder = new StringBuilder();
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char ch = text.charAt(i);
            if (ch == '\\' && i < length - 1) {
                char next_ch = text.charAt(++i); 
                if (next_ch == 'n' || next_ch == 'N') {
                    builder.append("\r\n");
                } else {
                    builder.append(next_ch);
                }
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }
    
    @Override
    protected String maybeUnescape(char ch) {
        if (ch == 'n' || ch == 'N') {
            return "\r\n";
        } else {
            return String.valueOf(ch);
        }
    }
}
