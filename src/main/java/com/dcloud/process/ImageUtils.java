/**
 * 
 */
package com.dcloud.process;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 彭堃
 * @date 2016年9月2日下午3:23:16
 */
public class ImageUtils {
	  /**
     * 
     * @brief 邻域
     * @details 邻域
     * @param i
     * @param j
     * @param maxWidth
     * @param maxHeight
     * @param linkLength 宽度
     * @return
     * @author 彭堃
     * @throws Exception 
     * @date 2016年9月2日下午1:55:46
     */
    public static List<Point> findLink(final int i,final int j,final int maxWidth,final int maxHeight,final int linkLength) throws Exception{
    	if(linkLength<=1||linkLength%2==0){
    		throw new Exception("linkLength 必须大于1，且是奇数");
    	}
    	List<Point> res=new ArrayList<Point>(linkLength*linkLength);
    	
    	for (int tmpi = i-linkLength/2; tmpi <= linkLength/2+i; tmpi++) {
			if(tmpi>=0&&tmpi<maxWidth){
				
				for (int tmpj = j-linkLength/2; tmpj <= linkLength/2+j; tmpj++) {
					if(tmpj>=0&&tmpj<maxHeight){
						if(tmpi!=i||tmpj!=j){
							res.add(new Point(tmpi,tmpj));
						}
					}
				}
			}
		}
    	return res;
    }

	/**
	 * @brief 矩阵计算
	 * @details （必填）
	 * @param i
	 * @param j
	 * @param matrix
	 * @return
	 * @author 彭堃
	 * @throws Exception 
	 * @date 2016年9月2日下午3:28:45
	 */
	public static Point matrixCal(int i, int j, int[][] matrix) throws Exception {
		if(matrix==null||matrix.length!=3||matrix[0].length!=3){
    		throw new Exception("matrix必须是3*3的矩阵");
    	}
		int x=i*matrix[0][0]+j*matrix[1][0]+matrix[2][0];
		
		int y=i*matrix[0][1]+j*matrix[1][1]+matrix[2][1];
		
		return new Point(x,y);
	}
	/**
	 * @brief 矩阵计算
	 * @details （必填）
	 * @param i
	 * @param j
	 * @param matrix
	 * @return
	 * @author 彭堃
	 * @throws Exception 
	 * @date 2016年9月2日下午3:28:45
	 */
	public static Float matrixCal(int i, int j, float[][] matrix) throws Exception {
		if(matrix==null||matrix.length!=3||matrix[0].length!=3){
    		throw new Exception("matrix必须是3*3的矩阵");
    	}
		float x=i*matrix[0][0]+j*matrix[1][0]+matrix[2][0];
		
		float y=i*matrix[0][1]+j*matrix[1][1]+matrix[2][1];
		
		return new Point2D.Float(x,y);
	}
	
	/**
	 * @brief 计算灰度
	 * @details （必填）
	 * @param rgb
	 * @return
	 */
	public static int getHuidu(int rgb){
    	int red=(rgb&(255<<16))>>16;
    	int green=(rgb&(255<<8))>>8;
    	int blue=rgb&(255);
    	
    	int huidu=(red+green+blue)/3;
    	return huidu;
	}
	public static int getValidHuidu(int huidu){
    	if(huidu>255){
    		return 255;
    	}else if(huidu<0){
    		return 0;
    	}else{
    		return huidu;
    	}
	}
	/**
	 *  //min=1,max=4。分成3份
        //1-0,4-255
        //x=2->(2-1)/3*255=>(x-min)/(max-min)*255
	 * @param huidu
	 * @param min
	 * @param max
	 * @return 
	 * added by Buke at 2017年4月9日
	 */
	public static int getValidHuidu(int huidu,int min,int max){
    	int res=CommonUtils.doubleToInt((huidu-min)/((max-min)*1.0)*255);
    	
    	return res;
	}
}
