package com.widget.iconify.font;

import com.widget.iconify.Icon;
import com.widget.iconify.IconFontDescriptor;

public class FontAwesomeModule implements IconFontDescriptor {

	@Override
	public String ttfFileName() {
		return "iconify/android-iconify-fontawesome.ttf";
	}

	@Override
	public Icon[] characters() {
		return FontAwesomeIcons.values();
	}
}
