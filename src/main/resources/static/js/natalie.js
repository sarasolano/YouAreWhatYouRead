$( document ).ready(function() {
	var plus = false;
	var minus = false;
	var article;
	var moods;
	var sentiments;
	$("#plus").click(function(e) {
			if (plus) {
				plus = false;
				$("#plus").removeClass("selected");
			} else {
				$("#plus").focus();
				plus = true;
				minus = false;
				$("#plus").addClass("selected");
				$("#minus").removeClass("selected");
			}
		});

	$("#minus").click(function(e) {
			if (minus) {
				minus = false;
				$("#minus").removeClass("selected");
			} else {
				minus = true;
				plus = false;
				$("#plus").removeClass("selected");
				$("#minus").addClass("selected");
			}
		});
	$("#add-article").click(function(e) {
			sendUrl();
		});

	function sendUrl() {
		var url = $("#url-input").val();
		var rank;
		var postParameters;
		if (plus) {
			postParameters = {
				url : url,
				rank : JSON.stringify(1)
			};

		} else if (minus) {
			postParameters = {
				url : url,
				rank : JSON.stringify(0)
			};
		} else {
			postParameters = {
				url : url,
				rank : JSON.stringify(null)
			};
		}
		console.log(postParameters);
		
		$.post("/add", postParameters, function(res) {
						console.log(res);
					var response = JSON.parse(res);
					article = response["article"];
					console.log(article);
					moods = article["moods"];
					console.log(moods["object"]);
					loadGraphs();
				
				}); 


		
	}

	function loadGraphs(){
		console.log(moods);
		var chart = c3.generate({
		  bindto: '#mood',
		  data: {
		        json: moods,
		        keys: {
		            value: ['trust', 'surprise', 'joy', 'anticipation', 'sadness', 'disgust', 'anger', 'fear']
		        },
		        type: 'donut'
		   },
		  donut: {
		    title: "Mood",
		  }
	});
	
	var sentiments = [[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1]];;
	
	var chart2 = c3.generate({
		bindto: '#sentiment',
	    data: {
	        columns: sentiments,
	        type: 'area'
	    }
	});
	
	var words = [
	             {text: "Lorem", weight: 13},
	             {text: "Ipsum", weight: 10.5},
	             {text: "Dolor", weight: 9.4},
	             {text: "Sit", weight: 8},
	             {text: "Amet", weight: 6.2},
	             {text: "Consectetur", weight: 5},
	             {text: "Adipiscing", weight: 5}
	             /* ... */
	           ];

	$("#cloud").jQCloud(words, {
		  width: $("#cloud").width(),
		  height: $("#row2").height(),
		  shape: 'rectangular',
		  autoResize: true
	});

	}



});

	
//		function(json) {
//			console.log("herel lol");
//			var obj = JSON.parse(json);
//			var username = obj.username;
//			
//			for (int i = 0; ) {
//				obj.articles[i];
//				obj.articles[i].id;
//			}
//			var chart = c3.generate({
//				  bindto: '#mood',
//				  data: {
//				    columns: [
//				      ['Angry', 60],
//				      ['Whatever', 50],
//				      ['Happy', 50],
//				    ],
//				    type: 'donut'
//				  },
//				  donut: {
//				    title: "Dogs love:",
//				  }
//			});
//		}
//});

  $('#logout').click(function(e) {
  	e.preventDefault();
  	console.log("jdsafjsdaklflj");
				
		$.post("/logout", function() {
			window.location = "/signin";
		}); 
    });

//var words = [{
//  text: "Lorem",
//  weight: 13
//}, {
//  text: "Ipsum",
//  weight: 10.5
//}, {
//  text: "Dolor",
//  weight: 9.4
//}, {
//  text: "Sit",
//  weight: 8
//}, {
//  text: "Amet",
//  weight: 6.2
//}, {
//  text: "Consectetur",
//  weight: 5
//}, {
//  text: "Adipiscing",
//  weight: 5
//}];
//
//$('#cloud').jQCloud(words);
//
//var chart = c3.generate({
//  bindto: '#chart1',
//  data: {
//    columns: [
//      ['data1', 300, 350, 300, 0, 0, 0],
//      ['data2', 130, 100, 140, 200, 150, 50]
//    ],
//    types: {
//      data1: 'area',
//      data2: 'area-spline'
//    },
//    colors: {
//      data1: 'green',
//      data2: 'pink'
//    }
//  }
//});
//
//var chart = c3.generate({
//  bindto: '#chart2',
//  data: {
//    columns: [
//      ['Angry', ],
//      ['Whatever', 50],
//      ['Happy', 50],
//    ],
//    type: 'donut'
//  },
//  donut: {
//    title: "Dogs love:",
//  }
//});
//
//var chart = c3.generate({
//  bindto: '#chart3',
//  data: {
//    columns: [
//      ['data1', 30],
//      ['data2', 50]
//    ],
//    type: 'pie'
//  },
//  pie: {
//    label: {
//      format: function(value, ratio, id) {
//        return d3.format('$')(value);
//      }
//    }
//  }
//});
//
//var chart = c3.generate({
//  bindto: '#chart4',
//  data: {
//    columns: [
//      ['data1', 30],
//      ['data2', 50]
//    ],
//    type: 'pie',
//    colors: {
//      data1: 'hotpink',
//      data2: 'pink'
//    }
//  },
//  pie: {
//    label: {
//      format: function(value, ratio, id) {
//        return d3.format('$')(value);
//      }
//    }
//  }
//});