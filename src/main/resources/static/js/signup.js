	function hasWhiteSpace(s) {
  return s.indexOf(' ') >= 0;
}

(function() {

		$("#success").hide();
    var form = $('#signup');
		var pw = $("#pwd-signup");
		var pw2 = $("#pwd2");
		var username = $('#username-signup');
		var name = $('#first');
		$("#username-taken").addClass("hide");
	
		username.keyup(function(e) {
			var split = username.val().split(" ");
			if (hasWhiteSpace(username.val())) {
				$("#username-taken").addClass("hide");
				$("#spaces").removeClass("hide");
				$("#available").addClass("hide");
			} else if (!e || e.which == 13) {
				return;
			} else if (!username.val()) {
				$("#username-taken").addClass("hide");
				$("#available").addClass("hide");
					$("#spaces").addClass("hide");
				return;
			}

			if (hasWhiteSpace(username.val())) {
				$("#username-taken").addClass("hide");
				$("#available").addClass("hide");
				$("#spaces").removeClass("hide");
			} else {
			var params = {"username" : username.val()};
			$.post("/exists", params, function(res) {
				var result = JSON.parse(res);
				if (result.isUserName === true) {
					$("#spaces").addClass("hide");
					$("#username-taken").removeClass("hide");
					$("#available").addClass("hide");
				} else {
					$("#username-taken").addClass("hide");
					$("#available").removeClass("hide");
						$("#spaces").addClass("hide");
				}
			});
		}
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


		if (username.val().length != 0  && !(pw.val().length < 8) && !(pw.val().length > 36) && (pw.val() == pw2.val()) && ($("#first").val().length != 0)&& !(hasWhiteSpace(username.val()))) {
						var name = $("#first").val();
            var postParameters = {"username" : username.val(), "password" : pw.val(), "name" : name};
            $.post("/create", postParameters, function(res) {
							var response = JSON.parse(res);
							if (jQuery.isEmptyObject(response)) {
								$("#sub-err").show();
							} else {
//								$("#success").show();
								window.location = "/home";
								$.post("/home");
							}
						}); 
        }
    });
})();