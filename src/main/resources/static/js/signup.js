(function() {
    var form = $('#signup');
		var pw = $("#pwd");
		var pw2 = $("#pwd2");
		var username = $('#username');
	
//		username.bind("keyup", function(e) {
//			$("#repeat-err").hide();
//				if (exists(username)) {
//					$("#repeat-err").show();
//					username.css("border", "red solid 1px");
//				} else {
//					username.css("border", "green solid 1px");
//				}
//			});
//		});
	
		username.click(function(e) {
			username.css("border", "grey solid 1px");
		});
		
		pw.click(function(e) {
			pw.css("border", "grey solid 1px");
		});
	
		pw2.click(function(e) {
			pw2.css("border", "grey solid 1px");
		});
		
    $('#submit').on('click', function(e) {
				$("#username-err").hide();
				$("#pwd-err").hide();
				$("#sub-err").hide();
				$("#pwd2-err").hide();
			
        if (username.val().length == 0) {
					$("#username-err").show();
					username.css("border", "red solid 1px");
        } else if (pw.val().length < 8 || pw.val().length > 36) {
					$("#pwd-err").show();
					pw.css("border", "red solid 1px");
        } else if (pw.val() != pw2.val()) {
					$("#pwd2-err").show();
					pw2.css("border", "red solid 1px");
				} else {
            var postParameters = {username: username.val(), password: pw.val()};
            $.post("/signup", postParameters, function(res) {
							var response = JSON.parse(res);
							if (jQuery.isEmptyObject(response)) {
								$("sub-err").show();
							} else {
								
							}
						}); 
        }
    });
})();

function exists(username) {
	var isUserName = false;
	var params = {"username" : username};
	$.post("/exists", params, function(res) {
		if (res.isUserName) {
			isUserName = true;
		}
	});
	return isUserName;
}