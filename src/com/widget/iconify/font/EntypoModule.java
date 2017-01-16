package com.widget.iconify.font;

import com.widget.iconify.Icon;
import com.widget.iconify.IconFontDescriptor;

public class EntypoModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "iconify/android-iconify-entypo.ttf";
    }

    @Override
    public Icon[] characters() {
        return EntypoIcons.values();
    }
}
