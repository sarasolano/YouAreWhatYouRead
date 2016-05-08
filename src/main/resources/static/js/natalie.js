$( document ).ready(function() {
	var err = $("#alert");
	err.hide();
	var pathArray = window.location.pathname.split( '/' );

	if (pathArray[1] == "article") {
		postParameters = { 
			id : pathArray[2]
		};


		$.post("/article/a", postParameters, function(res) {
			var response = JSON.parse(res);
			article = response;
					moods = article["moods"];
					sentiments = [article["sentiment"]];
					readlevel = article["readlevel"];
					wordcount = article["word-count"];
					pages = article["pages"];
					title = article["title"];
					link = article["url"];
					topic = article["topic"];
					words = JSON.parse(article["wordCloud"]);
					loadGraphs();

			   //window.location = "/profile";
			
	});

	}

	if (window.location.pathname == "/profile") {

	 postParameters = {
				
			};
				
	$.post("/getprof", postParameters, function(res) {
			var response = JSON.parse(res);
			var articles = response["articles"];
			var ul = $("#articlelist");
			loadProfGraphs(response["avgReadLevel"],response["wordsRead"],response["numArticles"],response["avgMoods"]);
				
			for (var i = 0; i <articles.length; i++){
				var a = articles[i];
				var link = a["link"];
					var title = a["title"];
					if (a["rank"]>0){
						ul.prepend("<li class='list-group-item up'>" + "<span class='date'>"+a["addedDate"] + "</span>" + '<a href =' + '"' +link + '">' + title + "</a>" + "</li>" );
					} else if (a["rank"] == 0){
						ul.prepend("<li class='list-group-item neutral'>" + "<span class='date'>"+a["addedDate"] + "</span>" + '<a href =' + '"' +link + '">' + title + "</a>" + "</li>" );

					} else {
						ul.prepend("<li class='list-group-item down'>" + "<span class='date'>"+a["addedDate"] + "</span>" + '<a href =' + '"' +link + '">' + title + "</a>" + "</li>" );
					}
					
					
				
			}

			   //window.location = "/profile";
			
	}); 
}
	$("#article").hide();
	var plus = false;
	var minus = false;
	var article;
	var moods;
	var sentiments;
	var readlevel;
	var title;
	var wordcount;
	var pages;
	var link;
	var topic;
	var words;

	if (window.location.pathname == "/home") {
	$("#plus").click(function(e) {
			plus = true;
			sendUrl();
		});

	$("#minus").click(function(e) {
			minus = true;
			sendUrl();
		});
	$("#add-article").click(function(e) {
			sendUrl();
		});
}

	function sendUrl() {
		err.hide();
		var url = $("#url-input").val();
		if (url.length != 0) {
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
				rank : JSON.stringify(-1)
			};
		} else {
			postParameters = {
				url : url,
				rank : JSON.stringify(0)
			};
		}
		
		$.post("/add", postParameters, function(res) {
					var response = JSON.parse(res);
					article = response["article"];
					moods = article["moods"];
					sentiments = [article["sentiment"]];
					readlevel = article["readlevel"];
					wordcount = article["word-count"];
					pages = article["pages"];
					title = article["title"];
					link = article["url"];
					topic = article["topic"];
					words = JSON.parse(article["wordCloud"]);
					loadGraphs();
					 $('#article').animate({
  					scrollTop: $('#article').get(0).scrollHeight +10000});
				
				}); 
	} else {
		err.show();

	}


		
	}

	function loadProfGraphs(rl,tw,na,am) {

		var chart3 = c3.generate({
		bindto: '#avg-rl',
	    data: {
	        columns: [
	            ['Reading Level', rl]
	        ],
	        type: 'gauge'
	    },
	    gauge: {
	        label: {
	            format: function(value, ratio) {
	                return value;
	            },
	            show: true
	        },
	      min: 3,
	      max: 7
	    }
	});
		$("#totalwords").text("You have read " + tw + " total words" );
		$("#numArticles").text("You have read " + na + " articles");


		var chart = c3.generate({
		  bindto: '#avgMoods',
		  data: {
		        json: [am],
		        keys: {
		            value: ['trust', 'surprise', 'joy', 'anticipation', 'sadness', 'disgust', 'anger', 'fear']
		        },
		        type: 'donut'
		   },
		  donut: {
		    title: "Mood",
		  }
	});

		
	}

	function loadGraphs(){
		$("#article").show();
		$("#title").text(title);
		$("#word-count").text("Word Count: " + wordcount);
		$("#pages").text("Pages: " + pages);
		$("#topic").text("Topic: " + topic);
		$("#title").attr("href", link);

		console.log(sentiments);
		var sen = sentiments[0];
		var currsent = sen[0];
		var percent = currsent/sen.length *100;
		var y =0;
		var progressBar = [];
		var total =0;
		//total = percent;
		var i;
		for(i =1; i < sen.length; i++) {
			if (sen[i] == currsent) {
				//total += Math.abs(100* currsent/sen.length);
				percent = percent + (100* currsent/sen.length);
			} else {
				progressBar[y] = percent;
				currsent = sen[i];
				total += Math.abs(percent);
				percent = 100 *currsent/sen.length;
				y++;
				
			}

		}

		console.log(total);
		console.log(100 - total -1);
		progressBar[y] = 100 - total -.001;
		console.log(percent + (100* currsent/sen.length)-.1);
		console.log(y);
		console.log(progressBar.length);

		var bar = $("#bar");
		bar.empty();
		for (var z = 0; z < progressBar.length; z++) {
			var width = "style='width: " + Math.abs(progressBar[z]) +"%'";
			console.log(width);
			if (progressBar[z] >0) {
				var div = '<div class="progress-bar progress-bar-success progress-bar-striped active"' + width +'> <span class="sr-only">35% Complete (success)</span> </div>';
				bar.append(div);
			} else {
				var div = '<div class="progress-bar progress-bar-danger"' + width +'> <span class="sr-only">35% Complete (success)</span> </div>';
				bar.append(div);
			} 
			
		}

		console.log(progressBar);

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
	/*
	
	var chart2 = c3.generate({
		bindto: '#sentiment',
	    data: {
	        columns: sentiments,
	        type: 'area-step'
	    },

	    axis: {
        	y: {
            	max: 2,
            	min: -2,
            // Range includes padding, set 0 if no padding needed
            // padding: {top:0, bottom:0}
        	}
    	}
	});
*/
	
	var chart3 = c3.generate({
		bindto: '#level',
	    data: {
	        columns: [
	            ['Reading Level', readlevel]
	        ],
	        type: 'gauge'
	    },
	    gauge: {
	        label: {
	            format: function(value, ratio) {
	                return value;
	            },
	            show: true
	        },
	      min: 3,
	      max: 7
	    }
	});
	
	/*var words = [
	             {text: "Lorem", weight: 13},
	             {text: "Ipsum", weight: 10.5},
	             {text: "Dolor", weight: 9.4},
	             {text: "Sit", weight: 8},
	             {text: "Amet", weight: 6.2},
	             {text: "Consectetur", weight: 5},
	             {text: "Adipiscing", weight: 5}
	          
	           ];
	           */

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
				
		$.post("/logout", function() {
			window.location = "/signin";
		}); 
    });

    $('#profile').click(function(e) {
  	    e.preventDefault();
  	    window.location = "/profile";
  	 

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