package com.dcloud.app_dao;

import java.math.BigDecimal;

import com.dcloud.bean.DFTItem;
import com.dcloud.process.CommonUtils;

public class TestFFT {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		int size = 6;
		DFTItem[] x = new DFTItem[size];

		for (int i = 0; i < size; i++) {
			x[i] = new DFTItem();
			x[i].setShishu(i);
			x[i].setXushu(0);
		}
		System.out.println("==================x序列============================");
		CommonUtils.showDFT(x);
		System.out.println("====================FFT序列==========================");
		
		DFTItem[] _X = calFFT(x);
		CommonUtils.showDFT(_X);
		System.out.println("===================还原x序列===========================");
		
		DFTItem[] preX=calIFFT(_X);
		CommonUtils.showDFT(preX);

	}
   
	/**
	 * 
	 * @brief 快速IDFT
	 * @details （必填）
	 * @param x
	 * @return
	 * @author 江东
	 * @date 2017年8月29日下午1:20:39
	 */
   public static DFTItem[] calIFFT(DFTItem[] _X){
	   int size=_X.length;
	   
	   _X=calInitSortArray(_X);
	      
	   DFTItem[] x=new DFTItem[size];
       for (int i=0;i<size;i++){
          x[i]=new DFTItem();
       }
	      
      //逐层推进
      int floorSize=1;//第一层是1个数，每次乘以2，直到size/2个
      do{
    	  //确定WnK的指数
    	  int wnFactor=size/2/floorSize;
    	  
    	  //一组内计算下一层值
    	  for (int i = 0; i < x.length; i++) {
    		  //一次算2个，确定是否是一组中上边一半
    		  if((i/floorSize)%2==0){
    			  DFTItem wnValue=wN(-((i%floorSize)*wnFactor),size);
	    		  
    			  DFTItem x1=_X[i],x2=_X[i+floorSize];
	    		  //同时计算2个值   
    			  //x2*wnValue。(a+bi)*(c+di)=ac-bd+(ad-dc)i;
    			  DFTItem x2_wn=x2.multiple(wnValue);
    			  
//		    		  _X[i]=x1+x2*wnValue;
//		    		  _X[i+floorSize]=x1-x2*wnValue;
    			  x[i].setShishu(x1.getShishu()+x2_wn.getShishu());
    			  x[i].setXushu(x1.getXushu()+x2_wn.getXushu());
    			  
    			  x[i+floorSize].setShishu(x1.getShishu()-x2_wn.getShishu());
    			  x[i+floorSize].setXushu(x1.getXushu()-x2_wn.getXushu());
    		  }else{
    			  //出现一个重复的,直接跳到下一组
    			  i+=floorSize-1;
    			  continue;
    		  }
    	  }
    	  
    	  //上一个计算层作为下一个输入
    	  DFTItem[] temp=_X;
    	  _X=x;
    	  x=temp;
    	  
    	  floorSize=floorSize*2;
      }while(floorSize!=size);
      
      
      //x对应公式除以N
      for (int i = 0; i < _X.length; i++) {
    	  _X[i].setShishu(_X[i].getShishu()/(_X.length*1.0));
    	  _X[i].setXushu(_X[i].getXushu()/(_X.length*1.0));
      }
      return _X;
   }
   
	/**
	 * 
	 * @brief 快速DFT
	 * @details （必填）
	 * @param x
	 * @return
	 * @author 江东
	 * @date 2017年8月29日下午1:20:39
	 */
   public static DFTItem[] calFFT(DFTItem[] x){
	   int size=x.length;
	   
	   x=calInitSortArray(x);
	      
	   DFTItem[] _X=new DFTItem[size];
	   
      for (int i=0;i<size;i++){
         _X[i]=new DFTItem();
      }
      
      //逐层推进
      int floorSize=1;//第一层是1个数，每次乘以2，直到size/2个
      do{
    	  //确定WnK的指数
    	  int wnFactor=size/2/floorSize;
    	  
    	  //一组内计算下一层值
    	  for (int i = 0; i < x.length; i++) {
    		  //一次算2个，确定是否是一组中上边一半
    		  if((i/floorSize)%2==0){
    			  DFTItem wnValue=wN((i%floorSize)*wnFactor,size);
	    		  
    			  DFTItem x1=x[i],x2=x[i+floorSize];
	    		  //根据对称同时计算2个位置的值   
    			  //x2*wnValue。(a+bi)*(c+di)=ac-bd+(ad-dc)i;
    			  DFTItem x2_wn=x2.multiple(wnValue);;
    			  
//		    		  _X[i]=x1+x2*wnValue;
//		    		  _X[i+floorSize]=x1-x2*wnValue;
    			  _X[i].setShishu(x1.getShishu()+x2_wn.getShishu());
    			  _X[i].setXushu(x1.getXushu()+x2_wn.getXushu());
    			  
    			  _X[i+floorSize].setShishu(x1.getShishu()-x2_wn.getShishu());
    			  _X[i+floorSize].setXushu(x1.getXushu()-x2_wn.getXushu());
    			  
    		  }else{
    			  //出现一个重复的,直接跳到下一组
    			  i+=floorSize-1;
    			  continue;
    		  }
    	  }
    	//上一个计算层作为下一个输入
    	  DFTItem[] temp=_X;
    	  _X=x;
    	  x=temp;
    	  floorSize=floorSize*2;
      }while(floorSize!=size);
      
      return x;
   }
	/**
	 * @brief     计算WNk的值
	 * @details   应该是个复数，这个也没毛病
	 * @param       
	 * @retval    返回值
	 * @author    江东
	 * @date      2017/8/28      
	 * @note      变更信息
	 * @return    
	*/
	private static DFTItem wN(int wnFactor,int N) {
		//System.out.println("wn"+wnFactor);
		double w=-2*Math.PI/(N*1.0)*wnFactor;
		
		DFTItem dftItem=new DFTItem();
		
		dftItem.setShishu(Math.cos(w));
		dftItem.setXushu(Math.sin(w));
		
		BigDecimal shishu=new BigDecimal(dftItem.getShishu());
		dftItem.setShishu(shishu.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue());
		
		BigDecimal xushu=new BigDecimal(dftItem.getXushu());
		dftItem.setXushu(xushu.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue());
		return dftItem;
	}


	/**
	 * @brief     初始化第一层
	 * @details    时间奇偶法，验证绝对正确
	 * @param     
	 * @retval    返回值
	 * @author    江东
	 * @date      2017/8/28      
	 * @note      变更信息
	 * @return    
	*/
	public static DFTItem[] calInitSortArray(DFTItem[] x){
			DFTItem[] xbak=new DFTItem[x.length];
		      //xbak=0 2 4 6      1 3 5 7
		      //x   =0 4 2 6      1 5 3 7
		      int onePart=x.length;//第一次分组是整体
		      int i=0;
		      do{
		    	  DFTItem[] a=x;
		    	  DFTItem[] b=xbak;
		         if(i%2!=0){
		            b=x;
		            a=xbak;
		         }
		         //onePart是一个分组，a奇数位置的移到到b排列上,接下来a偶数位置数移到到b排列上
		         int aIndex=0,step=0;
		         for(int bIndex=0;bIndex<x.length;bIndex++){

		            int checkAIndex=2*(aIndex-1)+step;
		            if(checkAIndex==onePart-2){
		               //结束，切换奇
		               aIndex=0;
		               step=1;
		            }else if(checkAIndex==onePart-1){
		               //结束，切换偶
		               aIndex=0;
		               step=0;
		            }
		            b[bIndex]=a[2*aIndex+step+onePart*(bIndex/onePart)];
		            aIndex++;
		         }
		         
		         onePart=onePart/2;
		         i++;
		      }while(onePart!=2);

		      
		      if(i%2==0){
		         return x;
		      }else{
		         return xbak;
		      }
	   }

}