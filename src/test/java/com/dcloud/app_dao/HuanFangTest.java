/**
 * 
 */
package com.dcloud.app_dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @brief 这是一个Java API注解的例子（必填）
 * @details （必填）
 * @author 彭堃
 * @date 2016年8月11日下午1:30:13
 */
public class HuanFangTest {
	public static void main(String[] args) {
		int[][] reshuanfang={
				{10,0,0,0,0,0},
				{0,0,0,0,0,0},
				{0,0,0,0,0,0},
				{0,0,0,0,0,0},
				{0,0,0,0,0,0},
				{0,0,0,0,0,0}};
		//可选组合
		List<Integer[]> options=new ArrayList<Integer[]>();
		
		//全部数据 
		Set<Integer> allnoOptionSet=new HashSet<Integer>();
		int rowCount=(1+reshuanfang.length*reshuanfang.length)*reshuanfang.length*reshuanfang.length/2/reshuanfang.length;
		//统计所有组合
		int limit=reshuanfang.length*reshuanfang.length;
		for (int i = 1; i <=limit-reshuanfang.length+1; i++) {
			Stack<Integer> list=new Stack<Integer>();
			
			list.push(i);
			allnoOptionSet.add(i);
			
			calConpom(allnoOptionSet,i,rowCount-i,reshuanfang.length-1,limit,list,options);
		}
		
		//不可选
		allnoOptionSet.clear();
		for (int i = 0; i < reshuanfang.length; i++) {
			for (int j = 0; j < reshuanfang[i].length; j++) {
				int point=reshuanfang[i][j];
				
				if(point!=0){
					allnoOptionSet.add(point);
				}
			}
		}
		calOption(reshuanfang,options,allnoOptionSet,rowCount,0,0,limit);
		//calxie1(huanfang,rowCount);
		quar(reshuanfang);
	}
	/**
	 * 
	 * @brief 计算当前点按顺序取数行不行
	 * @details （必填）
	 * @param reshuanfang
	 * @param optionMap
	 * @param allOptionSet
	 * @return
	 * @author 彭堃
	 * @date 2016年8月11日下午2:42:29
	 */
	private static boolean calOption(int[][] reshuanfang,List<Integer[]> optionList,Set<Integer> allnoOptionSet,int rowCount
			,int i,int j,int limit){
		boolean isSuccess=true;
		int nexti=i;
		int nextj=j;
		if(j==reshuanfang.length-1){
			//边
			nexti++;
			nextj=0;
		}else{
			nextj++;
		}
		
		if(i>=reshuanfang.length||j>=reshuanfang.length){
			return true;
		}
		if(i>=reshuanfang.length-1){
			
		}
		//全部数据
		int point=reshuanfang[i][j];
		
		if(point==0){
			//找出左上角，上，左的各自列表，然后统计出共同拥有的点，遍历即可
			Integer[] optionPoints=findOptions(reshuanfang,i,j,optionList);

			if(optionPoints!=null){
				for (Integer k : optionPoints) {
					if(!allnoOptionSet.contains(k)){
						reshuanfang[i][j]=k;
						allnoOptionSet.add(k);
						//能计算出左下角了
						if(i==reshuanfang.length-2&&j==0){

							quar(reshuanfang);
							System.out.println("\n");
							
							int colAll=0;
							for (int k2 = 0; k2 <=i; k2++) {
								colAll+=reshuanfang[k2][j];
							}
							if(rowCount-colAll>0&&rowCount-colAll<=limit&&!allnoOptionSet.contains(rowCount-colAll)){
								reshuanfang[i+1][j]=rowCount-colAll;
								allnoOptionSet.add(reshuanfang[i+1][j]);
								
								//斜就算出
								colAll=reshuanfang[i+1][j];
								for (int k2 = 0; k2 < reshuanfang.length-2; k2++) {
									colAll+=reshuanfang[k2][reshuanfang.length-k2-1];
								}
								if(rowCount-colAll>0&&rowCount-colAll<=limit&&!allnoOptionSet.contains(rowCount-colAll)){
									reshuanfang[i][j+1]=rowCount-colAll;
									allnoOptionSet.add(reshuanfang[i][j+1]);
									
									//i+1,j+1也就算出来
									colAll=0;
									for (int k2 = 0; k2 < reshuanfang.length-1; k2++) {
										colAll+=reshuanfang[k2][j+1];
									}
									if(rowCount-colAll>0&&rowCount-colAll<=limit&&!allnoOptionSet.contains(rowCount-colAll)){
										reshuanfang[i+1][j+1]=rowCount-colAll;
										allnoOptionSet.add(reshuanfang[i+1][j+1]);
									}else{
										//ij失败
										reshuanfang[i][j]=0;
										allnoOptionSet.remove(k);
										
										allnoOptionSet.remove(reshuanfang[i+1][j]);
										reshuanfang[i+1][j]=0;
										
										allnoOptionSet.remove(reshuanfang[i][j+1]);
										reshuanfang[i][j+1]=0;
										continue;
									}
								}else{
									//ij失败
									reshuanfang[i][j]=0;
									allnoOptionSet.remove(k);
									
									allnoOptionSet.remove(reshuanfang[i+1][j]);
									reshuanfang[i+1][j]=0;
									continue;
								}
							}else{
								reshuanfang[i][j]=0;
								allnoOptionSet.remove(k);
								continue;
							}
						}else{
							
						}
						//判断下一个满足不
						boolean resNextNode=calOption(reshuanfang, optionList, allnoOptionSet, rowCount,nexti,nextj,limit);
						if(resNextNode){
							isSuccess=true;
							break;
						}else{
							reshuanfang[i][j]=0;
							
							if(i==reshuanfang.length-2&&j==0){
								allnoOptionSet.remove(reshuanfang[i+1][j]);
								reshuanfang[i+1][j]=0;
								
								allnoOptionSet.remove(reshuanfang[i][j+1]);
								reshuanfang[i][j+1]=0;
								
								allnoOptionSet.remove(reshuanfang[i+1][j+1]);
								reshuanfang[i+1][j+1]=0;
							}
							
							allnoOptionSet.remove(k);
							continue;
						}
					}
				}
			}
			//不成功，退一步
			if(reshuanfang[i][j]==0){
				return false;
			}
		}else{
			isSuccess=calOption(reshuanfang, optionList, allnoOptionSet, rowCount,nexti,nextj,limit);
		}
		return isSuccess;
	}
	/**
	 * @brief 找出左上角，上，左的各自列表，然后统计出共同拥有的点，遍历即可
	 * @details （必填）
	 * @param reshuanfang
	 * @param i
	 * @param j
	 * @param optionList
	 * @return
	 * @author 彭堃
	 * @date 2016年8月16日上午11:27:09
	 */
	private static Integer[] findOptions(int[][] reshuanfang, int i, int j,
			List<Integer[]> optionList) {
		System.out.println(String.format("当前(%s,%s)", i,j));
		
		Set<Integer> result = new HashSet<Integer>();
		//上
		List<Integer> hasExist=new ArrayList<Integer>(reshuanfang.length);
		for (int k = 0; k < i; k++) {
			hasExist.add(reshuanfang[k][j]);
		}
		
		Set<Integer> canSelected=getExistFromAlloption(hasExist,optionList);
		
		System.out.println(String.format("当前竖可选%s", canSelected));
		//左
		hasExist.clear();
		for (int k = 0; k < j; k++) {
			hasExist.add(reshuanfang[i][k]);
		}
		Set<Integer> tmpSelected=getExistFromAlloption(hasExist,optionList);
		System.out.println(String.format("当前横可选%s", tmpSelected));
		//交集
		result.addAll(canSelected);
		result.retainAll(tmpSelected);
		
		System.out.println(String.format("当前竖横交集可选%s", result));
		if(result.size()<=0){
			return null;
		}
		//左上
		if(i==j){
			hasExist.clear();
			for (int k = 0; k < j; k++) {
				hasExist.add(reshuanfang[k][k]);
			}
			tmpSelected=getExistFromAlloption(hasExist,optionList);
			System.out.println(String.format("当前反斜可选%s", tmpSelected));
			//交集
			result.retainAll(tmpSelected);
			
			System.out.println(String.format("当前竖横反斜交集可选%s", result));
			if(result.size()<=0){
				return null;
			}
		}
		//右上
		if(i+j==reshuanfang.length-1){
			hasExist.clear();
			for (int k = 0; k < i; k++) {
				hasExist.add(reshuanfang[k][reshuanfang.length-k-1]);
			}
			tmpSelected=getExistFromAlloption(hasExist,optionList);
			System.out.println(String.format("当前斜可选%s", tmpSelected));
			//交集
			result.retainAll(tmpSelected);
			
			System.out.println(String.format("当前竖横反斜斜交集可选%s", result));
			if(result.size()<=0){
				return null;
			}
		}
		if(result.size()<=0){
			return null;
		}
		return result.toArray(new Integer[result.size()]);
	}
	
	static Map<String,Set<Integer>> hasMap=new HashMap<String, Set<Integer>>();
	/**
	 * 
	 * @brief 在所有组合中找到包含已存在，有些耗时
	 * @details （必填）
	 * @param hasExist
	 * @param optionList
	 * @return
	 * @author 彭堃
	 * @date 2016年8月16日下午4:02:27
	 */
	private static Set<Integer> getExistFromAlloption(List<Integer> hasExist,List<Integer[]> optionList){
		Set<Integer> res=new HashSet<Integer>();
		
		String hasExistHash=join(hasExist);
		Set<Integer> optionres=hasMap.get(hasExistHash);
		if(optionres!=null){
			return optionres;
		}
		
		List<Integer> list=new ArrayList<Integer>();
		for (Integer[] ol : optionList) {
			list.clear();
			Collections.addAll(list, ol);
			int len=list.size();
			
			if(hasExist.size()>0){
				list.removeAll(hasExist);
			}
			
			if(len-list.size()==hasExist.size()){
				res.addAll(list);
			}
		}
		hasMap.put(hasExistHash,res);
		return res;
	}
	
	private static String join(List<Integer> hasExist){
		String s="";
		if(hasExist.size()>0){
			for (Integer he : hasExist) {
				s+=he+",";
			}
		}
		return s;
	}
	/**
	 * 
	 * @brief 计算合计的所有组合
	 * @details （必填）
	 * @param allnoOptionSet 所有不可选
	 * @param cur 当前值
	 * @param allAmount 剩下的凑数的和
	 * @param lastnum 剩下的凑数的个数
	 * @param max 集合最大值
	 * @param list
	 * @author 彭堃
	 * @date 2016年8月14日下午4:43:11
	 */
	public static void calConpom(Set<Integer> allnoOptionSet,int cur,int allAmount,int lastnum,
			int max,Stack<Integer> list,List<Integer[]> options){
		//System.out.print(cur+"\t");
		//System.out.print("("+lastnum+"->"+allAmount+")");
		
		if(lastnum==1){
			//System.out.println();
			if(allAmount<cur||allAmount>max){
				return;
			}
			for (int i = cur; i <= max; i++) {
				if(allAmount==i&&!allnoOptionSet.contains(i)){
					list.push(i);
					
					options.add(list.toArray(new Integer[list.size()]));
					printShow(list);
					list.pop();
					return;
				}
			}
		}else{
			//计算下一个
			for (int i = cur+1; i <= max; i++) {
				if(i>=allAmount-i|| max*(lastnum-1)<allAmount-i) continue;
				
				//先自己加入
				list.push(i);
				allnoOptionSet.add(i);
				
				calConpom(allnoOptionSet,i,allAmount-i,lastnum-1,max,list,options);
				//找不到，移除cur
				list.pop();
				allnoOptionSet.remove(i);
			}
		}
	}
	/**
	 */
	private static void printShow(Stack<Integer> list) {
		// TODO Auto-generated method stub
		for (Integer integer : list) {
			System.out.print(integer+"--");
		}
		System.out.println();
	}
	
	private static void quar(int[][] reshuanfang){
		for (int i = 0; i < reshuanfang.length; i++) {
			for (int j = 0; j < reshuanfang[i].length; j++) {
				int point=reshuanfang[i][j];
				
				System.out.print(point+"\t");
			}
			System.out.print("\n");
		}
	}
}
