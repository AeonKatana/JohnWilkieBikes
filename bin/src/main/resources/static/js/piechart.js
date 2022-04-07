
        $(document).ready(function() {
		var real_data = $("#data").attr("object");
	console.log(real_data);
            google.charts.load('current', {
                packages : [ 'corechart']
            });
            
            google.charts.setOnLoadCallback(drawPieChart);
             function drawPieChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Year');
            data.addColumn('number', 'Views');
            Object.keys(real_data).forEach(function(key) {
                data.addRow([ key, real_data[key] ]);
            });
            var options = {
                title : 'Blog stats'
            };
            var chart = new google.visualization.PieChart(document
                    .getElementById('container'));
            chart.draw(data, options);
  }
        });
       
       