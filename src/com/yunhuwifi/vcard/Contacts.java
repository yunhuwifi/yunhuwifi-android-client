package com.yunhuwifi.vcard;

public class Contacts {
    private static final String TAG = "Contacts";
    
    public static final String AUTHORITY = "contacts";


    public static final int KIND_EMAIL = 1;
    public static final int KIND_POSTAL = 2;
    public static final int KIND_IM = 3;
    public static final int KIND_ORGANIZATION = 4;
    public static final int KIND_PHONE = 5;

    private Contacts() {}

    public interface PeopleColumns {
        public static final String NAME = "name";

        public static final String PHONETIC_NAME = "phonetic_name";
        
        public static final String DISPLAY_NAME = "display_name";

        public static final String SORT_STRING = "sort_string";
        
        public static final String NOTES = "notes";

        public static final String TIMES_CONTACTED = "times_contacted";

        public static final String LAST_TIME_CONTACTED = "last_time_contacted";

        public static final String CUSTOM_RINGTONE = "custom_ringtone";

        public static final String SEND_TO_VOICEMAIL = "send_to_voicemail";

        public static final String STARRED = "starred";

        public static final String PHOTO_VERSION = "photo_version";       
    }
        public static final class Phones implements BaseColumns, PhonesColumns,
                PeopleColumns {
            private Phones() {}
            public static final String CONTENT_DIRECTORY = "phones";

            public static final String DEFAULT_SORT_ORDER = "number ASC";
        }

        public static final class ContactMethods
                implements BaseColumns, ContactMethodsColumns, PeopleColumns {
            private ContactMethods() {}

            public static final String CONTENT_DIRECTORY = "contact_methods";

            public static final String DEFAULT_SORT_ORDER = "data ASC";
        }

    public interface PhonesColumns {
        public static final String TYPE = "type";

        public static final int TYPE_CUSTOM = 0;
        public static final int TYPE_HOME = 1;
        public static final int TYPE_MOBILE = 2;
        public static final int TYPE_WORK = 3;
        public static final int TYPE_FAX_WORK = 4;
        public static final int TYPE_FAX_HOME = 5;
        public static final int TYPE_PAGER = 6;
        public static final int TYPE_OTHER = 7;

        public static final String LABEL = "label";

        public static final String NUMBER = "number";

        public static final String NUMBER_KEY = "number_key";

        public static final String ISPRIMARY = "isprimary";
    }
    public interface ContactMethodsColumns {
        public static final String KIND = "kind";

        public static final String TYPE = "type";
        public static final int TYPE_CUSTOM = 0;
        public static final int TYPE_HOME = 1;
        public static final int TYPE_WORK = 2;
        public static final int TYPE_OTHER = 3;

        public static final int MOBILE_EMAIL_TYPE_INDEX = 2;

        public static final String MOBILE_EMAIL_TYPE_NAME = "_AUTO_CELL";

        public static final String LABEL = "label";

        public static final String DATA = "data";

        public static final String AUX_DATA = "aux_data";

        public static final String ISPRIMARY = "isprimary";
    }
    public interface OrganizationColumns {
        public static final String TYPE = "type";

        public static final int TYPE_CUSTOM = 0;
        public static final int TYPE_WORK = 1;
        public static final int TYPE_OTHER = 2;

        public static final String LABEL = "label";

        public static final String COMPANY = "company";

        public static final String TITLE = "title";

        public static final String PERSON_ID = "person";

        public static final String ISPRIMARY = "isprimary";
    }
}
