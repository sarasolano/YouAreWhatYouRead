function sendUrl() {
	var postParameters = {
		newTopLat : JSON.stringify(newTopLat),
	};
}

  $('#logout').click(function(e) {
  	e.preventDefault();
  	console.log("jdsafjsdaklflj");
				
		$.post("/logout"); 
    });