/**
 * 
 */
package com.dcloud.moshi;

/**
 * @brief 这是一个Java API注解的例子（必填）
 * @details （必填）
 * @author 彭堃
 * @date 2016年10月31日下午3:52:11
 */
public class Callee1 implements Incrementable {
	private int i=0;
	/* (non-Javadoc)
	 * @see com.dcloud.moshi.Incrementable#increment()
	 */
	@Override
	public void increment() {
		// TODO Auto-generated method stub
		i++;
		System.out.println(i);
	}

}
