package com.johnwilkie.shop.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String fname;

	private String lname;

	private String role;

	private String username;

	private char[] password;

	private String email;

	private String verifytoken;

	private String passwordtoken;
	
	private LocalDateTime datejoined;

	private int timesordered;
	
	private int timescancelled;
	
	private long contactno;

	private boolean enabled;
	@Default
	private boolean locked = true;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userid", cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JsonManagedReference
	private Set<Cart> usercart;

	@OneToMany(mappedBy = "user")
	@JsonManagedReference
	private Set<Orders> orders;

	
	@OneToMany(mappedBy = "user")
	@JsonManagedReference
	private Set<Review> reviews;
	
	
	@OneToOne(mappedBy = "user")
	@JsonManagedReference
	private UserAddress address;
	
}
