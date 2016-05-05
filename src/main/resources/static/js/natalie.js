$( document ).ready(function() {
	console.log("here lolllll");
	var chart = c3.generate({
		  bindto: '#mood',
		  data: {
		    columns: [
		      ['Angry', 60],
		      ['Whatever', 50],
		      ['Happy', 50],
		    ],
		    type: 'donut'
		  },
		  donut: {
		    title: "Dogs love:",
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
});

(function() {
	function(json) {
		var obj = JSON.parse(json);
		var username = obj.username;
		
		for (int i = 0; ) {
			obj.articles[i];
			obj.articles[i].id;
		}
		var chart = c3.generate({
			  bindto: '#mood',
			  data: {
			    columns: [
			      ['Angry', 60],
			      ['Whatever', 50],
			      ['Happy', 50],
			    ],
			    type: 'donut'
			  },
			  donut: {
			    title: "Dogs love:",
			  }
		});
	}
})();

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