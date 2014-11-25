
package com.yunhuwifi.vcard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.yunhuwifi.vcard.ContactStruct.PhoneData;


public class VCardComposer {
	final public static int VERSION_VCARD21_INT = 1;

	final public static int VERSION_VCARD30_INT = 2;

	
	private String mNewline;

	private StringBuilder mResult;

	static final private HashSet<String> emailTypes = new HashSet<String>(
			Arrays.asList("CELL", "AOL", "APPLELINK", "ATTMAIL", "CIS",
					"EWORLD", "INTERNET", "IBMMAIL", "MCIMAIL", "POWERSHARE",
					"PRODIGY", "TLX", "X400"));

	static final private HashSet<String> phoneTypes = new HashSet<String>(
			Arrays.asList("PREF", "WORK", "HOME", "VOICE", "FAX", "MSG",
					"CELL", "PAGER", "BBS", "MODEM", "CAR", "ISDN", "VIDEO"));

	static final private String TAG = "VCardComposer";

	public VCardComposer() {
	}

	private static final HashMap<Integer, String> phoneTypeMap = new HashMap<Integer, String>();

	private static final HashMap<Integer, String> emailTypeMap = new HashMap<Integer, String>();

	static {
		phoneTypeMap.put(Contacts.Phones.TYPE_HOME, "HOME");
		phoneTypeMap.put(Contacts.Phones.TYPE_MOBILE, "CELL");
		phoneTypeMap.put(Contacts.Phones.TYPE_WORK, "WORK");
		phoneTypeMap.put(Contacts.Phones.TYPE_FAX_WORK, "WORK;FAX");
		phoneTypeMap.put(Contacts.Phones.TYPE_FAX_HOME, "HOME;FAX");
		phoneTypeMap.put(Contacts.Phones.TYPE_PAGER, "PAGER");
		phoneTypeMap.put(Contacts.Phones.TYPE_OTHER, "X-OTHER");
		emailTypeMap.put(Contacts.ContactMethods.TYPE_HOME, "HOME");
		emailTypeMap.put(Contacts.ContactMethods.TYPE_WORK, "WORK");
	}

	public String createVCard(ContactStruct struct, int vcardversion)
			throws VCardException {

		mResult = new StringBuilder();
		if (vcardversion == VERSION_VCARD21_INT) {
			mNewline = "\r\n";
		} else if (vcardversion == VERSION_VCARD30_INT) {
			mNewline = "\n";
		} else {
			throw new VCardException(
					" version not match VERSION_VCARD21 or VERSION_VCARD30.");
		}
		mResult.append("BEGIN:VCARD").append(mNewline);

		if (vcardversion == VERSION_VCARD21_INT) {
			mResult.append("VERSION:2.1").append(mNewline);
		} else {
			mResult.append("VERSION:3.0").append(mNewline);
		}

		if (!isNull(struct.name)) {
			appendNameStr(struct.name);
		}

		if (!isNull(struct.company)) {
			mResult.append("ORG:").append(struct.company).append(mNewline);
		}

		if (struct.notes.size() > 0 && !isNull(struct.notes.get(0))) {
			mResult.append("NOTE:")
					.append(foldingString(struct.notes.get(0), vcardversion))
					.append(mNewline);
		}

		if (!isNull(struct.title)) {
			mResult.append("TITLE:")
					.append(foldingString(struct.title, vcardversion))
					.append(mNewline);
		}

		if (struct.photoBytes != null) {
			appendPhotoStr(struct.photoBytes, struct.photoType, vcardversion);
		}

		if (struct.phoneList != null) {
			appendPhoneStr(struct.phoneList, vcardversion);
		}

		if (struct.contactmethodList != null) {
			appendContactMethodStr(struct.contactmethodList, vcardversion);
		}

		mResult.append("END:VCARD").append(mNewline);
		return mResult.toString();
	}

	private String foldingString(String str, int version) {
		if (str.endsWith("\r\n")) {
			str = str.substring(0, str.length() - 2);
		} else if (str.endsWith("\n")) {
			str = str.substring(0, str.length() - 1);
		}

		str = str.replaceAll("\r\n", "\n");
		if (version == VERSION_VCARD21_INT) {
			return str.replaceAll("\n", "\r\n ");
		} else if (version == VERSION_VCARD30_INT) {
			return str.replaceAll("\n", "\n ");
		} else {
			return null;
		}
	}

	private void appendPhotoStr(byte[] bytes, String type, int version)
			throws VCardException {
		String value, encodingStr;
		try {
			value = foldingString(new String(Base64.encodeBase64(bytes, true)),
					version);
		} catch (Exception e) {
			throw new VCardException(e.getMessage());
		}

		if (isNull(type) || type.toUpperCase().indexOf("JPEG") >= 0) {
			type = "JPEG";
		} else if (type.toUpperCase().indexOf("GIF") >= 0) {
			type = "GIF";
		} else if (type.toUpperCase().indexOf("BMP") >= 0) {
			type = "BMP";
		} else {
			int indexOfSlash = type.indexOf("/");
			if (indexOfSlash >= 0) {
				type = type.substring(indexOfSlash + 1).toUpperCase();
			} else {
				type = type.toUpperCase();
			}
		}

		mResult.append("LOGO;TYPE=").append(type);
		if (version == VERSION_VCARD21_INT) {
			encodingStr = ";ENCODING=BASE64:";
			value = value + mNewline;
		} else if (version == VERSION_VCARD30_INT) {
			encodingStr = ";ENCODING=b:";
		} else {
			return;
		}
		mResult.append(encodingStr).append(value).append(mNewline);
	}

	private boolean isNull(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		return false;
	}

	private void appendNameStr(String name) {
		mResult.append("FN:").append(name).append(mNewline);
		mResult.append("N:").append(name).append(mNewline);
	}

	private void appendPhoneStr(List<ContactStruct.PhoneData> phoneList,
			int version) {
		HashMap<String, String> numMap = new HashMap<String, String>();
		String joinMark = version == VERSION_VCARD21_INT ? ";" : ",";

		for (ContactStruct.PhoneData phone : phoneList) {
			String type;
			if (!isNull(phone.data)) {
				type = getPhoneTypeStr(phone);
				if (version == VERSION_VCARD30_INT && type.indexOf(";") != -1) {
					type = type.replace(";", ",");
				}
				if (numMap.containsKey(phone.data)) {
					type = numMap.get(phone.data) + joinMark + type;
				}
				numMap.put(phone.data, type);
			}
		}

		for (Map.Entry<String, String> num : numMap.entrySet()) {
			if (version == VERSION_VCARD21_INT) {
				mResult.append("TEL;");
			} else { 
				mResult.append("TEL;TYPE=");
			}
			mResult.append(num.getValue()).append(":").append(num.getKey())
					.append(mNewline);
		}
	}

	private String getPhoneTypeStr(PhoneData phone) {

		int phoneType = phone.type;
		String typeStr, label;

		if (phoneTypeMap.containsKey(phoneType)) {
			typeStr = phoneTypeMap.get(phoneType);
		} else if (phoneType == Contacts.Phones.TYPE_CUSTOM) {
			label = phone.label.toUpperCase();
			if (phoneTypes.contains(label) || label.startsWith("X-")) {
				typeStr = label;
			} else {
				typeStr = "X-CUSTOM-" + label;
			}
		} else {
			typeStr = "VOICE"; 
		}
		return typeStr;
	}

	private void appendContactMethodStr(
			List<ContactStruct.ContactMethod> contactMList, int version) {

		HashMap<String, String> emailMap = new HashMap<String, String>();
		String joinMark = version == VERSION_VCARD21_INT ? ";" : ",";
		for (ContactStruct.ContactMethod contactMethod : contactMList) {
			switch (contactMethod.kind) {
			case Contacts.KIND_EMAIL:
				String mailType = "INTERNET";
				if (!isNull(contactMethod.data)) {
					int methodType = new Integer(contactMethod.type).intValue();
					if (emailTypeMap.containsKey(methodType)) {
						mailType = emailTypeMap.get(methodType);
					} else if (!isNull(contactMethod.label)
							&& emailTypes.contains(contactMethod.label
									.toUpperCase())) {
						mailType = contactMethod.label.toUpperCase();
					}
					if (emailMap.containsKey(contactMethod.data)) {
						mailType = emailMap.get(contactMethod.data) + joinMark
								+ mailType;
					}
					emailMap.put(contactMethod.data, mailType);
				}
				break;
			case Contacts.KIND_POSTAL:
				if (!isNull(contactMethod.data)) {
					mResult.append("ADR;TYPE=POSTAL:")
							.append(foldingString(contactMethod.data, version))
							.append(mNewline);
				}
				break;
			default:
				break;
			}
		}
		for (Map.Entry<String, String> email : emailMap.entrySet()) {
			if (version == VERSION_VCARD21_INT) {
				mResult.append("EMAIL;");
			} else {
				mResult.append("EMAIL;TYPE=");
			}
			mResult.append(email.getValue()).append(":").append(email.getKey())
					.append(mNewline);
		}
	}
}
