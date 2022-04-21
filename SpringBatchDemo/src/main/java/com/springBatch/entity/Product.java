package com.springBatch.entity;

import lombok.Data;

@Data
public class Product {
	private Integer productId;
	private String productName;
	private Double productCost;
	
	private Double productDiscount;
	private Double productGst;

}
