package com.app.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 * 
 */
public class T
{
    private static Toast toast;

    private T()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showToast(Context context, String text, int gravity)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.setGravity(gravity, 0, 0);
        }
        else
        {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(Context context, String text)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        else
        {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(Context context, int resId)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        }
        else
        {
            toast.setText(resId);
        }
        toast.show();
    }

    public static void showToast(Context context, int resId, int gravity)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
            toast.setGravity(gravity, 0, 0);
        }
        else
        {
            toast.setText(resId);
        }
        toast.show();
    }

}