$(document).ready(function(){
	
	
	const usertable = $("#usertable").DataTable({
		
		"scrollY":        "250px",
        "scrollCollapse": true,
		"serverSide": false,
		"columnDefs": [{
			targets: -1,
			className: 'dt-right'
		}],
		'ajax': {
			url: '/admin/users/list', type: 'GET', dataSrc: ""
		},
		"pageLength": 10,
		"lengthChange": true,
		columns: [{
			data : "id"
				},{
			data: 'fname',
					render : function(data,type,row){
						if(row.locked === false)
							return "<p class='text-danger'>" + data + "</p>";
						else
							return data;
					}
				}, {
			data: "lname",
			render : function(data,type,row){
						if(row.locked === false)
							return "<p class='text-danger'>" + data + "</p>";
						else
							return data;
					}
				}, {
			data: "role"
				},{
			data: "datejoined",
				render : function(data){
					if(data == null){
						return "No Data"
					}
					else 
						return data;
				}
				},{
			data : null,
				render : function(){
					return "<button class='btn btn-primary view' data-bs-toggle='modal' "
					+ "data-bs-target='#viewuser'>View</button>" + "<button class='btn btn-danger'>Delete</button>";
				}
			}]
		
	});
	
	let userid = 0;

	$("#usertable tbody").on('click', '.view', function(){
		var data = usertable.row($(this).parents('tr')).data();
			$("#lockswitch").prop("checked",false); 
		userid = data.id;
		$.ajax({
			type : "GET",
			url : "/admin/getUser/" + data.id,
			contentType : "application/json",
			success : function(result){
				if(result.locked === true){
					$("#lockswitch").prop("checked",true);
				}
				$("#name").val(result.fname + ' '+ result.lname);
				$("#email").val(result.email);
				$("#contact").val(result.contactno);
				$("#address").val(result.address.streetno + ' ' + result.address.streetname + ' ' +
								 result.address.baranngay + ' ' + result.address.city + ' ' + result.address.zipcode
								);
			    $("#ordervalue").text(result.timesordered);
			    $("#cancelvalue").text(result.timescancelled);
			}
		});
		
		
		
	});
		$("#lockswitch").click(function(){
		
		let id = userid;
		$.ajax({
			type : "PUT",
			url : "/admin/user/setLock/" + id,
			data : {
				check : $(this).is(":checked")
			},
			success : function(result){
				console.log(result);
			}
		});
		
	});
	
	
	
});