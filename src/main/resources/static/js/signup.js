document.onload(function() {
        var form = $('#login');
    $('submit').on('click', function(e) {
        var pw = $("pwd");
				var username = $('username');
        if (username.val().length == 0) {
             document.getElementById("username-err").style.display = "block";
         } else if (pw.val().length < 8 || pw.length > 36) {
            document.getElementById("pwd-err").style.display = "block";
        } else {
            var user = JSON.stringify(username.val());
            var pass = JSON.stringify(pw.val());
            var postParameters = {username: user, password: pass};

            $.post("/login", postParameters, function(res) {
							var response = JSON.parse(res);
							if (response.length == 0) {
								document.getElementById("sub-err").style.display = "block";
							}
						}); 
        }
    });
});