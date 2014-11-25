
package com.yunhuwifi.vcard;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class VCardParser_V21 {
    private static final String LOG_TAG = "VCardParser_V21";
    
	public static final String DEFAULT_CHARSET = "UTF-8";
    
    private static final HashSet<String> sKnownTypeSet = new HashSet<String>(
            Arrays.asList("DOM", "INTL", "POSTAL", "PARCEL", "HOME", "WORK",
                    "PREF", "VOICE", "FAX", "MSG", "CELL", "PAGER", "BBS",
                    "MODEM", "CAR", "ISDN", "VIDEO", "AOL", "APPLELINK",
                    "ATTMAIL", "CIS", "EWORLD", "INTERNET", "IBMMAIL",
                    "MCIMAIL", "POWERSHARE", "PRODIGY", "TLX", "X400", "GIF",
                    "CGM", "WMF", "BMP", "MET", "PMB", "DIB", "PICT", "TIFF",
                    "PDF", "PS", "JPEG", "QTIME", "MPEG", "MPEG2", "AVI",
                    "WAVE", "AIFF", "PCM", "X509", "PGP"));

    private static final HashSet<String> sKnownValueSet = new HashSet<String>(
            Arrays.asList("INLINE", "URL", "CONTENT-ID", "CID"));
        
    private static final HashSet<String> sAvailablePropertyNameV21 =
        new HashSet<String>(Arrays.asList(
                "BEGIN", "LOGO", "PHOTO", "LABEL", "FN", "TITLE", "SOUND",
                "VERSION", "TEL", "EMAIL", "TZ", "GEO", "NOTE", "URL",
                "BDAY", "ROLE", "REV", "UID", "KEY", "MAILER"));

    private static final HashSet<String> sAvailableEncodingV21 =
        new HashSet<String>(Arrays.asList(
                "7BIT", "8BIT", "QUOTED-PRINTABLE", "BASE64", "B"));
    
    private String mPreviousLine;
    
    protected VBuilder mBuilder = null;

    protected String mEncoding = null;
    
    protected final String sDefaultEncoding = "8BIT";
    
    protected BufferedReader mReader;
    
    private boolean mCanceled;
    
    private int mNestCount;
    
    protected HashSet<String> mWarningValueMap = new HashSet<String>();
    
    private long mTimeTotal;
    private long mTimeStartRecord;
    private long mTimeEndRecord;
    private long mTimeStartProperty;
    private long mTimeEndProperty;
    private long mTimeParseItems;
    private long mTimeParseItem1;
    private long mTimeParseItem2;
    private long mTimeParseItem3;
    private long mTimeHandlePropertyValue1;
    private long mTimeHandlePropertyValue2;
    private long mTimeHandlePropertyValue3;
    
   
    public VCardParser_V21() {
        super();
    }

    public VCardParser_V21(VCardSourceDetector detector) {
        super();
        if (detector != null && detector.getType() == VCardSourceDetector.TYPE_FOMA) {
            mNestCount = 1;
        }
    }
    
    
    protected void parseVCardFile() throws IOException, VCardException {
        boolean firstReading = true;
        while (true) {
            if (mCanceled) {
                break;
            }
            if (!parseOneVCard(firstReading)) {
                break;
            }
            firstReading = false;
        }

        if (mNestCount > 0) {
            boolean useCache = true;
            for (int i = 0; i < mNestCount; i++) {
                readEndVCard(useCache, true);
                useCache = false;
            }
        }
    }

    protected String getVersion() {
        return "2.1";
    }
    
    
    protected boolean isValidPropertyName(String propertyName) {
        if (!(sAvailablePropertyNameV21.contains(propertyName.toUpperCase()) ||
                propertyName.startsWith("X-")) && 
                !mWarningValueMap.contains(propertyName)) {
            mWarningValueMap.add(propertyName);
            Log.w(LOG_TAG, "Property name unsupported by vCard 2.1: " + propertyName);
        }
        return true;
    }

   
    protected boolean isValidEncoding(String encoding) {
        return sAvailableEncodingV21.contains(encoding.toUpperCase());
    }
    
   
    protected String getLine() throws IOException {
        return mReader.readLine();
    }
    
    protected String getNonEmptyLine() throws IOException, VCardException {
        String line;
        while (true) {
            line = getLine();
            if (line == null) {
                throw new VCardException("Reached end of buffer.");
            } else if (line.trim().length() > 0) {                
                return line;
            }
        }
    }
    
    
    private boolean parseOneVCard(boolean firstReading) throws IOException, VCardException {
        boolean allowGarbage = false;
        if (firstReading) {
            if (mNestCount > 0) {
                for (int i = 0; i < mNestCount; i++) {
                    if (!readBeginVCard(allowGarbage)) {
                        return false;
                    }
                    allowGarbage = true;
                }
            }
        }

        if (!readBeginVCard(allowGarbage)) {
            return false;
        }
        long start;
        if (mBuilder != null) {
            start = System.currentTimeMillis();
            mBuilder.startRecord("VCARD");
            mTimeStartRecord += System.currentTimeMillis() - start;
        }
        start = System.currentTimeMillis();
        parseItems();
        mTimeParseItems += System.currentTimeMillis() - start;
        readEndVCard(true, false);
        if (mBuilder != null) {
            start = System.currentTimeMillis();
            mBuilder.endRecord();
            mTimeEndRecord += System.currentTimeMillis() - start;
        }
        return true;
    }
    
   
    
    protected boolean readBeginVCard(boolean allowGarbage)
            throws IOException, VCardException {
        String line;
        do {
            while (true) {
                line = getLine();
                if (line == null) {
                    return false;
                } else if (line.trim().length() > 0) {
                    break;
                }
            }
            String[] strArray = line.split(":", 2);
            int length = strArray.length;

          
            if (length == 2 &&
                    strArray[0].trim().equalsIgnoreCase("BEGIN") &&
                    strArray[1].trim().equalsIgnoreCase("VCARD")) {
                return true;
            } else if (!allowGarbage) {
                if (mNestCount > 0) {
                    mPreviousLine = line;
                    return false;
                } else {
                    throw new VCardException(
                            "Expected String \"BEGIN:VCARD\" did not come "
                            + "(Instead, \"" + line + "\" came)");
                }
            }
        } while(allowGarbage);

        throw new VCardException("Reached where must not be reached.");
    }

    protected void readEndVCard(boolean useCache, boolean allowGarbage)
            throws IOException, VCardException {
        String line;
        do {
            if (useCache) {
                line = mPreviousLine;
            } else {
                while (true) {
                    line = getLine();
                    if (line == null) {
                        throw new VCardException("Expected END:VCARD was not found.");
                    } else if (line.trim().length() > 0) {
                        break;
                    }
                }
            }

            String[] strArray = line.split(":", 2);
            if (strArray.length == 2 &&
                    strArray[0].trim().equalsIgnoreCase("END") &&
                    strArray[1].trim().equalsIgnoreCase("VCARD")) {
                return;
            } else if (!allowGarbage) {
                throw new VCardException("END:VCARD != \"" + mPreviousLine + "\"");
            }
            useCache = false;
        } while (allowGarbage);
    }
    
  
    protected void parseItems() throws IOException, VCardException {
        boolean ended = false;
        
        if (mBuilder != null) {
            long start = System.currentTimeMillis();
            mBuilder.startProperty();
            mTimeStartProperty += System.currentTimeMillis() - start;
        }
        ended = parseItem();
        if (mBuilder != null && !ended) {
            long start = System.currentTimeMillis();
            mBuilder.endProperty();
            mTimeEndProperty += System.currentTimeMillis() - start;
        }

        while (!ended) {
            if (mBuilder != null) {
                long start = System.currentTimeMillis();
                mBuilder.startProperty();
                mTimeStartProperty += System.currentTimeMillis() - start;
            }
            ended = parseItem();
            if (mBuilder != null && !ended) {
                long start = System.currentTimeMillis();
                mBuilder.endProperty();
                mTimeEndProperty += System.currentTimeMillis() - start;
            }
        }
    }
    
   
    protected boolean parseItem() throws IOException, VCardException {
        mEncoding = sDefaultEncoding;

        String line = getNonEmptyLine();
        long start = System.currentTimeMillis();

        String[] propertyNameAndValue = separateLineAndHandleGroup(line);
        if (propertyNameAndValue == null) {
            return true;
        }
        if (propertyNameAndValue.length != 2) {
            throw new VCardException("Invalid line \"" + line + "\""); 
        }
        String propertyName = propertyNameAndValue[0].toUpperCase();
        String propertyValue = propertyNameAndValue[1];

        mTimeParseItem1 += System.currentTimeMillis() - start;

        if (propertyName.equals("ADR") ||
                propertyName.equals("ORG") ||
                propertyName.equals("N")) {
            start = System.currentTimeMillis();
            handleMultiplePropertyValue(propertyName, propertyValue);
            mTimeParseItem3 += System.currentTimeMillis() - start;
            return false;
        } else if (propertyName.equals("AGENT")) {
            handleAgent(propertyValue);
            return false;
        } else if (isValidPropertyName(propertyName)) {
            if (propertyName.equals("BEGIN")) {
                if (propertyValue.equals("VCARD")) {
                    throw new VCardNestedException("This vCard has nested vCard data in it.");
                } else {
                    throw new VCardException("Unknown BEGIN type: " + propertyValue);
                }
            } else if (propertyName.equals("VERSION") &&
                    !propertyValue.equals(getVersion())) {
                throw new VCardVersionException("Incompatible version: " + 
                        propertyValue + " != " + getVersion());
            }
            start = System.currentTimeMillis();
            handlePropertyValue(propertyName, propertyValue);
            mTimeParseItem2 += System.currentTimeMillis() - start;
            return false;
        }
        
        throw new VCardException("Unknown property name: \"" + 
                propertyName + "\"");
    }

    static private final int STATE_GROUP_OR_PROPNAME = 0;
    static private final int STATE_PARAMS = 1;
    static private final int STATE_PARAMS_IN_DQUOTE = 2;
    
    protected String[] separateLineAndHandleGroup(String line) throws VCardException {
        int length = line.length();
        int state = STATE_GROUP_OR_PROPNAME;
        int nameIndex = 0;

        String[] propertyNameAndValue = new String[2];
        
        for (int i = 0; i < length; i++) {
            char ch = line.charAt(i); 
            switch (state) {
            case STATE_GROUP_OR_PROPNAME:
                if (ch == ':') { 
                    String propertyName = line.substring(nameIndex, i);
                    if (propertyName.equalsIgnoreCase("END")) {
                        mPreviousLine = line;
                        return null;
                    }
                    if (mBuilder != null) {
                        mBuilder.propertyName(propertyName);
                    }
                    propertyNameAndValue[0] = propertyName; 
                    if (i < length - 1) {
                        propertyNameAndValue[1] = line.substring(i + 1); 
                    } else {
                        propertyNameAndValue[1] = "";
                    }
                    return propertyNameAndValue;
                } else if (ch == '.') {
                    String groupName = line.substring(nameIndex, i);
                    if (mBuilder != null) {
                        mBuilder.propertyGroup(groupName);
                    }
                    nameIndex = i + 1;
                } else if (ch == ';') {
                    String propertyName = line.substring(nameIndex, i);
                    if (propertyName.equalsIgnoreCase("END")) {
                        mPreviousLine = line;
                        return null;
                    }
                    if (mBuilder != null) {
                        mBuilder.propertyName(propertyName);
                    }
                    propertyNameAndValue[0] = propertyName;
                    nameIndex = i + 1;
                    state = STATE_PARAMS;
                }
                break;
            case STATE_PARAMS:
                if (ch == '"') {
                    state = STATE_PARAMS_IN_DQUOTE;
                } else if (ch == ';') { 
                    handleParams(line.substring(nameIndex, i));
                    nameIndex = i + 1;
                } else if (ch == ':') {
                    handleParams(line.substring(nameIndex, i));
                    if (i < length - 1) {
                        propertyNameAndValue[1] = line.substring(i + 1);
                    } else {
                        propertyNameAndValue[1] = "";
                    }
                    return propertyNameAndValue;
                }
                break;
            case STATE_PARAMS_IN_DQUOTE:
                if (ch == '"') {
                    state = STATE_PARAMS;
                }
                break;
            }
        }
        
        throw new VCardException("Invalid line: \"" + line + "\"");
    }
    
    
  
    protected void handleParams(String params) throws VCardException {
        String[] strArray = params.split("=", 2);
        if (strArray.length == 2) {
            String paramName = strArray[0].trim();
            String paramValue = strArray[1].trim();
            if (paramName.equals("TYPE")) {
                handleType(paramValue);
            } else if (paramName.equals("VALUE")) {
                handleValue(paramValue);
            } else if (paramName.equals("ENCODING")) {
                handleEncoding(paramValue);
            } else if (paramName.equals("CHARSET")) {
                handleCharset(paramValue);
            } else if (paramName.equals("LANGUAGE")) {
                handleLanguage(paramValue);
            } else if (paramName.startsWith("X-")) {
                handleAnyParam(paramName, paramValue);
            } else {
                throw new VCardException("Unknown type \"" + paramName + "\"");
            }
        } else {
            handleType(strArray[0]);
        }
    }
    
   
    protected void handleType(String ptypeval) {
        String upperTypeValue = ptypeval;
        if (!(sKnownTypeSet.contains(upperTypeValue) || upperTypeValue.startsWith("X-")) && 
                !mWarningValueMap.contains(ptypeval)) {
            mWarningValueMap.add(ptypeval);
            Log.w(LOG_TAG, "Type unsupported by vCard 2.1: " + ptypeval);
        }
        if (mBuilder != null) {
            mBuilder.propertyParamType("TYPE");
            mBuilder.propertyParamValue(upperTypeValue);
        }
    }
    
    protected void handleValue(String pvalueval) throws VCardException {
        if (sKnownValueSet.contains(pvalueval.toUpperCase()) ||
                pvalueval.startsWith("X-")) {
            if (mBuilder != null) {
                mBuilder.propertyParamType("VALUE");
                mBuilder.propertyParamValue(pvalueval);
            }
        } else {
            throw new VCardException("Unknown value \"" + pvalueval + "\"");
        }
    }
    
   
    protected void handleEncoding(String pencodingval) throws VCardException {
        if (isValidEncoding(pencodingval) ||
                pencodingval.startsWith("X-")) {
            if (mBuilder != null) {
                mBuilder.propertyParamType("ENCODING");
                mBuilder.propertyParamValue(pencodingval);
            }
            mEncoding = pencodingval;
        } else {
            throw new VCardException("Unknown encoding \"" + pencodingval + "\"");
        }
    }
    
   
    protected void handleCharset(String charsetval) {
        if (mBuilder != null) {
            mBuilder.propertyParamType("CHARSET");
            mBuilder.propertyParamValue(charsetval);
        }
    }
  
    protected void handleLanguage(String langval) throws VCardException {
        final String[] strArray = langval.split("-");
        if (strArray.length > 2) {
            throw new VCardException("Invalid Language: \"" + langval + "\"");
        }
        String tmp = strArray[0];
        int length = tmp.length();
        for (int i = 0; i < length; i++) {
            if (!isLetter(tmp.charAt(i))) {
                throw new VCardException("Invalid Language: \"" + langval + "\"");
            }
        }

        if (strArray.length > 1) {
            tmp = strArray[1];
            length = tmp.length();
            for (int i = 0; i < length; i++) {
                if (!isLetter(tmp.charAt(i))) {
                    throw new VCardException("Invalid Language: \"" + langval + "\"");
                }
            }
        }
        if (mBuilder != null) {
            mBuilder.propertyParamType("LANGUAGE");
            mBuilder.propertyParamValue(langval);
        }
    }

   
    protected void handleAnyParam(String paramName, String paramValue) {
        if (mBuilder != null) {
            mBuilder.propertyParamType(paramName);
            mBuilder.propertyParamValue(paramValue);
        }
    }
    
    protected void handlePropertyValue(
            String propertyName, String propertyValue) throws
            IOException, VCardException {
        if (mEncoding.equalsIgnoreCase("QUOTED-PRINTABLE")) {
            long start = System.currentTimeMillis();
            String result = getQuotedPrintable(propertyValue);
            if (mBuilder != null) {
                ArrayList<String> v = new ArrayList<String>();
                v.add(result);
                mBuilder.propertyValues(v);
            }
            mTimeHandlePropertyValue2 += System.currentTimeMillis() - start;
        } else if (mEncoding.equalsIgnoreCase("BASE64") ||
                mEncoding.equalsIgnoreCase("B")) {
            long start = System.currentTimeMillis();
            try {
                String result = getBase64(propertyValue);
                if (mBuilder != null) {
                    ArrayList<String> v = new ArrayList<String>();
                    v.add(result);
                    mBuilder.propertyValues(v);
                }
            } catch (OutOfMemoryError error) {
                Log.e(LOG_TAG, "OutOfMemoryError happened during parsing BASE64 data!");
                if (mBuilder != null) {
                    mBuilder.propertyValues(null);
                }
            }
            mTimeHandlePropertyValue3 += System.currentTimeMillis() - start;
        } else {
            if (!(mEncoding == null || mEncoding.equalsIgnoreCase("7BIT")
                    || mEncoding.equalsIgnoreCase("8BIT")
                    || mEncoding.toUpperCase().startsWith("X-"))) {
                Log.w(LOG_TAG, "The encoding unsupported by vCard spec: \"" + mEncoding + "\".");
            }

            long start = System.currentTimeMillis();
            if (mBuilder != null) {
                ArrayList<String> v = new ArrayList<String>();
                v.add(maybeUnescapeText(propertyValue));
                mBuilder.propertyValues(v);
            }
            mTimeHandlePropertyValue1 += System.currentTimeMillis() - start;
        }
    }
    
    protected String getQuotedPrintable(String firstString) throws IOException, VCardException {
       
        if (firstString.trim().endsWith("=")) {
            int pos = firstString.length() - 1;
            while(firstString.charAt(pos) != '=') {
            }
            StringBuilder builder = new StringBuilder();
            builder.append(firstString.substring(0, pos + 1));
            builder.append("\r\n");
            String line;
            while (true) {
                line = getLine();
                if (line == null) {
                    throw new VCardException(
                            "File ended during parsing quoted-printable String");
                }
                if (line.trim().endsWith("=")) {
                    pos = line.length() - 1;
                    while(line.charAt(pos) != '=') {
                    }
                    builder.append(line.substring(0, pos + 1));
                    builder.append("\r\n");
                } else {
                    builder.append(line);
                    break;
                }
            }
            return builder.toString(); 
        } else {
            return firstString;
        }
    }
    
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
            }
            builder.append(line);
        }
        
        return builder.toString();
    }
    
  
    protected void handleMultiplePropertyValue(
            String propertyName, String propertyValue) throws IOException, VCardException {
        if (mEncoding.equalsIgnoreCase("QUOTED-PRINTABLE")) {
            propertyValue = getQuotedPrintable(propertyValue);
        }

        if (mBuilder != null) {
            StringBuilder builder = new StringBuilder();
            ArrayList<String> list = new ArrayList<String>();
            int length = propertyValue.length();
            for (int i = 0; i < length; i++) {
                char ch = propertyValue.charAt(i);
                if (ch == '\\' && i < length - 1) {
                    char nextCh = propertyValue.charAt(i + 1);
                    String unescapedString = maybeUnescape(nextCh); 
                    if (unescapedString != null) {
                        builder.append(unescapedString);
                        i++;
                    } else {
                        builder.append(ch);
                    }
                } else if (ch == ';') {
                    list.add(builder.toString());
                    builder = new StringBuilder();
                } else {
                    builder.append(ch);
                }
            }
            list.add(builder.toString());
            mBuilder.propertyValues(list);
        }
    }
    
    
    protected void handleAgent(String propertyValue) throws VCardException {
        throw new VCardException("AGENT Property is not supported.");
       
    }
    
    
    protected String maybeUnescapeText(String text) {
        return text;
    }
    
  
    protected String maybeUnescape(char ch) {
        if (ch == '\\' || ch == ';' || ch == ':' || ch == ',') {
            return String.valueOf(ch);
        } else {
            return null;
        }
    }
    
   
    public boolean parse(InputStream is, String charset, VBuilder builder)
            throws IOException, VCardException {
        mReader = new CustomBufferedReader(new InputStreamReader(is, charset));
        
        mBuilder = builder;

        long start = System.currentTimeMillis();
        if (mBuilder != null) {
            mBuilder.start();
        }
        parseVCardFile();
        if (mBuilder != null) {
            mBuilder.end();
        }
        mTimeTotal += System.currentTimeMillis() - start;
                
        return true;
    }
    
    public boolean parse(InputStream is, VBuilder builder) throws IOException, VCardException {
        return parse(is, DEFAULT_CHARSET, builder);
    }
    
    public void cancel() {
        mCanceled = true;
    }
    
    public void parse(InputStream is, String charset, VBuilder builder, boolean canceled)
            throws IOException, VCardException {
        mCanceled = canceled;
        parse(is, charset, builder);
    }
    
    public void showDebugInfo() {
        Log.d(LOG_TAG, "total parsing time:  " + mTimeTotal + " ms");
        if (mReader instanceof CustomBufferedReader) {
            Log.d(LOG_TAG, "total readLine time: " +
                    ((CustomBufferedReader)mReader).getTotalmillisecond() + " ms");
        }
        Log.d(LOG_TAG, "mTimeStartRecord: " + mTimeStartRecord + " ms");
        Log.d(LOG_TAG, "mTimeEndRecord: " + mTimeEndRecord + " ms");
        Log.d(LOG_TAG, "mTimeParseItem1: " + mTimeParseItem1 + " ms");
        Log.d(LOG_TAG, "mTimeParseItem2: " + mTimeParseItem2 + " ms");
        Log.d(LOG_TAG, "mTimeParseItem3: " + mTimeParseItem3 + " ms");
        Log.d(LOG_TAG, "mTimeHandlePropertyValue1: " + mTimeHandlePropertyValue1 + " ms");
        Log.d(LOG_TAG, "mTimeHandlePropertyValue2: " + mTimeHandlePropertyValue2 + " ms");
        Log.d(LOG_TAG, "mTimeHandlePropertyValue3: " + mTimeHandlePropertyValue3 + " ms");
    }
    
    private boolean isLetter(char ch) {
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
            return true;
        }
        return false;
    }
}

class CustomBufferedReader extends BufferedReader {
    private long mTime;
    
    public CustomBufferedReader(Reader in) {
        super(in);
    }
    
    @Override
    public String readLine() throws IOException {
        long start = System.currentTimeMillis();
        String ret = super.readLine();
        long end = System.currentTimeMillis();
        mTime += end - start;
        return ret;
    }
    
    public long getTotalmillisecond() {
        return mTime;
    }
}
