$( document ).ready(function() {
	var err = $("#alert");
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
		var loadArticles = function(start, end) {
			var ul = $("#articlelist");
			postParameters = {"start" : start, "end" : end, "amount" : 0};
			$.post("/articles/time", postParameters, function(res) {
				var articles = JSON.parse(res);
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
			});
		}	
	 
				
		$.post("/getprof", function(res) {
				var response = JSON.parse(res);
				var articles = response["articles"];
				getDomain(articles);
						loadProfGraphs(response["avgReadLevel"],response["wordsRead"],response["numArticles"],response["avgMoods"]);
		});
		
		var map = {};
		var cal;
		
		jQuery.ajax({
  		type: "POST",
			url: "/articles/dates",
			success: function(res) {
				map = JSON.parse(res);
			},
			async: false
		});
		
		var monthsAgo = function(size){
				now = new Date();
				now.setMonth(now.getMonth() - size);
				return now;
		}

		var oneYearAgo = function(){
				now = new Date();
				now.setYear(now.getFullYear() - 1);
				now.setMonth(now.getMonth() + 1);
				return now;
		}

		var responsiveCal = function( options ) {
				if( $(window).width() < 1000 ) {
						options.start = monthsAgo(7);
						options.range = 7;
				} else if ( $(window).width() < 1200 ) {
					options.start = monthsAgo(10);
					options.range = 10;
				} else {
						options.start = oneYearAgo();
						options.range = 12;
				}

				if( typeof cal === "object" ) {
						$('#cal-heatmap').html('');
						cal = cal.destroy();
				}
				cal = new CalHeatMap();
				cal.init( options );
		}

		caloptions = {
				domain: 'month',
				subdomain: 'x_day',
				cellSize: 15,
				cellPadding: 2,
				itemName:["action","actions"],
				legend: [1, 5, 10, 15, 20, 25, 30],
				displayLegend: false,
				maxDate: new Date(),
				data: map,
				legendColors: {
					empty: "#ededed",
					min: "#FFB9B9",
					max: "#AB2121"
  			},
				tooltip: true,
				onClick: function (date, nb) {
					$("#articlelist").empty();
					date.setHours(00);
					date.setMinutes(00);
          var unixS = date.getTime();
					date.setHours(23);
					date.setMinutes(59);
					var unixEnd = date.getTime();
					loadArticles(unixS, unixEnd);
        }
		};

		// run first time, put in load if your scripts are in footer
		responsiveCal( caloptions );

		$(window).on("resize", function() {
				// run on resize
				responsiveCal( cal.options );
		});
}
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
	var grade;

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
		err.addClass("hide");
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
		


		jQuery.ajax({
  		type: "POST",
			url: "/add",
			data: postParameters,
			success: function(res) {
				var response = JSON.parse(res);
					if (!jQuery.isEmptyObject(response)) {
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
					grade = article["gradelevel"];
					loadGraphs();
					 $('#article').animate({
  					scrollTop: $('#article').get(0).scrollHeight +10000});
					}
			}, 
			error : function (xhr, ajaxOptions, thrownError) {
				err.removeClass("hide");
			}
		});
		

		/*$.post("/add", postParameters, function(res) {
					var response = JSON.parse(res);
					if (!jQuery.isEmptyObject(response)) {
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
					}
				
				}, false); 
*/
	} else {
		err.removeClass("hide");

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
	      min: 0,
	      max: 100
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
		$("#article").removeClass("hide")
		$("#bar").removeClass("hide");		$("#title").text(title);
		$("#word-count").text("Word Count: " + wordcount);
		$("#pages").text("Pages: " + pages);
		$("#topic").text("Topic: " + topic);
		$("#title").attr("href", link);
		$("#grade").text("Grade: " + grade);

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
		$("#pos").removeClass("hide");
		$("#neg").removeClass("hide");
		for (var z = 0; z < progressBar.length; z++) {
			var width = "style='width: " + Math.abs(progressBar[z]) +"%'";
			var per = Math.abs(progressBar[z]) + " percent";
			console.log(width);
			if (progressBar[z] >0) {
				var div = '<div'+ ' data-trigger="hover" data-container="body" data-toggle="popover" data-placement="bottom" title="Positive" data-content="Positive" class="progress-bar progress-bar-success progress-bar-striped active pop"' + width +'> <span class="sr-only">' + per +'</span> </div>';
				bar.append(div);
			} else {
				var div = '<div'+ ' data-trigger="hover" data-container="body" data-toggle="popover" data-placement="bottom" title="Negative" data-content="Negative" class="progress-bar progress-bar-danger pop"' + width +'> <span class="sr-only">' + per +'</span> </div>'
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
	      min: 0,
	      max: 100
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
	$("#cloud").empty();
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


    $('.progressBar').hover(function(e) {
    	console.log( $('.progressBar').text());


    });

    $(function () {
  $('[data-toggle="popover"]').popover()
});

    function getDomain(ar) {
    	console.log(ar);
	var domains = [];
	var domainHashNeg= {};
	var domainHashPos= {};
	var domainHashNeu ={};
	for (var i = 0; i <ar.length; i++){
			var a = ar[i];
			var url = a["url"];
			var dom = extractDomain(url);
			console.log(dom + a["rank"]);
		
			if (a["rank"]>0){
				if (domainHashPos[dom] == null) {
					domainHashPos[dom] = 1;
				} else {

					var num = domainHashPos[dom];
					domainHashPos[dom] = num + 1;
				}
			} else if (a["rank"] == 0){
				if (domainHashNeu[dom] == null) {
					domainHashNeu[dom] = 1;
				} else {
					var num = domainHashNeu[dom];
					domainHashNeu[dom] = num + 1;
				}
			} else {
				if (domainHashNeg[dom] == null) {
					domainHashNeg[dom] = 1;
				} else {
					var num = domainHashNeg[dom];
					domainHashNeg[dom] = num + 1;
				}
			}
				
		}
		var y =0;

		for (var a in domainHashPos) {
			console.log(a);
			domains[y] = {text: a, weight: domainHashPos[a]};
			y++;

		}
		console.log(domains);
		//$("#cloud").empty();
	$("#cloud2").jQCloud(domains, {
		  width: $("#cloud2").width(),
		  height: 50,
		  shape: 'rectangular',
		  autoResize: true
	});

	function extractDomain(url) {
    var domain;
    //find & remove protocol (http, ftp, etc.) and get domain
    if (url.indexOf("://") > -1) {
        domain = url.split('/')[2];
    }
    else {
        domain = url.split('/')[0];
    }

    //find & remove port number
    domain = domain.split(':')[0];

    return domain;
}



}

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