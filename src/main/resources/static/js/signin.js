(function() {
		var pw = $('#password');
		var username = $('#uname');
	
    $('#login').click(function(e) {
				$("#sub-err").hide();
				
       	var postParameters = {username: username.val(), password: pw.val()};
				$.post("/login", postParameters, function(res) {
					var response = JSON.parse(res);
					if (jQuery.isEmptyObject(response)) {
						$("#sub-err").show();
					} else {

					}
				}); 
    });
})();