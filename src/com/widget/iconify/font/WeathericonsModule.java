package com.widget.iconify.font;

import com.widget.iconify.Icon;
import com.widget.iconify.IconFontDescriptor;

public class WeathericonsModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "iconify/android-iconify-weathericons.ttf";
    }

    @Override
    public Icon[] characters() {
        return WeathericonsIcons.values();
    }
}
