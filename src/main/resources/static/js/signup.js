(function() {
		$("#success").hide();
    var form = $('#signup');
		var pw = $("#pwd-signup");
		var pw2 = $("#pwd2");
		var username = $('#username-signup');
		var name = $('#first');
	
		username.keyup(function(e) {
			if (!e || e.which == 13) {
				return;
			} else if (username.val()) {
				username.css("border", "grey solid 1px");
				return;
			}
			var params = {"username" : username.val()};
			$.post("/exists", params, function(res) {
				var result = JSON.parse(res);
				if (result.isUserName === true) {
					username.css("border", "red solid 1px");
				} else {
					username.css("border", "green solid 1px");
				}
			});
		});
	
		$(document).keypress(function(e) {
			if (e.which == 13) {
				$('.btn').trigger("click");
			}
		});
	
		username.click(function(e) {
			$("#success").hide();
			$("#username-err").hide();
			username.removeClass("err");
		});

		$("#first").click(function(e) {
			$("#success").hide();
			$("#first").removeClass("err");
			$("#name-err").hide();
		});
		
		pw.click(function(e) {
			$("#success").hide();
			$("#pwd-err").hide();
			pw.removeClass("err");
		});
	
		pw2.click(function(e) {
			$("#success").hide();
			$("#pwd2-err").hide();
			pw2.removeClass("err");
		});
		
    $('#submit').on('click', function(e) {
				$("#username-err").hide();
				$("#name-err").hide();
				$("#pwd-err").hide();
				$("#sub-err").hide();
				$("#pwd2-err").hide();
			
        if (username.val().length == 0) {
					$("#username-err").show();
					username.addClass("err");;
				}
        if (pw.val().length < 8 || pw.val().length > 36) {
					$("#pwd-err").show();
					pw.addClass("err");;
        } 
        if (pw.val() != pw2.val()) {
					$("#pwd2-err").show();
					pw2.addClass("err");
				}
		if ($("#first").val().length == 0) {
				$("#name-err").show();
				$("#first").addClass("err");
		} 
		if (username.val().length != 0  && !(pw.val().length < 8) && !(pw.val().length > 36) && (pw.val() == pw2.val()) && ($("#first").val().length != 0)) {
						var name = $("#first").val();
            var postParameters = {"username" : username.val(), "password" : pw.val(), "name" : name};
            $.post("/create", postParameters, function(res) {
							var response = JSON.parse(res);
							if (jQuery.isEmptyObject(response)) {
								$("#sub-err").show();
							} else {
								$("#first").val('');
								pw.val('');
								pw2.val('');
								username.val('');
								$("#success").show();
								window.location = "/home";
								$.post("/home");
							}
						}); 
        }
    });
})();