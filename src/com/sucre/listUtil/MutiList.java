package com.sucre.listUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;

/**
 * 作为账号,密码,或者其它cookie,vid 存储之用的列表类
 */
public class MutiList extends ArrayList<String> {
	private ArrayList<String> myList = null;
    private int size=0;
	//private final Lock lock=new ReentrantLock();
	public MutiList() {
		super();
		this.myList = new ArrayList<>();
	}


	/**
	 * 此方法用于加载当前目录下的文件数据到列表.
	 * 
	 * @param fileName 为当前目录下的文件名,带后缀.
	 * @throws IOException 如果找不到文件抛出异常让调用者处理!
	 */
	public void loadFromFile(String fileName) throws IOException {
		try (
			// 打开文件读取数据,如出现异常自动释放.
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
	    )
		{
            
			 String str = null;
	         while ((str = in.readLine()) != null) {
	            //写入数组
	        	this.add(str);
	        	
	         }
	        //关闭流
	        in.close();
		
		
		} catch (IOException  e) {
			
			//e.printStackTrace();
			//抛出异常给调用者显示给用户
			throw new IOException(fileName +"出错了!文件名错误或正在被使用!");
		}
	}
    
	
	/**
	 * 覆盖父类方法,根据索引取元索
	 *
	 */
	public String get(int index) {
		
		if(index>-1 && index<=size) {
			return myList.get(index);
		}
		return "";
	}


	/**
	 * 取元素的个数
	 * @return 取元素的个数
	 */
    public int getSize() {
    	return size;
    }
    /**
     *   加入元素到数组
     */
    @Override
	public boolean add(String e) {
    	myList.add(e);
    	size++;
		return true;
	}

   /**
    * 覆盖remove方法,
    * @param index 为列表索引
    * @return 成功则返回索引最新长度,
    */
	@Override
	public String remove(int index) {
	    if(index < 0 ||index >= size ||size==0) {
	    	return "索引不合法";
	    }
	    myList.remove(index);
	    size--;
		return String.valueOf(size);
	}


	//覆盖toString方法,用于打印数组元素.因为父类已经有了方法,其实只要return myList.toString();就可以了,这样做只是为了练习一下.
	@Override
	public String toString() {
		//数组为空直接返加[]
		if(size==0) {
			return "[]";
		}
		StringBuilder temp=new StringBuilder(size *2);
		temp.append("{");
		for(int i=0;i<size;i++) {
			temp.append(myList.get(i));
			if(i!=size-1) {
				temp.append(",");
			}else {
				temp.append("}");
			}
		}
		
		return temp.toString();
	}
	
	

}
