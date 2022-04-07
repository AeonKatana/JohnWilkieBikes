
$(document).ready(function() {


	$(".my-rating").starRating({
		starSize: 30,
		readOnly: true,
		callback: function(currentRating, $el) {

		}
	});
	
	$(".review-rating").starRating({
		starSize : 20,
		readOnly : true,
		callback : function(currentRating , $el){
			
		}
	})

	let stock = "";
	let prodid = parseInt($("#thisprod").attr("pid"));
	let qty = parseInt($("#box").val());
	let varid = "";


	$("input[name='variation']").click(function() {
		$("#plus").prop("disabled", false);
		$("#addcartbtn").prop("disabled", false);
		varid = $(this).attr("vid");
		$("#addcartbtn").css("cursor", "pointer");
		$("#buynow").prop("disabled", false);
		$.ajax({
			type: "GET",
			url: "/product/" + prodid + "/" + varid,
			contentType: "application/json",
			success: function(result) {
				
				console.log(result.name);
				$("#price").html("Price : " + '&#8369;' + parseFloat(result.discountedprice).toFixed(2));
				$("#stock").text("Stock :" + result.stocks);
				$("#stock").attr("stock", result.stocks);
				stock = $("#stock").attr("stock");
				parseInt($("#box").val("1"));
				qty = parseInt($("#box").val());
			}
		});


	});


	//Increment and Decrement Button

	if (qty <= 1) {
		$("#minus").prop("disabled", true);
	}

	$('#minus').click(function() {
		if (parseInt($("#box").val()) >= 2) {
			$("#box").val(parseInt($("#box").val()) - 1);
			qty = parseInt($("#box").val());
			$("#plus").prop("disabled", false);
			if (qty <= 1) {
				$("#minus").prop("disabled", true);
			}
		}
	});

	$("#plus").click(function() {
		if (parseInt($("#box").val()) < stock) {
			$("#box").val(parseInt($("#box").val()) + 1);
			qty = parseInt($("#box").val());
			$("#minus").prop("disabled", false);
			if (qty == stock) {
				$("#plus").prop("disabled", true);
			}

		}
	});

	// Add to Cart

	$("#addcartbtn").click(function() {
		console.log(qty);

		$.ajax({
			type: "POST",
			url: "/addtocart/" + prodid + "/" + qty,
			contentType: "application/json",
			success: function(result) { // Is user Loggged in ?
				if (!result) {
					$("#modalmsg").text("Login first!");
				}
				else {
					$.ajax({
						type: "POST",
						url: "/addtocart/" + prodid + "/" + varid + "/" + qty + "/allow",
						contentType: "application/json",
						success: function(result) {  // Cart Add/Update Message
							$("#modalmsg").text(result);
						}

					});
				}
			}
		});
	});
	
	
	$("#buynow").click(function(){
		
		$.ajax({
			type : "POST",
			url : "/buynow",
			success : function(result){
				if(!result){
					$("#buynowmsg").text("Login First")
				}
				else{	
					$("#buynowmsg").css("display","none");
					$("#exampleModalLongTitle").text("Confirm");
				    $("#checkout").css("display","block");
					$(".varid").val(varid);
					$(".qty").val(qty);
					console.log(qty);
				}	
			}
		});
		
	});
	

	$("#loginbtn").click(function() {
		var url = "/login-page";
		window.location.href = url;
	});
	$("#loginbtns").click(function() {
		var url = "/login-page";
		window.location.href = url;
	});


});
