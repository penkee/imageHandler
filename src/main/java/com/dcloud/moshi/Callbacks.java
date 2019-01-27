/**
 * 
 */
package com.dcloud.moshi;

/**
 * @brief 这是一个Java API注解的例子（必填）
 * @details （必填）
 * @author 彭堃
 * @date 2016年10月31日下午4:03:47
 */
public class Callbacks {
	public static void main(String[] args) {
		Callee1 c1=new Callee1();
		Callee2 c2=new Callee2();
		
		MyIncrement.f(c2);
		Caller caller1=new Caller(c1);
		Caller caller2=new Caller(c2.getCallbackReference());
		caller1.go();
		caller1.go();
		caller2.go();
		caller2.go();
	}
}
