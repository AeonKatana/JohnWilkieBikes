$(document).ready(function () {

			if (!!window.performance && window.performance.navigation.type == 2) { // if Back button was pressed to go back to this page then refresh
				window.location.reload();
			}

			var formatter = new Intl.NumberFormat('tl-PH', {
				style: 'currency',
				currency: 'PHP'
			});

			$(".itemprice").each(function (index) {
				$(this).text(formatter.format($(this).text()));
			});


			function getTotal() {

				$.ajax({
					type: "GET",
					url: "/mycart/totalQty",
					contentType: "application.json",
					success: function (data) {
						var value = parseFloat(data).toFixed(2);

						$("#totalPrice").html(formatter.format(value));

					}
				})
			}
			$(getTotal()); // Refresh Total Price 

			$('.minusbtn').click(function () { // Decrease QTY
				let prodId = $(this).attr("pid");
				let priceId = $(this).attr("vid");
				let varid = $(this).attr("vid");

				let stock = $(this).attr("stock");
				let qty = parseInt($("#box" + varid).val());
				if (parseInt($("#box" + varid).val()) >= 2) {
					$.ajax({
						type: "PUT",
						url: "/mycart/minus/" + prodId + "/" + varid,
						contentType: "application/json",
						success: function (result) {
							$("#box" + varid).val(result.quantity);
							$("#price" + varid).html(formatter.format(result.qtyPrice));
							getTotal();
							qty = parseInt($("#box" + varid).val());

							$("#itemid" + varid).prop("disabled", false);
							if (qty <= 1) {
								$("#minus" + varid).prop("disabled", true);

							}
						}
					});

				}

			});
			$(".addbtn").click(function () { // Increase QTY
				let prodId = $(this).attr("pid");
				let priceId = $(this).attr("vid");
				let varid = $(this).attr("vid");

				let stock = $(this).attr("stock");

				let qty = parseInt($("#box" + varid).val());
				if (parseInt($("#box" + varid).val()) < stock) {

					$.ajax({
						type: "PUT",
						url: "/mycart/add/" + prodId + "/" + varid,
						contentType: "application/json",
						success: function (result) {

							$("#box" + varid).val(result.quantity);
							$("#price" + varid).html(formatter.format(result.qtyPrice));
							getTotal();
							qty = parseInt($("#box" + varid).val());
							$("#minus" + varid).prop("disabled", false);
							if (qty == stock) {
								$("#itemid" + varid).prop("disabled", true);

							}
						}
					});
				}




			});

			$(".remover").click(function () { // Delete Cart Item
				let prodId = $(this).attr("pid");
				
				let thiscart = $(this).attr("pid");
				//	alert("Cart ID : " + prodId);
			let varid = $(this).attr("vid");

				$.ajax({
					type: "DELETE",
					url: "/mycart/delete/" + prodId + "/"  + varid,
					contentType: "application/json",
					success: function (result) {
						$("#cartbox" + varid).fadeOut(1000, function() {
							console.log(result);
							location.reload();
						});
					}
				});
			});
	


			$("#checkout").click(function () {
				window.location.href = "/mycart/checkout";
			});


		});
