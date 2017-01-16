/**
 * Project Name:YTOInfield
 * File Name:SoundManager.java
 * Package Name:cn.net.yto.infield.biz
 * Date:2013-3-7 am 10:10:13
 * Copyright (c) 2013, zhiliantiandi All Rights Reserved.
 *
 */

package com.app.util;


import com.app.scanner.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


/**
 * ClassName:SoundManager <br/>
 * Date: 2013-3-7 am 10:10:13 <br/>
 * 
 * @author Liliang
 * @version
 * @since JDK 1.6
 * @see
 */
public final class SoundUtils {

    public static final int SOUND_TYPE_SUCCESS = 0;
    public static final int SOUND_TYPE_WARNING = 1;
    public static final int SOUND_TYPE_QUERY = 2;

    private static SoundUtils sManager;
    private float mStreamVolume = 0.1f;
    private Context mContext;
    
    private int mWarningId = R.raw.ios7;//warning
    private int mSuccessId = R.raw.barcode;
    private int mQueryId = R.raw.query;

    private SoundUtils() {
        
    }

    public static SoundUtils getInstance() {
        if (sManager == null) {
            sManager = new SoundUtils();
        }

        return sManager;
    }

    public void init(Context context) {
        if (mContext == null || mSoundPool == null) {
            mContext = context;
            loadSoundResources(context);
        }
    }
    
    public void init(Context context, int warningId, int successId, int queryId) {
        if (mContext == null || mSoundPool == null) {
            mContext = context;
            mWarningId = warningId;
            mSuccessId = successId;
            mQueryId = queryId;
            loadSoundResources(context);
        }
    }

    /**
     * 
     * playSound:Play sound for scan. <br/>
     * 
     * @author Liliang
     * @param soundType
     *            One of {@link #SOUND_TYPE_SUCCESS},
     *            {@link #SOUND_TYPE_WARNING}, or {@link #SOUND_TYPE_QUERY}
     * @since JDK 1.6
     */
    public void playSound(int soundType) {
        int soundResId = mSoundSuccessId;
        switch (soundType) {
        case SOUND_TYPE_SUCCESS:
            soundResId = mSoundSuccessId;
            break;

        case SOUND_TYPE_WARNING:
            soundResId = mSoundWarningId;
            break;

        case SOUND_TYPE_QUERY:
            soundResId = mSoundQueryId;
            break;

        default:
            break;
        }

        mSoundPool.play(soundResId, getVolume(), getVolume(), 1, 0, 1f);
    }
    
    public void playOther(int resourceId) {
        int id = mSoundPool.load(mContext, resourceId, 1);
        mSoundPool.play(id, getVolume(), getVolume(), 1, 0, 1f);
    }

    public void warn() {
        mSoundPool
                .play(mSoundWarningId, getVolume(), getVolume(), 1, 0, 1f);
    }
    
    public void warn(float leftVolume, float rightVolume) {
        mSoundPool
                .play(mSoundWarningId, leftVolume, rightVolume, 1, 0, 1f);
    }

    public void success() {
        mSoundPool
                .play(mSoundSuccessId, getVolume(), getVolume(), 1, 0, 1f);
    }
    
    public void success(float leftVolume, float rightVolume) {
        mSoundPool
                .play(mSoundSuccessId, leftVolume, rightVolume, 1, 0, 1f);
    }

    public void query() {
        mSoundPool.play(mSoundQueryId, getVolume(), getVolume(), 1, 0, 1f);
    }
    
    public void query(float leftVolume, float rightVolume) {
        mSoundPool.play(mSoundQueryId, leftVolume, rightVolume, 1, 0, 1f);
    }
    
    private float getVolume() {
        return 0.8f;
    }

    private void loadSoundResources(Context context) {
        release();
        if (mSoundPool == null) {
            mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
            mSoundWarningId = mSoundPool.load(context, mWarningId, 1);
            mSoundSuccessId = mSoundPool.load(context, mSuccessId, 1);
            mSoundQueryId = mSoundPool.load(context, mQueryId, 1);
        }
    }

    public void release() {
        if (mSoundPool != null) {
            mSoundPool.release();
        }
        mSoundPool = null;
    }

    private SoundPool mSoundPool;
    private int mSoundWarningId;
    private int mSoundSuccessId;
    private int mSoundQueryId;

}
