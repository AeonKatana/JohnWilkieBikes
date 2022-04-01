$(document).ready(function(){
	
		var formatter = new Intl.NumberFormat('tl-PH', {
				style: 'currency',
				currency: 'PHP'
			});
	
	const ordertable = $("#ordertable").DataTable({
		"createdRow": function( row, data, dataIndex ) {
    	if ( data.ordertype === 'paypal') {
      		$(row).addClass( 'text-info' );
    		}
  		},
  		"aaSorting": [],
  		 "order": [],
		"scrollY":        "250px",
        "scrollCollapse": true,
		"serverSide": false,
		"columnDefs": [{
			targets: -1,
			className: 'dt-right'
		},{
			 "targets": [ 0 , 2],
			 "searchable" : true,
              "visible": false	
		}],
		'ajax': {
			url: '/admin/orders/quicklist', type: 'GET', dataSrc: ""
		},
		"pageLength": 10,
		"lengthChange": false,
		columns: [{
			data : "ordercode"
				},{
			data : 	"bikeprod.prodimgurl",
					render : function(data){
						return "<img src='https://ik.imagekit.io/aeonkatana/" + data+"' width='100px;'>"
					}	
				},{
			data : "id"
				},{
			data : null,
					render : function(data,type,row){
						return row.month + ' ' + row.day + ' ' + row.year;
					}		
				},{
			data: "bikeprod.prodname"
				}, {
			data: "variation"
				},{
			data: "price",
					render : function(data){
						return formatter.format(data);
					}		
				}, {
			data: "user.fname"
			
				 }, {
			data : "ordertype"
				},{
			data: "status",
					render : function(data,type,row){
						if(data === 'DELIVERED' || data === 'PICKEDUP'){
							return "<button class='btn btn-primary form-control view' data-bs-target='#viewuser' data-bs-toggle='modal' disabled>View</button><select disabled class='form-select statuses' id='status"+ row.id +"'>"            
						       + "<option value='" + data +"' selected>"+ data +"</option>"
						       +"</select>";
						}
						else
						return "<button class='btn btn-primary form-control view' data-bs-target='#viewuser' data-bs-toggle='modal'>View</button><select class='form-select statuses' id='status"+ row.id +"'>"            
						       + "<option value='" + data +"' selected>"+ data +"</option>"
						       + "<option value='PACKAGING'>PACKAGING</option>"
						       + "<option value='DELIVERING'>ON DELIVERY</option>"
						       + "<option value='DELIVERED'>DELIVERED</option>"
						       + "<option value='PICKEDUP'> PICKED UP</option>"
						       + "<option value='CANCELLED'>CANCEL</option>"
						       +"</select>";
					}
				}]
	});
	
	//-------------------------------------DataTable End-------------------------------------
	
	
	//------------------------------------ AJAX and Stuff------------------------------------
	
	
	$("#ordertable tbody").on('change','.statuses',function(){
		var data = ordertable.row($(this).parents('tr')).data(); 
		let status = $('#status' + data.id).val();
		
		$.ajax({
			type : "PUT",
			url : "/admin/orders/changeStatus/" + data.id,
			data : {
				status : status
			},
			success : function(result){
				$("#ordertable").DataTable().ajax.reload();
			}
		});
		
	});
	
	$("#ordertable tbody").on("click", ".view", function(){
		var data = ordertable.row($(this).parents('tr')).data(); 
		
		$.ajax({
			type : "GET",
			url : "/admin/orders/getOrder/" + data.id,
			success: function(result){
				console.log(result);
				if(result.ordertype === 'paypal'){
					$("#paymethod").text("Paid Delivery")
				}
				else if(result.ordertype === 'cop'){
					$("#paymethod").text("CASH ON PICKUP");
				}
				else if(result.ordertype === 'cod')
					$("#paymethod").text("CASH ON DELIVERY");
				$("#ordercode").text('Order Code : ' + result.ordercode);
				$("#name").val(result.user.fname + ' ' +  result.user.lname);
				$("#prodname").val(result.bikeprod.prodname + ' : ' + result.variation);
				$("#email").val(result.user.email);
				$("#contactno").val(result.user.contactno);
				$("#address").val(result.user.address.streetno + ' ' + result.user.address.streetname + ' ' +
								 result.user.address.baranngay + ' ' + result.user.address.city + ' ' + result.user.address.zipcode)
			}
		});
		
	});
	
	
});