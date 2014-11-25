

package com.yunhuwifi.vcard;


import java.io.ByteArrayInputStream;
import java.io.IOException;

public class VCardParser {

    VCardParser_V21 mParser = null;

    public final static String VERSION_VCARD21 = "vcard2.1";

    public final static String VERSION_VCARD30 = "vcard3.0";

    final public static int VERSION_VCARD21_INT = 1;

    final public static int VERSION_VCARD30_INT = 2;

    String mVersion = null;

    static final private String TAG = "VCardParser";

    public VCardParser() {
    }

    private void judgeVersion(String vcardStr) {
        if (mVersion == null) {
            int verIdx = vcardStr.indexOf("\nVERSION:");
            if (verIdx == -1) 
                mVersion = VERSION_VCARD21;
            else {
                String verStr = vcardStr.substring(verIdx, vcardStr.indexOf(
                        "\n", verIdx + 1));
                if (verStr.indexOf("2.1") > 0)
                    mVersion = VERSION_VCARD21;
                else if (verStr.indexOf("3.0") > 0)
                    mVersion = VERSION_VCARD30;
                else
                    mVersion = VERSION_VCARD21;
            }
        }
        if (mVersion.equals(VERSION_VCARD21))
            mParser = new VCardParser_V21();
        if (mVersion.equals(VERSION_VCARD30))
            mParser = new VCardParser_V30();
    }

    
    private String verifyVCard(String vcardStr) {
        this.judgeVersion(vcardStr);
        vcardStr = vcardStr.replaceAll("\r\n", "\n");
        String[] strlist = vcardStr.split("\n");
        StringBuilder v21str = new StringBuilder("");
        for (int i = 0; i < strlist.length; i++) {
            if (strlist[i].indexOf(":") < 0) {
                if (strlist[i].length() == 0 && strlist[i + 1].indexOf(":") > 0)
                    v21str.append(strlist[i]).append("\r\n");
                else
                    v21str.append(" ").append(strlist[i]).append("\r\n");
            } else
                v21str.append(strlist[i]).append("\r\n");
        }
        return v21str.toString();
    }

    private void setVersion(String version) {
        this.mVersion = version;
    }

    public boolean parse(String vcardStr, String encoding, VDataBuilder builder)
            throws VCardException, IOException {

        vcardStr = this.verifyVCard(vcardStr);

        boolean isSuccess = mParser.parse(new ByteArrayInputStream(vcardStr
                .getBytes(encoding)), encoding, builder);
        if (!isSuccess) {
            if (mVersion.equals(VERSION_VCARD21)) {

                this.setVersion(VERSION_VCARD30);

                return this.parse(vcardStr, builder);
            }
            throw new VCardException("parse failed.(even use 3.0 parser)");
        }
        return true;
    }

    public boolean parse(String vcardStr, VDataBuilder builder)
            throws VCardException, IOException {
       return parse(vcardStr, "US-ASCII", builder);
    }
}
