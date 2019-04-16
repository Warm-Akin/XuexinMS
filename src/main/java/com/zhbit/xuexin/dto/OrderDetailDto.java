package com.zhbit.xuexin.dto;

public class OrderDetailDto {

	private String totalFee;

	private String type; // type = '1' -> annualFee; type = '2' -> normalFee

	private String companySoleCode;

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCompanySoleCode() {
		return companySoleCode;
	}

	public void setCompanySoleCode(String companySoleCode) {
		this.companySoleCode = companySoleCode;
	}
}
