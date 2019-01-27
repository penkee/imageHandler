/**
 * 
 */
package com.dcloud.moshi;

/**
 * @brief 这是一个Java API注解的例子（必填）
 * @details （必填）
 * @author 彭堃
 * @date 2016年10月31日下午3:59:40
 */
public class Caller {
	private Incrementable callbackReference;
	Caller(Incrementable cbh){callbackReference=cbh;}
	void go(){callbackReference.increment();}
}
