package com.yunhuwifi.vcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ContactStruct {
    private static final String LOG_TAG = "ContactStruct";
    
    public static final int NAME_ORDER_TYPE_ENGLISH = 0;
    public static final int NAME_ORDER_TYPE_JAPANESE = 1;

    /** MUST exist */
    public String name;
    public String phoneticName;
    /** maybe folding */
    public List<String> notes = new ArrayList<String>();
    /** maybe folding */
    public String title;
    /** binary bytes of pic. */
    public byte[] photoBytes;
    /** The type of Photo (e.g. JPEG, BMP, etc.) */
    public String photoType;
    /** Only for GET. Use addPhoneList() to PUT. */
    public List<PhoneData> phoneList;
    /** Only for GET. Use addContactmethodList() to PUT. */
    public List<ContactMethod> contactmethodList;
    /** Only for GET. Use addOrgList() to PUT. */
    public List<OrganizationData> organizationList;
    /** Only for GET. Use addExtension() to PUT */
    public Map<String, List<String>> extensionMap;

    @Deprecated
    public String company;
    
    public static class PhoneData {
        public int type;
        /** maybe folding */
        public String data;
        public String label;
        public boolean isPrimary; 
    }

    public static class ContactMethod {
        // Contacts.KIND_EMAIL, Contacts.KIND_POSTAL
        public int kind;
        // e.g. Contacts.ContactMethods.TYPE_HOME, Contacts.PhoneColumns.TYPE_HOME
        // If type == Contacts.PhoneColumns.TYPE_CUSTOM, label is used.
        public int type;
        public String data;
        // Used only when TYPE is TYPE_CUSTOM.
        public String label;
        public boolean isPrimary;
    }
    
    public static class OrganizationData {
        public int type;
        public String companyName;
        public String positionName;
        public boolean isPrimary;
    }

    public void addPhone(int type, String data, String label, boolean isPrimary){
        if (phoneList == null) {
            phoneList = new ArrayList<PhoneData>();
        }
        PhoneData phoneData = new PhoneData();
        phoneData.type = type;
        
        StringBuilder builder = new StringBuilder();
        String trimed = data.trim();
        int length = trimed.length();
        for (int i = 0; i < length; i++) {
            char ch = trimed.charAt(i);
            if (('0' <= ch && ch <= '9') || (i == 0 && ch == '+')) {
                builder.append(ch);
            }
        }
        phoneData.data = PhoneNumberUtils.formatNumber(builder.toString());
        phoneData.label = label;
        phoneData.isPrimary = isPrimary;
        phoneList.add(phoneData);
    }

    public void addContactmethod(int kind, int type, String data,
            String label, boolean isPrimary){
        if (contactmethodList == null) {
            contactmethodList = new ArrayList<ContactMethod>();
        }
        ContactMethod contactMethod = new ContactMethod();
        contactMethod.kind = kind;
        contactMethod.type = type;
        contactMethod.data = data;
        contactMethod.label = label;
        contactMethod.isPrimary = isPrimary;
        contactmethodList.add(contactMethod);
    }
    
    public void addOrganization(int type, String companyName, String positionName,
            boolean isPrimary) {
        if (organizationList == null) {
            organizationList = new ArrayList<OrganizationData>();
        }
        OrganizationData organizationData = new OrganizationData();
        organizationData.type = type;
        organizationData.companyName = companyName;
        organizationData.positionName = positionName;
        organizationData.isPrimary = isPrimary;
        organizationList.add(organizationData);
    }

    public void setPosition(String positionValue) {
        if (organizationList == null) {
            organizationList = new ArrayList<OrganizationData>();
        }
        int size = organizationList.size();
        if (size == 0) {
            addOrganization(Contacts.OrganizationColumns.TYPE_OTHER, "", null, false);
            size = 1;
        }
        OrganizationData lastData = organizationList.get(size - 1);
        lastData.positionName = positionValue;
    }
    
    public void addExtension(PropertyNode propertyNode) {
        if (propertyNode.propValue.length() == 0) {
            return;
        }
        // Now store the string into extensionMap.
        List<String> list;
        String name = propertyNode.propName;
        if (extensionMap == null) {
            extensionMap = new HashMap<String, List<String>>();
        }
        if (!extensionMap.containsKey(name)){
            list = new ArrayList<String>();
            extensionMap.put(name, list);
        } else {
            list = extensionMap.get(name);
        }        
        
        list.add(propertyNode.encode());
    }
    
    private static String getNameFromNProperty(List<String> elems, int nameOrderType) {
        // Family, Given, Middle, Prefix, Suffix. (1 - 5)
        int size = elems.size();
        if (size > 1) {
            StringBuilder builder = new StringBuilder();
            boolean builderIsEmpty = true;
            // Prefix
            if (size > 3 && elems.get(3).length() > 0) {
                builder.append(elems.get(3));
                builderIsEmpty = false;
            }
            String first, second;
            if (nameOrderType == NAME_ORDER_TYPE_JAPANESE) {
                first = elems.get(0);
                second = elems.get(1);
            } else {
                first = elems.get(1);
                second = elems.get(0);
            }
            if (first.length() > 0) {
                if (!builderIsEmpty) {
                    builder.append(' ');
                }
                builder.append(first);
                builderIsEmpty = false;
            }
            // Middle name
            if (size > 2 && elems.get(2).length() > 0) {
                if (!builderIsEmpty) {
                    builder.append(' ');
                }
                builder.append(elems.get(2));
                builderIsEmpty = false;
            }
            if (second.length() > 0) {
                if (!builderIsEmpty) {
                    builder.append(' ');
                }
                builder.append(second);
                builderIsEmpty = false;
            }
            // Suffix
            if (size > 4 && elems.get(4).length() > 0) {
                if (!builderIsEmpty) {
                    builder.append(' ');
                }
                builder.append(elems.get(4));
                builderIsEmpty = false;
            }
            return builder.toString();
        } else if (size == 1) {
            return elems.get(0);
        } else {
            return "";
        }
    }
    
    public static ContactStruct constructContactFromVNode(VNode node,
            int nameOrderType) {
        if (!node.VName.equals("VCARD")) {
            Log.e(LOG_TAG, "Non VCARD data is inserted.");
            return null;
        }

        String fullName = null;
        String nameFromNProperty = null;

        String xPhoneticFirstName = null;
        String xPhoneticMiddleName = null;
        String xPhoneticLastName = null;
        
        ContactStruct contact = new ContactStruct();

        boolean prefIsSetAddress = false;
        boolean prefIsSetPhone = false;
        boolean prefIsSetEmail = false;
        boolean prefIsSetOrganization = false;
        
        for (PropertyNode propertyNode: node.propList) {
            String name = propertyNode.propName;

            if (TextUtils.isEmpty(propertyNode.propValue)) {
                continue;
            }
            
            if (name.equals("VERSION")) {
            } else if (name.equals("FN")) {
                fullName = propertyNode.propValue;
            } else if (name.equals("NAME") && fullName == null) {
                fullName = propertyNode.propValue;
            } else if (name.equals("N")) {
                nameFromNProperty = getNameFromNProperty(propertyNode.propValue_vector,
                        nameOrderType);
            } else if (name.equals("SORT-STRING")) {
                contact.phoneticName = propertyNode.propValue;
            } else if (name.equals("SOUND")) {
                if (propertyNode.paramMap_TYPE.contains("X-IRMC-N") &&
                        contact.phoneticName == null) {
                    StringBuilder builder = new StringBuilder();
                    String value = propertyNode.propValue;
                    int length = value.length();
                    for (int i = 0; i < length; i++) {
                        char ch = value.charAt(i);
                        if (ch != ';') {
                            builder.append(ch);
                        }
                    }
                    contact.phoneticName = builder.toString();
                } else {
                    contact.addExtension(propertyNode);
                }
            } else if (name.equals("ADR")) {
                List<String> values = propertyNode.propValue_vector;
                boolean valuesAreAllEmpty = true;
                for (String value : values) {
                    if (value.length() > 0) {
                        valuesAreAllEmpty = false;
                        break;
                    }
                }
                if (valuesAreAllEmpty) {
                    continue;
                }

                int kind = Contacts.KIND_POSTAL;
                int type = -1;
                String label = "";
                boolean isPrimary = false;
                for (String typeString : propertyNode.paramMap_TYPE) {
                    if (typeString.equals("PREF") && !prefIsSetAddress) {
                        prefIsSetAddress = true;
                        isPrimary = true;
                    } else if (typeString.equalsIgnoreCase("HOME")) {
                        type = Contacts.ContactMethodsColumns.TYPE_HOME;
                        label = "";
                    } else if (typeString.equalsIgnoreCase("WORK") || 
                            typeString.equalsIgnoreCase("COMPANY")) {
                        type = Contacts.ContactMethodsColumns.TYPE_WORK;
                        label = "";
                    } else if (typeString.equalsIgnoreCase("POSTAL")) {
                        kind = Contacts.KIND_POSTAL;
                    } else if (typeString.equalsIgnoreCase("PARCEL") || 
                            typeString.equalsIgnoreCase("DOM") ||
                            typeString.equalsIgnoreCase("INTL")) {
                    } else if (typeString.toUpperCase().startsWith("X-") &&
                            type < 0) {
                        type = Contacts.ContactMethodsColumns.TYPE_CUSTOM;
                        label = typeString.substring(2);
                    } else if (type < 0) {
                        type = Contacts.ContactMethodsColumns.TYPE_CUSTOM;
                        label = typeString;
                    }
                }
                if (type < 0) {
                    type = Contacts.ContactMethodsColumns.TYPE_HOME;
                }
                                
                String address;
                List<String> list = propertyNode.propValue_vector;
                int size = list.size();
                if (size > 1) {
                    StringBuilder builder = new StringBuilder();
                    boolean builderIsEmpty = true;
                    if (Locale.getDefault().getCountry().equals(Locale.JAPAN.getCountry())) {
                        for (int i = size - 1; i >= 0; i--) {
                            String addressPart = list.get(i);
                            if (addressPart.length() > 0) {
                                if (!builderIsEmpty) {
                                    builder.append(' ');
                                }
                                builder.append(addressPart);
                                builderIsEmpty = false;
                            }
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            String addressPart = list.get(i);
                            if (addressPart.length() > 0) {
                                if (!builderIsEmpty) {
                                    builder.append(' ');
                                }
                                builder.append(addressPart);
                                builderIsEmpty = false;
                            }
                        }
                    }
                    address = builder.toString().trim();
                } else {
                    address = propertyNode.propValue; 
                }
                contact.addContactmethod(kind, type, address, label, isPrimary);
            } else if (name.equals("ORG")) {
                int type = Contacts.OrganizationColumns.TYPE_WORK;
                boolean isPrimary = false;
                
                for (String typeString : propertyNode.paramMap_TYPE) {
                    if (typeString.equals("PREF") && !prefIsSetOrganization) {
                        prefIsSetOrganization = true;
                        isPrimary = true;
                    }
                }

                List<String> list = propertyNode.propValue_vector; 
                int size = list.size();
                StringBuilder builder = new StringBuilder();
                for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
                    builder.append(iter.next());
                    if (iter.hasNext()) {
                        builder.append(' ');
                    }
                }

                contact.addOrganization(type, builder.toString(), "", isPrimary);
            } else if (name.equals("TITLE")) {
                contact.setPosition(propertyNode.propValue);
            } else if (name.equals("ROLE")) {
                contact.setPosition(propertyNode.propValue);
            } else if (name.equals("PHOTO")) {
                String valueType = propertyNode.paramMap.getAsString("VALUE");
                if (valueType != null && valueType.equals("URL")) {
                } else {
                    contact.photoBytes = propertyNode.propValue_bytes;
                    String type = propertyNode.paramMap.getAsString("TYPE");
                    if (type != null) {
                        contact.photoType = type;
                    }
                }
            } else if (name.equals("LOGO")) {
                String valueType = propertyNode.paramMap.getAsString("VALUE");
                if (valueType != null && valueType.equals("URL")) {
                } else if (contact.photoBytes == null) {
                    contact.photoBytes = propertyNode.propValue_bytes;
                    String type = propertyNode.paramMap.getAsString("TYPE");
                    if (type != null) {
                        contact.photoType = type;
                    }
                }
            } else if (name.equals("EMAIL")) {
                int type = -1;
                String label = null;
                boolean isPrimary = false;
                for (String typeString : propertyNode.paramMap_TYPE) {
                    if (typeString.equals("PREF") && !prefIsSetEmail) {
                        prefIsSetEmail = true;
                        isPrimary = true;
                    } else if (typeString.equalsIgnoreCase("HOME")) {
                        type = Contacts.ContactMethodsColumns.TYPE_HOME;
                    } else if (typeString.equalsIgnoreCase("WORK")) {
                        type = Contacts.ContactMethodsColumns.TYPE_WORK;
                    } else if (typeString.equalsIgnoreCase("CELL")) {
                        // We do not have Contacts.ContactMethodsColumns.TYPE_MOBILE yet.
                        type = Contacts.ContactMethodsColumns.TYPE_CUSTOM;
                        label = Contacts.ContactMethodsColumns.MOBILE_EMAIL_TYPE_NAME;
                    } else if (typeString.toUpperCase().startsWith("X-") &&
                            type < 0) {
                        type = Contacts.ContactMethodsColumns.TYPE_CUSTOM;
                        label = typeString.substring(2);
                    } else if (type < 0) {
                        type = Contacts.ContactMethodsColumns.TYPE_CUSTOM;
                        label = typeString;
                    }
                }
                // We use "OTHER" as default.
                if (type < 0) {
                    type = Contacts.ContactMethodsColumns.TYPE_OTHER;
                }
                contact.addContactmethod(Contacts.KIND_EMAIL,
                        type, propertyNode.propValue,label, isPrimary);
            } else if (name.equals("TEL")) {
                int type = -1;
                String label = null;
                boolean isPrimary = false;
                boolean isFax = false;
                for (String typeString : propertyNode.paramMap_TYPE) {
                    if (typeString.equals("PREF") && !prefIsSetPhone) {
                        // Only first "PREF" is considered.
                        prefIsSetPhone = true;
                        isPrimary = true;
                    } else if (typeString.equalsIgnoreCase("HOME")) {
                        type = Contacts.PhonesColumns.TYPE_HOME;
                    } else if (typeString.equalsIgnoreCase("WORK")) {
                        type = Contacts.PhonesColumns.TYPE_WORK;
                    } else if (typeString.equalsIgnoreCase("CELL")) {
                        type = Contacts.PhonesColumns.TYPE_MOBILE;
                    } else if (typeString.equalsIgnoreCase("PAGER")) {
                        type = Contacts.PhonesColumns.TYPE_PAGER;
                    } else if (typeString.equalsIgnoreCase("FAX")) {
                        isFax = true;
                    } else if (typeString.equalsIgnoreCase("VOICE") ||
                            typeString.equalsIgnoreCase("MSG")) {
                    } else if (typeString.toUpperCase().startsWith("X-") &&
                            type < 0) {
                        type = Contacts.PhonesColumns.TYPE_CUSTOM;
                        label = typeString.substring(2);
                    } else if (type < 0){
                        type = Contacts.PhonesColumns.TYPE_CUSTOM;
                        label = typeString;
                    }
                }
                if (type < 0) {
                    type = Contacts.PhonesColumns.TYPE_HOME;
                }
                if (isFax) {
                    if (type == Contacts.PhonesColumns.TYPE_HOME) {
                        type = Contacts.PhonesColumns.TYPE_FAX_HOME; 
                    } else if (type == Contacts.PhonesColumns.TYPE_WORK) {
                        type = Contacts.PhonesColumns.TYPE_FAX_WORK; 
                    }
                }

                contact.addPhone(type, propertyNode.propValue, label, isPrimary);
            } else if (name.equals("NOTE")) {
                contact.notes.add(propertyNode.propValue);
            } else if (name.equals("BDAY")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("URL")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("REV")) {                
                contact.addExtension(propertyNode);
            } else if (name.equals("UID")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("KEY")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("MAILER")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("TZ")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("GEO")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("NICKNAME")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("CLASS")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("PROFILE")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("CATEGORIES")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("SOURCE")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("PRODID")) {
                contact.addExtension(propertyNode);
            } else if (name.equals("X-PHONETIC-FIRST-NAME")) {
                xPhoneticFirstName = propertyNode.propValue;
            } else if (name.equals("X-PHONETIC-MIDDLE-NAME")) {
                xPhoneticMiddleName = propertyNode.propValue;
            } else if (name.equals("X-PHONETIC-LAST-NAME")) {
                xPhoneticLastName = propertyNode.propValue;
            } else {
                contact.addExtension(propertyNode);
            }
        }

        if (fullName != null) {
            contact.name = fullName;
        } else if(nameFromNProperty != null) {
            contact.name = nameFromNProperty;
        } else {
            contact.name = "";
        }

        if (contact.phoneticName == null &&
                (xPhoneticFirstName != null || xPhoneticMiddleName != null ||
                        xPhoneticLastName != null)) {
            String first;
            String second;
            if (nameOrderType == NAME_ORDER_TYPE_JAPANESE) {
                first = xPhoneticLastName;
                second = xPhoneticFirstName;
            } else {
                first = xPhoneticFirstName;
                second = xPhoneticLastName;
            }
            StringBuilder builder = new StringBuilder();
            if (first != null) {
                builder.append(first);
            }
            if (xPhoneticMiddleName != null) {
                builder.append(xPhoneticMiddleName);
            }
            if (second != null) {
                builder.append(second);
            }
            contact.phoneticName = builder.toString();
        }
        if (contact.phoneticName != null) {
            contact.phoneticName = contact.phoneticName.trim();
        }

        if (!prefIsSetPhone &&
                contact.phoneList != null && 
                contact.phoneList.size() > 0) {
            contact.phoneList.get(0).isPrimary = true;
        }

        if (!prefIsSetAddress && contact.contactmethodList != null) {
            for (ContactMethod contactMethod : contact.contactmethodList) {
                if (contactMethod.kind == Contacts.KIND_POSTAL) {
                    contactMethod.isPrimary = true;
                    break;
                }
            }
        }
        if (!prefIsSetEmail && contact.contactmethodList != null) {
            for (ContactMethod contactMethod : contact.contactmethodList) {
                if (contactMethod.kind == Contacts.KIND_EMAIL) {
                    contactMethod.isPrimary = true;
                    break;
                }
            }
        }
        if (!prefIsSetOrganization &&
                contact.organizationList != null &&
                contact.organizationList.size() > 0) {
            contact.organizationList.get(0).isPrimary = true;
        }
        
        return contact;
    }
    
    public String displayString() {
        if (name.length() > 0) {
            return name;
        }
        if (contactmethodList != null && contactmethodList.size() > 0) {
            for (ContactMethod contactMethod : contactmethodList) {
                if (contactMethod.kind == Contacts.KIND_EMAIL && contactMethod.isPrimary) {
                    return contactMethod.data;
                }
            }
        }
        if (phoneList != null && phoneList.size() > 0) {
            for (PhoneData phoneData : phoneList) {
                if (phoneData.isPrimary) {
                    return phoneData.data;
                }
            }
        }
        return "";
    }
    
    public boolean isIgnorable() {
        return TextUtils.isEmpty(name) &&
                TextUtils.isEmpty(phoneticName) &&
                (phoneList == null || phoneList.size() == 0) &&
                (contactmethodList == null || contactmethodList.size() == 0);
    }
}
