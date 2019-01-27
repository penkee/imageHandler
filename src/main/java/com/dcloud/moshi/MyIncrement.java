/**
 * 
 */
package com.dcloud.moshi;

/**
 * @brief 这是一个Java API注解的例子（必填）
 * @details （必填）
 * @author 彭堃
 * @date 2016年10月31日下午3:53:22
 */
public class MyIncrement {
	public void increment(){
		System.out.println("Other operation");
	}
	
	static void f(MyIncrement mi){
		mi.increment();
	}
}
