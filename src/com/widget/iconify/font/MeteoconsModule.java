package com.widget.iconify.font;

import com.widget.iconify.Icon;
import com.widget.iconify.IconFontDescriptor;

public class MeteoconsModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "iconify/android-iconify-meteocons.ttf";
    }

    @Override
    public Icon[] characters() {
        return MeteoconsIcons.values();
    }
}
