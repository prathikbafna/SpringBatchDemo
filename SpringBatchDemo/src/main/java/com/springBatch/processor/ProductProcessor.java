package com.springBatch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.springBatch.entity.Product;

public class ProductProcessor implements ItemProcessor<Product,Product>{

	@Override
	public Product process(Product item) throws Exception {
		// TODO Auto-generated method stub
		Double cost = item.getProductCost();
		item.setProductDiscount(cost*12/100);
		item.setProductGst(cost*18/100);;
		return item;
	}

}
