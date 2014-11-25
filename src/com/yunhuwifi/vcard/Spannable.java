
package com.yunhuwifi.vcard;


public interface Spannable
extends Spanned
{
   
    public void setSpan(Object what, int start, int end, int flags);

    public void removeSpan(Object what);

}
