

package com.yunhuwifi.vcard;

import java.util.List;

public interface VBuilder {
    void start();

    void end();


    void startRecord(String type);

    void endRecord();

    void startProperty();

    void endProperty();

  
    void propertyGroup(String group);
    
   
    void propertyName(String name);

   
    void propertyParamType(String type);

    
    void propertyParamValue(String value);

    void propertyValues(List<String> values);
}
