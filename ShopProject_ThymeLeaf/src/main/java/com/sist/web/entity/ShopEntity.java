package com.sist.web.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "goods")
@Getter@Setter
public class ShopEntity {
	@Id
	private int gno;
	private int cprice, price, cno,hit;
	private String goods_name,goods_image,goods_detail_image, brand, origin;
}  
 