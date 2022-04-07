$(document).ready(function(){
	
	
	$("#editbtn").click(function(){
		
		
		$(".form-control").prop("disabled", false);
		$("#savebtn").css("display","block");
		$("#cancelbtn").css('display',"block");
		$(this).css("display","none");
	});
	
	$("#cancelbtn").click(function(){
		$(this).css("display","none");
		$("#editbtn").css("display","block");
		$(".form-control").prop("disabled", true);
		$("#savebtn").css("display","none");
		$("#editbtn").prop("disabled",false);
	});
	
	$("#savebtn").click(function(){
		alert("Changes saved and applied")
	})
	
});