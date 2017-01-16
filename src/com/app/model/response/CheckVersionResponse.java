package com.app.model.response;

import com.app.model.VersionInfo;

public class CheckVersionResponse extends BaseResponse {

	VersionInfo Version_Info;

	public VersionInfo getVersion_Info() {
		return Version_Info;
	}

	public void setVersion_Info(VersionInfo version_Info) {
		Version_Info = version_Info;
	}

}
