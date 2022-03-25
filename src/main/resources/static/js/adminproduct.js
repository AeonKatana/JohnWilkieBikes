$(document).ready(function(){
	
	
	
	var cptable = $("#prodtable").DataTable({
		"serverSide": false,
		"columnDefs": [{
			targets: -1,
			className: 'dt-right'
		}],
		'ajax': {
			url: '/admin/products/prodlist', type: 'GET', dataSrc: ""
		},
		"pageLength": 5,
		"lengthChange": false,
		columns: [{
			data: 'prodname',
				}, {
			data: "priceRange",
				}, {
			data: "category",
					render: function(data){
						console.log(data);
						return "<span class='badge bg-info'> " + data[0].category.categoryname + "</span>"
					}
				},{
				data : "prodStocks"
			},{
				data : null,
				render : function(){
					return "<button class='btn btn-primary'>Edit</button>" + "<button class='btn btn-danger'>Delete</button>";
				}
			}]
	});
	$("#cptable tbody").on('click', '.addscorecard', function() {
		var data = cptable.row($(this).parents('tr')).data();
	});
	
	
});