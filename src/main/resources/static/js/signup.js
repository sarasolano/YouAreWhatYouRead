document.onload(function() {
        var form = $('#login');
    $('submit').on('click', function(e) {
				$("#username-err").hide();
				$("#pwd-err").hide();
				$("sub-err").hide();
        var pw = $("pwd");
				var username = $('username');
        if (username.val().length == 0) {
            $("#username-err").show();
         } else if (pw.val().length < 8 || pw.length > 36) {
            $("#pwd-err").show();
        } else {
            var postParameters = {username: username.val(), password: pw.val()};
						
            $.post("/login", postParameters, function(res) {
							var response = JSON.parse(res);
							if (response.length == 0) {
								$("sub-err").show();
							} else {
								
							}
						}); 
        }
    });
});

function exists(username) {
	var isUserName = false;
	var params = {"username" : username};
	$.post("/exists", params, function(res) {
		if (res.isUserName) {
			isUserName = true;
		}
	}
	return isUserName;
}