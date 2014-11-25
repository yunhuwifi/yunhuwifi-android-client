
package com.yunhuwifi.vcard;


import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class PropertyNode {

    public String propName;

    public String propValue;

    public List<String> propValue_vector;

    public byte[] propValue_bytes;


    public ContentValues paramMap;

    public Set<String> paramMap_TYPE;

    public Set<String> propGroupSet;
    
    public PropertyNode() {
        propName = "";
        propValue = "";
        propValue_vector = new ArrayList<String>();
        paramMap = new ContentValues();
        paramMap_TYPE = new HashSet<String>();
        propGroupSet = new HashSet<String>();
    }
    
    public PropertyNode(
            String propName, String propValue, List<String> propValue_vector,
            byte[] propValue_bytes, ContentValues paramMap, Set<String> paramMap_TYPE,
            Set<String> propGroupSet) {
        if (propName != null) {
            this.propName = propName;
        } else {
            this.propName = "";
        }
        if (propValue != null) {
            this.propValue = propValue;
        } else {
            this.propValue = "";
        }
        if (propValue_vector != null) {
            this.propValue_vector = propValue_vector;
        } else {
            this.propValue_vector = new ArrayList<String>();
        }
        this.propValue_bytes = propValue_bytes;
        if (paramMap != null) {
            this.paramMap = paramMap;
        } else {
            this.paramMap = new ContentValues();
        }
        if (paramMap_TYPE != null) {
            this.paramMap_TYPE = paramMap_TYPE;
        } else {
            this.paramMap_TYPE = new HashSet<String>();
        }
        if (propGroupSet != null) {
            this.propGroupSet = propGroupSet;
        } else {
            this.propGroupSet = new HashSet<String>();
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PropertyNode)) {
            return false;
        }
        
        PropertyNode node = (PropertyNode)obj;
        
        if (propName == null || !propName.equals(node.propName)) {
            return false;
        } else if (!paramMap.equals(node.paramMap)) {
            return false;
        } else if (!paramMap_TYPE.equals(node.paramMap_TYPE)) {
            return false;
        } else if (!propGroupSet.equals(node.propGroupSet)) {
            return false;
        }
        
        if (propValue_bytes != null && Arrays.equals(propValue_bytes, node.propValue_bytes)) {
            return true;
        } else {
            if (!propValue.equals(node.propValue)) {
                return false;
            }

            return (propValue_vector.equals(node.propValue_vector) ||
                    propValue_vector.size() == 1 ||
                    node.propValue_vector.size() == 1);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("propName: ");
        builder.append(propName);
        builder.append(", paramMap: ");
        builder.append(paramMap.toString());
        builder.append(", propmMap_TYPE: ");
        builder.append(paramMap_TYPE.toString());
        builder.append(", propGroupSet: ");
        builder.append(propGroupSet.toString());
        if (propValue_vector != null && propValue_vector.size() > 1) {
            builder.append(", propValue_vector size: ");
            builder.append(propValue_vector.size());
        }
        if (propValue_bytes != null) {
            builder.append(", propValue_bytes size: ");
            builder.append(propValue_bytes.length);
        }
        builder.append(", propValue: ");
        builder.append(propValue);
        return builder.toString();
    }
    
    public String encode() {
        StringBuilder builder = new StringBuilder();
        if (propName.length() > 0) {
            builder.append("propName:[");
            builder.append(propName);
            builder.append("],");
        }
        int size = propGroupSet.size();
        if (size > 0) {
            Set<String> set = propGroupSet;
            builder.append("propGroup:[");
            int i = 0;
            for (String group : set) {
                builder.append(group);
                if (i < size - 1) {
                    builder.append(",");
                }
                i++;
            }
            builder.append("],");
        }

        if (paramMap.size() > 0 || paramMap_TYPE.size() > 0) {
            ContentValues values = paramMap;
            builder.append("paramMap:[");
            size = paramMap.size(); 
            int i = 0;
            for (Entry<String, Object> entry : values.valueSet()) {
                builder.append(entry.getKey());

                builder.append("=");
                builder.append(entry.getValue().toString()
                        .replaceAll("\\\\", "\\\\\\\\")
                        .replaceAll(",", "\\\\,"));
                if (i < size -1) {
                    builder.append(",");
                }
                i++;
            }

            Set<String> set = paramMap_TYPE;
            size = paramMap_TYPE.size();
            if (i > 0 && size > 0) {
                builder.append(",");
            }
            i = 0;
            for (String type : set) {
                builder.append("TYPE=");
                builder.append(type
                        .replaceAll("\\\\", "\\\\\\\\")
                        .replaceAll(",", "\\\\,"));
                if (i < size - 1) {
                    builder.append(",");
                }
                i++;
            }
            builder.append("],");
        }

        size = propValue_vector.size();
        if (size > 0) {
            builder.append("propValue:[");
            List<String> list = propValue_vector;
            for (int i = 0; i < size; i++) {
                builder.append(list.get(i)
                        .replaceAll("\\\\", "\\\\\\\\")
                        .replaceAll(",", "\\\\,"));
                if (i < size -1) {
                    builder.append(",");
                }
            }
            builder.append("],");
        }

        return builder.toString();
    }

    public static PropertyNode decode(String encodedString) {
        PropertyNode propertyNode = new PropertyNode();
        String trimed = encodedString.trim();
        if (trimed.length() == 0) {
            return propertyNode;
        }
        String[] elems = trimed.split("],");
        
        for (String elem : elems) {
            int index = elem.indexOf('[');
            String name = elem.substring(0, index - 1);
            Pattern pattern = Pattern.compile("(?<!\\\\),");
            String[] values = pattern.split(elem.substring(index + 1), -1);
            if (name.equals("propName")) {
                propertyNode.propName = values[0];
            } else if (name.equals("propGroupSet")) {
                for (String value : values) {
                    propertyNode.propGroupSet.add(value);
                }
            } else if (name.equals("paramMap")) {
                ContentValues paramMap = propertyNode.paramMap;
                Set<String> paramMap_TYPE = propertyNode.paramMap_TYPE;
                for (String value : values) {
                    String[] tmp = value.split("=", 2);
                    String mapKey = tmp[0];
                    String mapValue =
                        tmp[1].replaceAll("\\\\,", ",").replaceAll("\\\\\\\\", "\\\\");
                    if (mapKey.equalsIgnoreCase("TYPE")) {
                        paramMap_TYPE.add(mapValue);
                    } else {
                        paramMap.put(mapKey, mapValue);
                    }
                }
            } else if (name.equals("propValue")) {
                StringBuilder builder = new StringBuilder();
                List<String> list = propertyNode.propValue_vector;
                int length = values.length;
                for (int i = 0; i < length; i++) {
                    String normValue = values[i]
                                              .replaceAll("\\\\,", ",")
                                              .replaceAll("\\\\\\\\", "\\\\");
                    list.add(normValue);
                    builder.append(normValue);
                    if (i < length - 1) {
                        builder.append(";");
                    }
                }
                propertyNode.propValue = builder.toString();
            }
        }
        
        String encoding = propertyNode.paramMap.getAsString("ENCODING");
        if (encoding != null &&
                (encoding.equalsIgnoreCase("BASE64") ||
                        encoding.equalsIgnoreCase("B"))) {
            propertyNode.propValue_bytes =
                Base64.decodeBase64(propertyNode.propValue_vector.get(0).getBytes());
        }
        
        return propertyNode;
    }
}
