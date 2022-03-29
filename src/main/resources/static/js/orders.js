$(document).ready(function() {
  
    
    var formatter = new Intl.NumberFormat('tl-PH', {
				style: 'currency',
				currency: 'PHP'
	});
	
	$(".price").each(function (index) {
		$(this).text(formatter.format($(this).text()));
	});
	
	
	$(".my-rating").starRating({
	    starSize: 25,
	    useFullStars : true,
	    callback: function(currentRating, $el){
	    	
	    }
	});
	
	let prodid = 0;
	let oid = 0;
	$(".review").click(function(){
		prodid = $(this).attr("pid");
		oid = $(this).attr("oid");
	});
    
    $("#addreview").click(function(){
		
	  let rating =	$(".my-rating").starRating('getRating');
	  let message = $("#textreview").val();
	  
	  let review = {
		   rating  : rating,
		   message : message
		};
	  
	  $.ajax({
		 type : "POST",
		 url : "/product/addReview/" + prodid + "/" + oid,
		 contentType : "application/json",
		 data : JSON.stringify(review),
		 success : function(result){
			alert(result);
		}
		})
	  
	  	
		
	});
    
});