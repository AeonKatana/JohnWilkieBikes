$(document).ready(function(){
	
	
	$("#forgot").submit(function(e){
		e.preventDefault();
		
		
		let email = {};
		email['email'] = $("#email").val();
		
		$(this).find("#verifybtn").prop("disabled", true);
		$(this).find("#verifybtn").text("Sending...");
		
		$.ajax({
			type : "POST",
			url : "/forgotpass",
			contentType : "application/json",
			data : JSON.stringify(email),
			success : function(result){
				if(result == true){
					$("#succ").css("display" , "block");
					$(this).find("#verifybtn").text("Done");
				}
				else{
					$("#fail").css("display", "block");
					$(this).find("#verifybtn").text("Retry");
					$(this).find("#verifybtn").prop("disabled", false);
				}
			}
		});
		
	});
	
	
});