package com.zhbit.xuexin.model;

import java.util.Date;

import javax.persistence.Entity;

//@Entity
//@Table(name="t_order")
public class Order {

//	@Id
//	@GeneratedValue
//	private Integer id; // 编号
//
//	private String orderNo; // 订单号
//
//	private Integer productId; // 商品ID
//
//	private String subject; // 订单名称
//
//	private String body; // 商品描述
//
//	private String totalAmount; // 总金额
//
//	private Date buyTime; // 购买日期时间
//
//	private String nickName; // 支付人
//
//	private String floor; // 几栋
//
//	private String domitoryNo; // 楼层
//
//	private String way; // 支付方式
//
//	private int isPay; // 是否支付 0 未支付 1 支付
//
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public String getOrderNo() {
//		return orderNo;
//	}
//
//	public void setOrderNo(String orderNo) {
//		this.orderNo = orderNo;
//	}
//
//
//	public Integer getProductId() {
//		return productId;
//	}
//
//	public void setProductId(Integer productId) {
//		this.productId = productId;
//	}
//
//
//	public String getSubject() {
//		return subject;
//	}
//
//	public void setSubject(String subject) {
//		this.subject = subject;
//	}
//
//	public String getBody() {
//		return body;
//	}
//
//	public void setBody(String body) {
//		this.body = body;
//	}
//
//	public String getTotalAmount() {
//		return totalAmount;
//	}
//
//	public void setTotalAmount(String totalAmount) {
//		this.totalAmount = totalAmount;
//	}
//
//	public Date getBuyTime() {
//		return buyTime;
//	}
//
//	public void setBuyTime(Date buyTime) {
//		this.buyTime = buyTime;
//	}
//
//
//	public String getNickName() {
//		return nickName;
//	}
//
//	public void setNickName(String nickName) {
//		this.nickName = nickName;
//	}
//
//    public String getFloor() {
//        return floor;
//    }
//
//    public void setFloor(String floor) {
//        this.floor = floor;
//    }
//
//    public String getDomitoryNo() {
//        return domitoryNo;
//    }
//
//    public void setDomitoryNo(String domitoryNo) {
//        this.domitoryNo = domitoryNo;
//    }
//
//    public String getWay() {
//		return way;
//	}
//
//	public void setWay(String way) {
//		this.way = way;
//	}
//
//	public int getIsPay() {
//		return isPay;
//	}
//
//	public void setIsPay(int isPay) {
//		this.isPay = isPay;
//	}
	
	private String totalFee;

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
}
