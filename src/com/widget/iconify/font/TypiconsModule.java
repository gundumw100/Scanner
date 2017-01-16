package com.widget.iconify.font;

import com.widget.iconify.Icon;
import com.widget.iconify.IconFontDescriptor;

public class TypiconsModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "iconify/android-iconify-typicons.ttf";
    }

    @Override
    public Icon[] characters() {
        return TypiconsIcons.values();
    }
}
