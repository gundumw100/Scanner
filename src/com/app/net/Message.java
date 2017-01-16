package com.app.net;

/**
 * 发送数据，只有set方法
 * 
 * @author pythoner
 *
 */
public class Message {

	Pars Pars;
	String ActionName;

	public void setPars(Pars pars) {
		Pars = pars;
	}

	public void setActionName(String actionName) {
		ActionName = actionName;
	}

}
