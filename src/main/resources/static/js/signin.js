(function() {
		var pw = $('#password');
		var username = $('#uname');
	
    $('#login').click(function(e) {
				$("#sub-err").hide();
				
       	var postParameters = {username: username.val(), password: pw.val()};
       	console.log(pw.val());
				$.post("/login", postParameters, function(res) {
						console.log(res);
					var response = JSON.parse(res);
				
					if (jQuery.isEmptyObject(response)) {
						$("#sub-err").show();
					} else {
						console.log("here")
						window.location = "/home";
						$.post("/home");
					}
				}); 
    });
})();