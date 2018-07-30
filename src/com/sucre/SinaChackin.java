package com.sucre;

import com.sucre.mainUtil.MyUtil;
import com.sucre.myThread.Thread4Net;

public class SinaChackin extends Thread4Net{
     private int u;
     boolean isCircle;
     
	//构造器传参数给父类,定义索引上限及是否循环
	protected SinaChackin(int u, boolean isCircle) {
		super(u, isCircle);
		this.u=u;
		this.isCircle=isCircle;
	}

	//父类回调方法,要求必须覆盖
	public int doWork(int index) {
		SinaLogin sl=new SinaLogin(u,isCircle);
		String[] cookie=MyUtil.listCookie.get(index).split("\\|");
		sl.chackin(cookie[0],index);
		return index;
	}
	
	

}
