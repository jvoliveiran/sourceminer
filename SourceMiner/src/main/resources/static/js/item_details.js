function getFileContentInRevision(path) {
	var revisionNumber = $("#revisionFile").val();

	$.ajax({
		type : "POST",
		url : "/SourceMiner/dashboard/repository_item/file_content_revision",
		data : {
			'revisionNumber' : revisionNumber,
			'path' : path
		},
		success : changeFileContentRevision,
		error : function(e) {
			console.log("ERROR: ", e);
		},
		done : function(e) {
			console.log("DONE");
		}
	});
}

function changeFileContentRevision(data, status) {
	$('#fileContentBox').text(data);
}

function loadGraph(nodeName, projectId) {
	var width = 200, height = 400;
	var rest_service = "/SourceMiner/graph/findPlainJSONDependecyGraphCallFrom?"
			+ ("nodeName=" + nodeName) + ("&projectId=" + projectId);

	d3.json(rest_service).header("Content-Type", "application/json").send("get",
		function(error, graph) {
			if (error)	return;
			var nodeArray = graph.nodes;
			var linkArray = graph.links;
			console.log(graph);
			
			var force = d3.layout.force().gravity(.0)
				.distance(100)
				.charge(-100)
				.size([ width, height ])
				.nodes(graph.nodes)
				.links(graph.links).start();
				
			var svg = d3.select("#graph").append("svg")
				.attr("width", "100%")
				.attr("height", "100%")
				.attr("pointer-events", "all");
				
			// build the arrow.
			svg.append("svg:defs").selectAll("marker")
				  .data(["end"])      // Different link/path types can be defined here
				  .enter().append("svg:marker")    // This section adds in the arrows
				    .attr("id", String)
				    .attr("viewBox", "0 -5 10 10")
				    .attr("refX", 35)
				    .attr("refY", 1.5)
				    .attr("markerWidth", 6)
				    .attr("markerHeight", 6)
				    .attr("orient", "auto")
				    .append("svg:path")
				    .attr("d", "M0,-5L10,0L0,5");

				// add the links and the arrows
				var path = svg.append("svg:g").selectAll("path")
				    .data(force.links())
				    .enter().append("svg:path")
//				    .attr("class", function(d) { return "link " + d.type; })
				    .attr("class", "link")
				    .attr("marker-end", "url(#end)");

				// define the nodes
				var node = svg.selectAll(".node")
				    .data(force.nodes())
				    .enter().append("g")
				    .attr("class", "node")
				    .call(force.drag);

				node.append("circle").attr("r", "25");

				node.append("text")
					.attr("dx", 12)
					.attr("dy", ".35em").text(function(d) {return d.name});

				force.on("tick", function() {
					 path.attr("d", function(d) {
					        var dx = d.target.x - d.source.x,
					            dy = d.target.y - d.source.y,
					            dr = Math.sqrt(dx * dx + dy * dy);
					        return "M" + 
					            d.source.x + "," + 
					            d.source.y + "A" + 
					            dr + "," + dr + " 0 0,1 " + 
					            d.target.x + "," + 
					            d.target.y;});

					node.attr("transform", function(d) {return "translate(" + d.x + "," + d.y + ")";});
				});
			});
}