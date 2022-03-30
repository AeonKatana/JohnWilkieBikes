$(document).ready(function(){
	
	$(getCategories()); // Initialize Categories
	$(populateProductDropdown());
	//----------------------------------- DataTable Initialization-------------------------------------------
	
	var cptable = $("#prodtable").DataTable({
		"createdRow": function( row, data, dataIndex ) {
    	if ( data.featured) {
      		$(row).addClass( 'bg-warning' );
    		}
  		},
		"scrollY":        "250px",
        "scrollCollapse": true,
		"serverSide": false,
		"columnDefs": [{
			targets: -1,
			className: 'dt-right'
		}],
		'ajax': {
			url: '/admin/products/prodlist', type: 'GET', dataSrc: ""
		},
		"pageLength": 10,
		"lengthChange": true,
		columns: [{
			data : "prodimgurl",
					render : function(data){
						return "<img src='https://ik.imagekit.io/aeonkatana/" + data+"' width='100px;'>"
					}
				},{
			data: 'prodname',
				}, {
			data: "priceRange",
				}, {
			data: "category",
				   render : function(data,type,row){
					 var output = ""
					  if(row.category.length == 0){
						return "<button class='btn btn-info btn-sm px-3 py-1 mb-1 categs' data-bs-toggle='modal' data-bs-target='#prodcatadd'>+</button>"
					   }
						for(let i = 0 ;i < row.category.length;i++){
						    output += "<span class='lead'><span class='badge bg-info p-2'>" + row.category[i].category.categoryname +"</span></span> ";
						}
						return output + "<button class='btn btn-info btn-sm px-3 py-1 mb-1 categs' data-bs-toggle='modal' "
					+ "data-bs-target='#prodcatadd'>+</button>";
					}
				},{
				data : null,
					render : function(data,type,row){
						total = 0;
						for(let i = 0;i < row.variation.length;i++){
							total += row.variation[i].stocks;	
						}
						return total;
					}
			},{
				data : "featured",
				render : function(data){
					if(data)
						return "<input type='checkbox' class='check' checked>" + "<span style='display:none;'>"+ data +"</span>";
					else
						return "<input type='checkbox' class='check'>" + "<span style='display:none;'>"+ data +"</span>";
				}
			},{
				data : null,
				render : function(){
					return "<button class='btn btn-primary edit' data-bs-toggle='modal' "
					+ "data-bs-target='#editprod'>Edit</button>" + "<button class='btn btn-danger'>Delete</button>";
				}
			}]
	});
	
	//------------------------------------------------DataTable End--------------------------------------------------------------------
	
	// ------------------------------------------- Front End Logic-------------------------------
	
	let prodid = 0; // ID of selected product
	
	
	$("#categories").hide();
	
	$("#showcateg").click(function(){  // Show dropdown after clicking (+) button
		$(populateDropdown());
		$("#categories").show();
	});
	
	
	$("#prodtable tbody").on('click', '.check', function(){
		var data = cptable.row($(this).parents('tr')).data();
		let isCheck = ($(this).is(":checked"));
		$.ajax({
		     type : "PUT",
		     url : "/admin/products/featureProd/" + data.id,
		     data : {
				check : isCheck
			}	,
			success : function(result){
				$("#prodtable").DataTable().ajax.reload();
			}
		})
		
	});
	
	
	
	$("#prodtable tbody").on('click', '.categs', function() { // Click on (+) Button to add category
		var data = cptable.row($(this).parents('tr')).data(); 
		prodid= data.id;
		$("#category").find("span").remove();
		$.ajax({
			type : "GET",
			url : "/admin/products/prodlist/" + data.id,
			contentType : "application/json",
			success: function(result){	
				for(let x = 0; x < result.category.length;x++){ 
					$("#category").append(" <span class='lead'><span class=\"badge bg-info\"id='" + result.category[x].category.id + "'>" +  result.category[x].category.categoryname + " <i class='fa-solid text-dark m-1 fa-xmark removers'></i></span></span>")
				}
			
				$(".removers").click(function(){  // Delete the newly added category
					let categid = $(this).parent().attr("id");
					$("#" + categid).parent().remove();
					console.log($("#" + categid).fadeOut());
					$("#" + categid).fadeOut();
					$.ajax({
						type : "DELETE",
						url : "/admin/deleteCategory/product/" + prodid + "/" + categid,
						contentType : "application/json",
						success : function (result){
							
							alert("Category Removed!");
							$("#prodtable").DataTable().ajax.reload();
						}
			});
		});
			
				
			}
		});
		
	});
	$("#prodtable tbody").on('click', '.edit', function() { // Click on Edit Button
		var data = cptable.row($(this).parents('tr')).data(); 
		prodid = data.id;
		console.log(prodid);
		$("#saveprod").prop("disabled",true); 
		$("#price").val("");      // Refresh Values
		$("#stocks").val("");
		$("#variation").find("option").remove().end().append("<option value='0'>Select Variation</option>");
		
		
		
		// Get Variations of the selected product
		$.ajax({
			type : "GET",
			url : "/admin/products/prodlist/" + data.id,
			contentType : "application/json",
			success: function(result){
				
				$("#prodid").val(result.id);
				$("#name").val(result.prodname)
				$("#description").val(result.proddesc);
				$("#discount").val(result.proddiscout);
				
			   // Populate Dropdown for Variations Select	
			
				for(let i = 0;i < result.variation.length;i++){
					$("#variation").append("<option value='"+ result.variation[i].id +"'>" + result.variation[i].name + "</option>")
				}
			}
		});
	});
	
	// Immediately add the category after selecting dropdown
	$("#categories").on('change',function(){
		let id = $("#categories option:selected").val();
		let name = $("#categories option:selected").text();
		
		if(id == 0){
			return false;
		}
		
		$("#category").append(" <span class='lead'><span class=\"badge bg-info\"id='" + id + "'>" +  name + " <i class='fa-solid text-dark m-1 fa-xmark remover'></i></span></span>");
		$.ajax({
			type : "POST",
			url : "/admin/addCateg/product/" + prodid + "/" + id,
			contentType : "application/json",
			success : function(result){
				alert(result);
				$("#prodtable").DataTable().ajax.reload();
			}
		});
		
		
		
		
		$(this).hide(); // Hide after selecting an option
		
			$(".remover").click(function(){ // Enables the X button to delete category
					let categid = $(this).parent().attr("id");
					$("#" + categid).parent().remove();
					console.log($("#" + categid).fadeOut());
					$.ajax({
						type : "DELETE",
						url : "/admin/deleteCategory/product/" + prodid + "/" + categid,
						contentType : "application/json",
						success : function (result){
							console.log("WHat");
							
							alert("Category Removed!");
							$("#prodtable").DataTable().ajax.reload();
						}
					});
			});
		
	});
	// Variation Select Event
	let varid = 0;
	$("#variation").on('change',function(){
			let id = $("#variation option:selected").val();
			varid = id;
			if(id == 0){
				$("#saveprod").prop("disabled",true);
			}
			else{
				$("#saveprod").prop("disabled", false);
			}
			
			$.ajax({
				type : "GET",
				url : "/admin/products/product/" + prodid + "/" + id,
				contentType: "application/json",
				success : function(data){
					$("#price").val(data.price);
					$("#stocks").val(data.stocks);			
				}
			});
	});
	
	//Closes the Alert
	$("#closealert").click(function(){
		$("#notif").css("display","none");
	});
	
	// Save the Updates
	$("#updateprod").submit(function(){
		$("#saveprod").prop("disabled",true);
		$("#saveprod").text("Saving...");
		$.post($(this).attr("action"), $(this).serialize(),function(){
			$("#notif").css("display","block");
			$("#notif").show();
			$("#saveprod").prop("disabled",false);
			$("#saveprod").text("Save changes");
			$("#prodtable").DataTable().ajax.reload();
		});
		return false;
	});
	
	
 $(document).ready(function(){
	let v = 1;
	// Generate more variation forms
	$("#morevar").click(function(){
		$("#main").append(
			'<div id="'+ v + '" class="container varo">'
			+ '<div class="text-end"><button type="button" id="b'+ v +'" class="btn-close"></button></div><input type="text" id="varname" name="name" class="form-control my-2 varname"'
			+ 'placeholder="Variation Name">' 
			+ '<label for="price">Product Price</label> '
			+ '<input type="number" id="price" class="form-control prices" placeholder="0" name="price" required>'
			+ '<label for="stocks">Product Stock</label> <input type="number"'
			+ 'id="stocks" class="form-control stocks" placeholder="0" name="stock" required>'
			+ '</div>'
		);
		$(".varo button").click(function(){ // X button for generated Variation Forms
			$(this).parent().parent().remove();
		});
		v++;
		
	
	});
	
	// Add new Variation to an exisiting product
	$("#newvarform").submit(function(e){
		e.preventDefault();
		
		let prodid = $("#prods").val();
		
		let varprice = [];
		let varstocks = [];
		let varnames = [];	

	
		
		$('.varname2').each(function(i,obj){
			varnames[i] = $(this).val();
		});

		$('.prices2').each(function(i,obj){
			varprice[i] = $(this).val();
		});
		
		$('.stocks2').each(function(i,obj){
			varstocks[i] = $(this).val();
		});
		var form  = new FormData();
		form.append('prodid', prodid);
		form.append('price',varprice);
		form.append('stock', varstocks);
		form.append('name',varnames);
		
		$.ajax({
			type : "POST",
			url : "/admin/addVariation",
			data : form,
			contentType : false,
			processData : false,
			success : function(result){
				$("#savenewvar").text("Done");
			    $("#addnotif").modal('show');
			    $("#closenotif").click(function(){
					window.location.reload();
				});
			}
		})
		
	});
	
	// Add new Product to the Store
	$("#testarray").submit(function(e){
		e.preventDefault();
		
		let product = $("#prodname").val();
		let descript = $("#proddesc").val();
		let prodimg = $("#fileImage")[0].files[0];
		
		
		let varprice = [];
		let varstocks = [];
		let varnames = [];	

	
		
		$('.varname').each(function(i,obj){
			varnames[i] = $(this).val();
		});

		$('.prices').each(function(i,obj){
			varprice[i] = $(this).val();
		});
		
		$('.stocks').each(function(i,obj){
			varstocks[i] = $(this).val();
		});
		
		console.log(varstocks);
		var form  = new FormData();
		form.append('prod', product);
		form.append('desc', descript);
		form.append('price',varprice);
		form.append('stock', varstocks);
		form.append('name',varnames)
		form.append('file', prodimg);
		$("#savenewprod").text("Please wait...");
		$("#savenewprod").prop("disabled",true);
		$("#closeedit").prop("disabled",true);
	$.ajax({
			type : "POST",
			url : "/admin/newProduct",
			data : form,
			contentType : false,
			processData : false,
			success : function(result){
				$("#savenewprod").prop("disabled",false);
		$("#closeedit").prop("disabled",false);
				$("#savenewprod").text("Done");
			    $("#addnotif").modal('show');
			    $("#closenotif").click(function(){
					window.location.reload();
				});
			}
		}) 
	});
})	
	
	
	// Delete categories
	
	$(document).ready(function(){
		
		
		
	});
	// Add categories
	$("#categsave").click(function(){
		var newcateg = {};
		newcateg['categoryname'] = $("#newcateg").val();
		$.ajax({
			type : "POST",
			url : "/admin/addCateg",
			contentType : "application/json",
			data : JSON.stringify(newcateg),
			success : function (result){
				alert("Category Added!");
				location.reload();
		//		$("#here").append(" <span class='lead'><span id=" + result.id  +" class='badge bg-info'>" +  result.categoryname + " <i id='x' class='fa-solid text-dark m-2 fa-xmark'></i></span></span>");
			}
		});
	});
	
	
	
	
	
});


// Some Functions for Populating or showing some data

function populateDropdown(){
	$("#categories").find("option").remove().end().append("<option value='0'>Select Category</option>");
	$.ajax({
		type : "GET",
		url : "/admin/getCategories",
		contentType : "application/json",
		success : function(result){
			for(let i = 0 ; i < result.length;i++){
				$("#categories").append("<option value='" + result[i].id + "'>" +  result[i].categoryname + " </option>");
			}
		}
	})
	
	
}

function populateProductDropdown(){
	
	$.ajax({
			type : "GET",
			url : "/admin/products/prodlist",
			contentType : "application/json",
			success: function(result){
			   // Populate Dropdown for Variations Select	
				for(let i = 0;i < result.length;i++){
					$("#prods").append("<option value='"+ result[i].id +"'>" + result[i].prodname + "</option>")
				}
			}
		});
	
}



function getCategoriesofone(){
	
	$.ajax({
		type : "GET",
		url : "/admin/getCategories",
		contentType : "applicaton/json",
		success : function(result){
			for(let i = 0 ; i < result.length;i++){
				$("#category").append(" <span class='lead'><span class=\"badge bg-info\"id='" + result[i].id + "'>" +  result[i].categoryname + " <i class='fa-solid text-dark m-2 fa-xmark '></i></span></span>");
			}
			
			
		}
	});
	
}
function getCategories(){
	
	$.ajax({
		type : "GET",
		url : "/admin/getCategories",
		contentType : "applicaton/json",
		success : function(result){
			for(let i = 0 ; i < result.length;i++){
				$("#here").append(" <span class='lead'><span class=\"badge bg-info\"id='" + result[i].id + "'>" +  result[i].categoryname + " <i class='fa-solid text-dark m-2 fa-xmark'></i></span></span>");
			}
			$(".fa-xmark").click(function(){
			let categid = $(this).parent().attr("id");
			$.ajax({
				type : "DELETE",
				url : "/admin/deleteCategory/" + categid,
				contentType : "application/json",
				success : function(result){
					alert(result);
					$("#" + categid).fadeOut();
				}
			});
		});
		}
	});
	
}