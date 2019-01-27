/**
 * 
 */
package com.dcloud.moshi;

/**
 * @brief 这是一个Java API注解的例子（必填）
 * @details （必填）
 * @author 彭堃
 * @date 2016年10月31日下午3:54:44
 */
public class Callee2 extends MyIncrement{
	private int i=0;
	public void increment(){
		super.increment();
		i++;
		System.out.println(i);
	}
	private class Closure implements Incrementable{

		/* (non-Javadoc)
		 * @see com.dcloud.moshi.Incrementable#increment()
		 */
		@Override
		public void increment() {
			// TODO Auto-generated method stub
			Callee2.this.increment();
		}
	}
	Incrementable getCallbackReference(){
		return new Closure();
	}
	
}
