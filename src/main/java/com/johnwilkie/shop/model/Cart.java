package com.johnwilkie.shop.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "bike_product_id")
	private BikeProduct bikeprod;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "prod_variation")
	private BikeProdVariation variation;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "user_id")
	private User userid;

	private int quantity;

	@Transient
	private BigDecimal qtyPrice;

	@Transient
	public BigDecimal getQtyPrice() {
		BikeProdVariation variation = getVariation();
		this.qtyPrice = variation.getDiscountedprice().multiply(new BigDecimal(this.quantity));
		return this.qtyPrice;
	}
}
