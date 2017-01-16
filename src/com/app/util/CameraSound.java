package com.app.util;

import com.app.scanner.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class CameraSound {
	public static final int SHUTTER_CLICK_TONE3 = 0;
	private static boolean mPause = false;
	private static Player mInstance;
	private static Player mInstanceAlarmType;

	public interface Player {
		public void play(int action, int replay);

		public void stop();

		public void release();

		public void setPause(boolean pause);
	}

	public static Player getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SoundPoolPlayer(context);
		}
		return mInstance;
	}

	public static void release() {
		if (mInstance != null) {
			mInstance.release();
			mInstance = null;
		}
		if (mInstanceAlarmType != null) {
			mInstanceAlarmType.release();
			mInstanceAlarmType = null;
		}
	}

	private static class SoundPoolPlayer implements Player,
			SoundPool.OnLoadCompleteListener {

		private static final int NUM_SOUND_STREAMS = 1;
		private static final int[] SOUND_RES = { // Soundtrack res IDs.
		R.raw.barcode };

		// ID returned by load() should be non-zero.
		private static final int ID_NOT_LOADED = 0;

		private final int[] mSoundRes = { SHUTTER_CLICK_TONE3 };
		// Store the context for lazy loading.
		private Context mContext;
		// mSoundPool is created every time load() is called and cleared every
		// time release() is called.
		private SoundPool mSoundPool;
		// Sound ID of each sound resources. Given when the sound is loaded.
		private final int[] mSoundIDs;
		private final boolean[] mSoundIDReady;
		private int mSoundIDToPlay;
		private int mReplayToPlay = 0;

		private int mStreamID = 0;

		public SoundPoolPlayer(Context context) {
			mContext = context;
			int audioType = AudioManager.STREAM_RING;

			mSoundIDToPlay = ID_NOT_LOADED;

			mSoundPool = new SoundPool(NUM_SOUND_STREAMS, audioType, 5);
			mSoundPool.setOnLoadCompleteListener(this);

			mSoundIDs = new int[SOUND_RES.length];
			mSoundIDReady = new boolean[SOUND_RES.length];
		}

		@Override
		public synchronized void release() {
			if (mSoundPool != null) {
				mSoundPool.release();
				mSoundPool = null;
			}
			mContext = null;
		}

		@Override
		public synchronized void play(int action, int replay) {
			setPause(false);
			// Some Projects need to play shutter sound when the system is mute.
			if (action < 0 || action >= SOUND_RES.length) {
				return;
			}

			int index = mSoundRes[action];
			mStreamID = 0;
			if (mSoundIDs[index] == ID_NOT_LOADED) {
				// Not loaded yet, load first and then play when the loading is
				// complete.
				mSoundIDs[index] = mSoundPool.load(mContext, SOUND_RES[index],
						1);
				mSoundIDReady[index] = false;
				mSoundIDToPlay = mSoundIDs[index];
				mReplayToPlay = replay;
			} else if (!mSoundIDReady[index]) {
				// Loading and not ready yet.
				mSoundIDToPlay = mSoundIDs[index];
			} else if (mSoundPool != null) {
				mStreamID = mSoundPool.play(mSoundIDs[index], 1.0f, 1.0f, 0,
						replay, 1f);
				mSoundPool.setVolume(mStreamID, 1.0f, 1.0f);
			}
		}

		@Override
		public void stop() {
			if (mStreamID == 0) {
				return;
			}
			mSoundPool.stop(mStreamID);
			mStreamID = 0;
		}

		@Override
		public void onLoadComplete(SoundPool pool, int soundID, int status) {
			if (status != 0) {
				for (int i = 0; i < mSoundIDs.length; i++) {
					if (mSoundIDs[i] == soundID) {
						mSoundIDs[i] = ID_NOT_LOADED;
						break;
					}
				}
				return;
			}

			for (int i = 0; i < mSoundIDs.length; i++) {
				if (mSoundIDs[i] == soundID) {
					mSoundIDReady[i] = true;
					break;
				}
			}

			if ((soundID == mSoundIDToPlay) && (mSoundPool != null) && !mPause) {
				mSoundIDToPlay = ID_NOT_LOADED;
				mStreamID = mSoundPool.play(soundID, 1f, 1f, 0, mReplayToPlay,
						1f);
				mSoundPool.setVolume(mStreamID, 1.0f, 1.0f);
				mReplayToPlay = 0;
			}
		}

		@Override
		public void setPause(boolean pause) {
			mPause = pause;
		}
	}
}
