//package com.dcloud.process;
//
//import java.awt.Color;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import javax.imageio.ImageIO;
//import javax.imageio.ImageWriter;
//import javax.imageio.stream.ImageOutputStream;
//import javax.net.ssl.HttpsURLConnection;
//
//public class KongJianLvboImage{
//
//    public KongJianLvboImage() {
//		super();
//	}
//
//	/**空间滤波
//	 * @details （必填）
//	 * @param string
//	 * @param string2
//	 * @author 彭堃
//	 * @throws Exception 
//	 * @date 2016年10月15日下午4:22:51
//	 */
//	public void lvboChange(String file, String targetfile) throws Exception {
//		// TODO Auto-generated method stub
//		URL http;
//        if(file.trim().startsWith("https")){
//            http = new URL(file);
//            HttpsURLConnection conn = (HttpsURLConnection) http.openConnection();
//            conn.setRequestMethod("GET"); 
//        }else if(file.trim().startsWith("http")){
//            http = new URL(file);
//            HttpURLConnection conn = (HttpURLConnection) http.openConnection();
//            conn.setRequestMethod("GET"); 
//        }else{
//            http = new File(file).toURI().toURL();
//        }
//        BufferedImage bi = ImageIO.read(http.openStream());
//        
//        Map<String,Integer> sourceHuidu=new HashMap<>();
//        for (int i = 0; i < bi.getWidth(); i++) {
//            for (int j = 0; j < bi.getHeight(); j++){
//            	int rgb=bi.getRGB(i,j);
//            	
//            	int red=(rgb&(255<<16))>>16;
//            	int green=(rgb&(255<<8))>>8;
//            	int blue=rgb&(255);
//            	
//            	int huidu=(red+green+blue)/3;
//            	sourceHuidu.put(i+"_"+j, huidu);
//            	//模版
//            	/*
//            	 * 0  1 0
//            	 * 1 -4 1
//            	 * 0  1 0
//            	 * */
//            	int u=0,l=0,r=0,b=0;
//            	if(i-1<0){
//            		l=0;
//            	}else{
//            		l=sourceHuidu.get((i-1)+"_"+j);
//            	}
//            	
//            	if(i+1>bi.getWidth()-1){
//            		r=0;
//            	}else{
//            		r=ImageUtils.getHuidu(bi.getRGB(i+1,j));
//            	}
//            	
//            	if(j-1<0){
//            		u=0;
//            	}else{
//            		u=sourceHuidu.get(i+"_"+(j-1));
//            	}
//            	
//            	if(j+1>bi.getHeight()-1){
//            		b=0;
//            	}else{
//            		b=ImageUtils.getHuidu(bi.getRGB(i,j+1));
//            	}
//            	
//            	//灰度微分
////            	huidu=(u+l+r+b)-4*huidu;
////            	huidu=getValidHuidu(huidu+100);
////            	int newRGB=new Color(huidu, huidu, huidu).getRGB();
////            	
////	            bi.setRGB(i, j, newRGB);
//            	
//            	//叠加上后
//            	int huiduChange=(u+l+r+b)-4*huidu;
//            	huidu=ImageUtils.getValidHuidu(huidu+huiduChange);
//            	int newRGB=new Color(huidu, huidu, huidu).getRGB();
//            	
//	            bi.setRGB(i, j, newRGB);
//            }
//        }
//       
//        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("jpg");
//        ImageWriter writer = it.next();
//        File f = new File(targetfile);
//        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
//        writer.setOutput(ios);
//        writer.write(bi);
//        bi.flush();
//        ios.flush();
//        ios.close();
//	}
//	
//}