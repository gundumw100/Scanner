package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 手机硬件参数
 * 
 * @author pythoner
 *
 */
public class Config implements Parcelable
{

    // 手机参数
    public String platform = "2";// android

    public String OS = "android";// android系统

    public String androidID;// androidID

    public String model;// 手机型号

    public int sdk;// SDK版本号

    public String release;// Firmware/OS 版本号

    public int densityDpi;// 屏幕密度

    public int screenWidth;// 屏幕宽

    public int screenHeight;// 屏幕高
    
    public int statusBarHeight;// 状态栏高度
    
    public String totalMemory;// 手机总内存

    public String availMemory;// 可用内存

    public String phoneNumber;// 本机号码

    public String simOperatorName;// 服务提供商

    public String deviceId;// 设备唯一号

    // App信息
    public int versionCode;// 软件版本代号为整型数值

    public String versionName;// 软件版本号

    public int getStatusBarHeight() {
		return statusBarHeight;
	}

	public void setStatusBarHeight(int statusBarHeight) {
		this.statusBarHeight = statusBarHeight;
	}

	public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getSimOperatorName()
    {
        return simOperatorName;
    }

    public void setSimOperatorName(String simOperatorName)
    {
        this.simOperatorName = simOperatorName;
    }

    public String getOS()
    {
        return OS;
    }

    public void setOS(String OS)
    {
        this.OS = OS;
    }

    public String getAndroidID()
    {
        return androidID;
    }

    public void setAndroidID(String androidID)
    {
        this.androidID = androidID;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public int getSdk()
    {
        return sdk;
    }

    public void setSdk(int sdk)
    {
        this.sdk = sdk;
    }

    public String getRelease()
    {
        return release;
    }

    public void setRelease(String release)
    {
        this.release = release;
    }

    public int getDensityDpi()
    {
        return densityDpi;
    }

    public void setDensityDpi(int densityDpi)
    {
        this.densityDpi = densityDpi;
    }

    public int getScreenWidth()
    {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth)
    {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight()
    {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight)
    {
        this.screenHeight = screenHeight;
    }

    public String getTotalMemory()
    {
        return totalMemory;
    }

    public void setTotalMemory(String totalMemory)
    {
        this.totalMemory = totalMemory;
    }

    public String getAvailMemory()
    {
        return availMemory;
    }

    public void setAvailMemory(String availMemory)
    {
        this.availMemory = availMemory;
    }

    public int getVersionCode()
    {
        return versionCode;
    }

    public void setVersionCode(int versionCode)
    {
        this.versionCode = versionCode;
    }

    public String getVersionName()
    {
        return versionName;
    }

    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("androidID：").append(androidID);
        sb.append("；deviceId：").append(deviceId);
        sb.append("；版本代号：").append(versionCode);
        sb.append("；软件版本号：").append(versionName);
        sb.append("；手机型号：").append(model);
        sb.append("；SDK版本号：").append(sdk);
        sb.append("；Firmware/OS版本号：").append(release);
        sb.append("；屏幕密度：").append(densityDpi);
        sb.append("；屏幕宽高：").append(screenWidth).append("*").append(screenHeight);
        sb.append("；状态栏高度：").append(statusBarHeight);
        sb.append("；手机总内存：").append(totalMemory);
        sb.append("；可用内存：").append(availMemory);
        sb.append("；本机号码：").append(phoneNumber);
        sb.append("；服务提供商：").append(simOperatorName);
        return sb.toString();
    }

    public static Parcelable.Creator<Config> getCreator()
    {
        return CREATOR;
    }

    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub

        dest.writeString(platform);
    }

    public static final Parcelable.Creator<Config> CREATOR = new Creator<Config>()
    {
        public Config createFromParcel(Parcel source)
        {
            Config instance = new Config();
            instance.platform = source.readString();
            return instance;
        }

        public Config[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new Config[size];
        }
    };

}
