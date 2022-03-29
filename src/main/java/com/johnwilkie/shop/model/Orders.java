package com.johnwilkie.shop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "order_status")
	private String status = "PROCESSING";

	private String month;
	private int day;
	private int year;
	@Column(name = "order_date")
	private LocalDateTime datetime;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("orders")
	@JoinColumn(name = "bike_id")
	private BikeProduct bikeprod;

	@ManyToOne
	@JsonIgnoreProperties("orders")
	@JoinColumn(name = "customer_id")
	private User user;

	private BigDecimal price;

	private String ordertype;

	private String variation;

	private int quantity;
	
	private String ordercode;
	
	private boolean reviewed;
	
}
