package com.qlz.aibserver.base;

import java.io.Serializable;

/**
 * @program: aib-server
 * @author: Qiaolezi
 * @create: 2025-09-20 10:25
 * @description:
 **/
public class BaseReq implements Serializable {
	private static final long serialVersionUID = 1560110948595155330L;

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
