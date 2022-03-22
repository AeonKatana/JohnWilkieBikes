$(document).ready(function() {
  
    
    var formatter = new Intl.NumberFormat('tl-PH', {
				style: 'currency',
				currency: 'PHP'
	});
	
	$(".price").each(function (index) {
		$(this).text(formatter.format($(this).text()));
	});
    
});