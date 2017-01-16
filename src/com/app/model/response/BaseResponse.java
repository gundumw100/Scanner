package com.app.model.response;

public class BaseResponse {
	private String Message;
	private String ErrMessage;
	private String ErrSysMessage;
	private String ErrSysTrackMessage;

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getErrMessage() {
		return ErrMessage;
	}

	public void setErrMessage(String errMessage) {
		ErrMessage = errMessage;
	}

	public String getErrSysMessage() {
		return ErrSysMessage;
	}

	public void setErrSysMessage(String errSysMessage) {
		ErrSysMessage = errSysMessage;
	}

	public String getErrSysTrackMessage() {
		return ErrSysTrackMessage;
	}

	public void setErrSysTrackMessage(String errSysTrackMessage) {
		ErrSysTrackMessage = errSysTrackMessage;
	}
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (obj == null)
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// BaseResponse other = (BaseResponse) obj;
	// if (ErrMessage == null) {
	// if (other.ErrMessage != null)
	// return false;
	// } else if (!ErrMessage.equals(other.ErrMessage))
	// return false;
	// if (ErrSysMessage == null) {
	// if (other.ErrSysMessage != null)
	// return false;
	// } else if (!ErrSysMessage.equals(other.ErrSysMessage))
	// return false;
	// if (ErrSysTrackMessage == null) {
	// if (other.ErrSysTrackMessage != null)
	// return false;
	// } else if (!ErrSysTrackMessage.equals(other.ErrSysTrackMessage))
	// return false;
	// if (Message == null) {
	// if (other.Message != null)
	// return false;
	// } else if (!Message.equals(other.Message))
	// return false;
	//// if (Result == null) {
	//// if (other.Result != null)
	//// return false;
	//// } else if (!Result.equals(other.Result))
	//// return false;
	// return true;
	// }
}
