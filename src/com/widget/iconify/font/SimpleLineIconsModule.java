package com.widget.iconify.font;

import com.widget.iconify.Icon;
import com.widget.iconify.IconFontDescriptor;

public class SimpleLineIconsModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "iconify/android-iconify-simplelineicons.ttf";
    }

    @Override
    public Icon[] characters() {
        return SimpleLineIconsIcons.values();
    }
}
