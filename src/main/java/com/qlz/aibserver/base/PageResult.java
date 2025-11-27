package com.qlz.aibserver.base;

import java.util.List;

/**
 * @program: aib-server
 * @author: Qiaolezi
 * @create: 2025-09-20 10:36
 * @description:
 **/
public class PageResult<T> {
	private List<T> list;
	private int total;

	public PageResult(List<T> list, Integer total) {
		this.list = list;
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
