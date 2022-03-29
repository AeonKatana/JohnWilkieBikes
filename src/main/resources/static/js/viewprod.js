
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

	$("#loginbtn").click(function() {
		var url = "http://localhost:8080/login-page";
		window.location.href = url;
	});

});
