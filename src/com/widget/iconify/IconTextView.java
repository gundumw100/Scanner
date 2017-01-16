package com.widget.iconify;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * uage：
 * <com.widget.iconify.IconTextView
    android:text="I {fa-heart-o} to {fa-code} on {fa-android}"
    android:shadowColor="#22000000"
    android:shadowDx="3"
    android:shadowDy="3"
    android:shadowRadius="1"
    android:textSize="40sp"
    android:textColor="#FF..."
    ... />
 * 
 * 
 * Shall you need to override the text size of a particular icon, the following syntax is supported {fa-code 12px}, {fa-code 12dp}, {fa-code 12sp}, {fa-code @dimen/my_text_size}, and also {fa-code 120%}.
	In the same way you can override the icon color using {fa-code #RRGGBB}, {fa-code #AARRGGBB}, or {fa-code @color/my_color}.
	You can even easily spin an icon like so {fa-cog spin}.
 * 
 * If you need an icon in an ImageView or in your ActionBar menu item, then you should use IconDrawable. Again, icons are infinitely scalable and will never get fuzzy!
 * // Set an icon in the ActionBar
	menu.findItem(R.id.share).setIcon(
	   new IconDrawable(this, FontAwesomeIcons.fa_share)
	   .colorRes(R.color.ab_icon)
	   .actionBarSize());
   
 * @author pythoner
 * @see https://github.com/JoanZapata/android-iconify
 */
public class IconTextView extends TextView {

    public IconTextView(Context context) {
        super(context);
        init();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setTransformationMethod(null);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(Iconify.compute(getContext(), text, this), type);
    }

}
