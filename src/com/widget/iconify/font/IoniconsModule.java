package com.widget.iconify.font;

import com.widget.iconify.Icon;
import com.widget.iconify.IconFontDescriptor;

public class IoniconsModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "iconify/android-iconify-ionicons.ttf";
    }

    @Override
    public Icon[] characters() {
        return IoniconsIcons.values();
    }
}
