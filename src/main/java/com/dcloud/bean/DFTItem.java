package com.dcloud.bean;

public class DFTItem{
	private double shishu;
	private double xushu;
	public DFTItem(){
		
	}
	
	/**
	 * @param shishu
	 * @param xushu
	 */
	public DFTItem(double shishu, double xushu) {
		super();
		this.shishu = shishu;
		this.xushu = xushu;
	}

	public double getShishu() {
		return shishu;
	}
	public void setShishu(double shishu) {
		this.shishu = shishu;
	}
	public double getXushu() {
		return xushu;
	}
	public void setXushu(double xushu) {
		this.xushu = xushu;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "shishu:"+shishu+",xushu="+xushu;
	}
	
	/**
	 * @brief 乘以
	 * @details （必填）
	 * @param x
	 * @return
	 * @author 江东
	 * @date 2017年8月30日下午4:47:38
	 */
	public DFTItem multiple(DFTItem x){
		 DFTItem res=new DFTItem();
		 res.setShishu(this.getShishu()*x.getShishu()-this.getXushu()*x.getXushu());
		 res.setXushu(this.getShishu()*x.getXushu()+this.getXushu()*x.getShishu());
		 return res;
	}
	
}