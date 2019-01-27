/**
 * 
 */
package com.dcloud.app_dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.junit.Test;

import com.dcloud.process.StringFunc;

/**
 * @brief 这是一个Java API注解的例子（必填）
 * @details （必填）
 * @author 江东
 * @date 2017年3月28日下午5:28:56
 */
public class TestWEIKAIWork {
	public List<WeiKaiWorkItem> readExcel(){  
	    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");  
	    
	    List<WeiKaiWorkItem> resList=new ArrayList<WeiKaiWorkItem>();
	    try {  
	        //同时支持Excel 2003、2007  
	        File excelFile = new File("e:/mei/2017年10月份操作记录.xlsx"); //创建文件对象  
	        FileInputStream is = new FileInputStream(excelFile); //文件流  
	        Workbook workbook = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的  
	        //int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
	        //遍历每个Sheet  
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数  
            //遍历每一行  
            for (int r = 2; r < rowCount; r++) {  
                Row row = sheet.getRow(r);  
               
                Cell cell = row.getCell(1); //客户 
                if(cell==null)continue;
                
                String cellValue = cell.getStringCellValue();
                
               
                Cell cell3 = row.getCell(3);  //操作项目
                String cellValue3 = cell3.getStringCellValue();  
               
                Cell cell7 = row.getCell(7);  //医生
                String cellValue7 = cell7.getStringCellValue();  
               
                
                if(StringFunc.isNull(cellValue)||StringFunc.isNull(cellValue3)||StringFunc.isNull(cellValue7))continue;
                
//                System.out.print(cellValue + "    ");  
//                System.out.print(cellValue3.replace("-", "") + "    ");  
//                System.out.print(cellValue7 + "    \n");  
                
                WeiKaiWorkItem item=new WeiKaiWorkItem();
                item.setDoctor(cellValue7);
                item.setProject(cellValue3.replace("-", ""));
                item.setKehu(cellValue);
                resList.add(item);
            }  
	    }  
	    catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return resList;
	}
	
	public void handleDoctorExcel(List<WeiKaiWorkItem> mainList,String path,String doctorName){  
	    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");  
	    
	    try {  
	        //同时支持Excel 2003、2007  
	        File excelFile = new File(path); //创建文件对象  
	        FileInputStream is = new FileInputStream(excelFile); //文件流  
	        Workbook workbook = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的  
	        //int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
	        //遍历每个Sheet  
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数  
            //遍历每一行  
            XSSFCellStyle styleFind = (XSSFCellStyle) workbook.createCellStyle();
            XSSFFont fontGreen = (XSSFFont) workbook.createFont();
            fontGreen.setColor(HSSFColor.GREEN.index);
            styleFind.setFont(fontGreen);
            
            XSSFCellStyle styleNoFind = (XSSFCellStyle) workbook.createCellStyle();
            XSSFFont fontRed = (XSSFFont) workbook.createFont();
            fontRed.setColor(HSSFColor.RED.index);
            styleNoFind.setFont(fontRed);
            
            for (int r = 2; r < rowCount; r++) {  
                Row row = sheet.getRow(r);  
               
                Cell cellKehu = row.getCell(1); //客户 
                if(cellKehu==null)continue;
                
                String cellkehuValue = cellKehu.getStringCellValue();
               
                Cell cellpro = row.getCell(2);  //操作项目
                String cellValuepro = cellpro.getStringCellValue();  
                
                if(StringFunc.isNull(cellkehuValue)||StringFunc.isNull(cellValuepro))continue;
                
                String[] proarr=cellValuepro.split("、");
                
                cellkehuValue=cellkehuValue.trim();
                if(cellkehuValue.equals("王淑云")){
                	System.out.println("");
                	}
                
                int findCount=0,allProCount=0;
                for (int i = 0; i < proarr.length; i++) {
                	
                	Integer count=1;
                	String pro=proarr[i].replace("-", "");
                	if(proarr[i].contains("x")){
                		String num=pro.substring(pro.indexOf("x")+1, pro.indexOf("x")+2);
                		pro=pro.substring(0, pro.indexOf("x"));
                		count=Integer.parseInt(num);
                	}
                	
                	allProCount+=count;
                	
                	for (int j = 0; j < count; j++) {
                        //比对开始
                        int findIndex=-1;
                        for (int m=0; m< mainList.size() ;m++) {
							if(mainList.get(m).getIsChecked())continue;
							
							if(mainList.get(m).getDoctor().equals(doctorName)
									&&mainList.get(m).getKehu().equals(cellkehuValue)
									&&mainList.get(m).getProject().toLowerCase().equals(pro.toLowerCase())){
								findIndex=m;
								mainList.get(m).setIsChecked(true);
								break;
							}
						}
                        
                        if(findIndex>=0){
                        	findCount++;
                        }else{
                        	System.out.print(cellkehuValue + "    ");  
                            System.out.print(pro + "    "); 
                            System.out.print(doctorName + "    \n");
                        }
					}
				}
                

            	if(findCount==allProCount){
                	//找到就标绿
                	//row.setRowStyle(style);
                	cellpro.setCellStyle(styleFind);
                }else{
                	//cellpro.setCellStyle(styleNoFind);
                }
            }  
            
            
            
            FileOutputStream excelFileOutPutStream = new FileOutputStream(excelFile);
	         // 将最新的 Excel 文件写入到文件输出流中，更新文件信息！
	         workbook.write(excelFileOutPutStream);
	          // 执行 flush 操作， 将缓存区内的信息更新到文件上
	         excelFileOutPutStream.flush();
	         // 使用后，及时关闭这个输出流对象， 好习惯，再强调一遍！
	         excelFileOutPutStream.close();
	         
	         workbook.cloneSheet(0);
	            is.close();
	    }  
	    catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}

	public void handleDoctorLinExcel(List<WeiKaiWorkItem> mainList,String path,String doctorName){  
	    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");  
	    
	    try {  
	        //同时支持Excel 2003、2007  
	        File excelFile = new File(path); //创建文件对象  
	        FileInputStream is = new FileInputStream(excelFile); //文件流  
	        Workbook workbook = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的  
	        //int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
	        //遍历每个Sheet  
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数  
            //遍历每一行  
            XSSFCellStyle styleFind = (XSSFCellStyle) workbook.createCellStyle();
            XSSFFont fontGreen = (XSSFFont) workbook.createFont();
            fontGreen.setColor(HSSFColor.GREEN.index);
            styleFind.setFont(fontGreen);
            
            XSSFCellStyle styleNoFind = (XSSFCellStyle) workbook.createCellStyle();
            XSSFFont fontRed = (XSSFFont) workbook.createFont();
            fontRed.setColor(HSSFColor.RED.index);
            styleNoFind.setFont(fontRed);
            
            for (int r = 2; r < rowCount; r++) {  
                Row row = sheet.getRow(r);  
               
                Cell cellKehu = row.getCell(1); //客户 
                if(cellKehu==null)continue;
                
                String cellkehuValue = cellKehu.getStringCellValue();
               
                Cell cellpro = row.getCell(2);  //操作项目
                String cellValuepro = cellpro.getStringCellValue();  
                
                if(StringFunc.isNull(cellkehuValue)||StringFunc.isNull(cellValuepro))continue;
                
                String[] proarr=cellValuepro.split("、");
                
                cellkehuValue=cellkehuValue.trim();
                if(cellkehuValue.equals("王淑云")){
                	System.out.println("");
                	}
                
                int findCount=0,allProCount=0;
                for (int i = 0; i < proarr.length; i++) {
                	
                	Integer count=1;
                	String pro=proarr[i].replace("-", "");
                	if(proarr[i].contains("x")){
                		String num=pro.substring(pro.indexOf("x")+1, pro.indexOf("x")+2);
                		pro=pro.substring(0, pro.indexOf("x"));
                		count=Integer.parseInt(num);
                	}
                	
                	allProCount+=count;
                	
                	for (int j = 0; j < count; j++) {
                        //比对开始
                        int findIndex=-1;
                        for (int m=0; m< mainList.size() ;m++) {
							if(mainList.get(m).getIsChecked())continue;
							
							if(mainList.get(m).getDoctor().equals(doctorName)
									&&mainList.get(m).getKehu().equals(cellkehuValue)
									&&mainList.get(m).getProject().toLowerCase().equals(pro.toLowerCase())){
								findIndex=m;
								mainList.get(m).setIsChecked(true);
								break;
							}
						}
                        
                        if(findIndex>=0){
                        	findCount++;
                        }else{
                        	System.out.print(cellkehuValue + "    ");  
                            System.out.print(pro + "    "); 
                            System.out.print(doctorName + "    \n");
                        }
					}
				}
                

            	if(findCount==allProCount){
                	//找到就标绿
                	//row.setRowStyle(style);
                	cellpro.setCellStyle(styleFind);
                }else{
                	//cellpro.setCellStyle(styleNoFind);
                }
            }  
            
            
            
            FileOutputStream excelFileOutPutStream = new FileOutputStream(excelFile);
	         // 将最新的 Excel 文件写入到文件输出流中，更新文件信息！
	         workbook.write(excelFileOutPutStream);
	          // 执行 flush 操作， 将缓存区内的信息更新到文件上
	         excelFileOutPutStream.flush();
	         // 使用后，及时关闭这个输出流对象， 好习惯，再强调一遍！
	         excelFileOutPutStream.close();
	         
	         workbook.cloneSheet(0);
	            is.close();
	    }  
	    catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}
	@Test
	public void checkWork() throws FileNotFoundException{
		// 对读取Excel表格内容测试
        TestWEIKAIWork excelReader = new TestWEIKAIWork();
        List<WeiKaiWorkItem> mainList=excelReader.readExcel();
        
        //excelReader.handleDoctorExcel(mainList,"e:/mei/dui/金杰2017年10月份工作量+.xlsx","金杰");
        //excelReader.handleDoctorExcel(mainList,"e:/mei/dui/mia十月.xlsx","蔡羽萱");
        excelReader.handleDoctorExcel(mainList,"e:/mei/dui/林医生.xlsx","林雨婵");
	}
}
