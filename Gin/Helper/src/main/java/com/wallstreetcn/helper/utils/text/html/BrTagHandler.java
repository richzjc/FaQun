package com.wallstreetcn.helper.utils.text.html;

import android.text.Editable;
import android.text.Html;
import com.wallstreetcn.helper.utils.TLog;

import org.xml.sax.XMLReader;

public class BrTagHandler implements Html.TagHandler {
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
//        TLog.i("BrTagHandler", tag);
        if (tag.equalsIgnoreCase("br")) {
            TLog.i("BrTagHandler", output.toString());
        }
    }
}
