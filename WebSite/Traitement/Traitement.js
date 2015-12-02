var json;
//Function for launch LoadJson and chart generation
function traitement(){

	/* functionality for allow user to choose json DISABLED: json file is load with search a local specific file
	//create a listener when a file is selected: this listener launch treatement
	document.getElementById('fileLoader').addEventListener("change", function(event){
	//We launch treatement
	LoadJson(event); //param: file selected
	});*/
	//Cache is disabled to avoid keep old json data
	$(document).ready(function() {
		$.ajaxSetup({ cache: false });
	});
	LoadJson();
}

//Function for load one of generates Json 
function LoadJson(){
	/* functionality for allow user to choose json DISABLED: json file is load with search a local specific file
	//create a reader
	var reader = new FileReader();
    reader.onload = onReaderLoad; //Call an internal function when reading text
    reader.readAsText(event.target.files[0]);
	 
	//internal fonction for create an json obj with file and launch treatment if it's ok
	function onReaderLoad(event){
		try{
			console.log(event.target.result);
			json = JSON.parse(event.target.result);
		}catch(e)
		{
			alert("Erreur lors du chargement: Veuillez utiliser un fichier json valide");
		}
		GenerateFilter(json);
		Generate(json);	
    }
	*/
	//Serch a specific json file in local repository
	//if parse fail an error message appear
	//if parse succes we call function for generate filters and chart
	$.get("../../json/generation.json").success(function(file){	
		json = file;
		GenerateFilter(json); //generate filters
		Generate(json);	 //generate chart
	})
	.fail(function(jqXHR, status, error){
       alert("Erreur lors du chargement: Veuillez utiliser un fichier json valide.Message: "+ error);
    });

}
//Fonction generate chart (dimension)
function Generate(json){
	//Initialisation and set charts parameters
	var chart; //Var Chart
	nv.addGraph(function() {
		chart = nv.models.scatterChart() //type of chart
		.showLegend(false) //hide legend
		.showDistX(true) //show X axis
		.showDistY(true) //show Y axis
		.useVoronoi(true) 
		.duration(300) ;
		
		chart.xAxis.tickFormat(d3.format('.02f')); //set tick format x
		chart.yAxis.tickFormat(d3.format('.02f')); //set tick format y
		chart.yAxis.orient("left").ticks(15); //set number values and position x axis
		chart.xAxis.orient("bottom").ticks(15); //set number values and position y axis
		
		var data=LoadData(chart); //call function to create an array of data
		personalizeDots(chart,data); //manage color for each future dot
		
		//add chart in html
		d3.select('#graph svg')
		//add data, product on chart (generate dot)
		.datum(data)
		//chart generation
		.call(chart);
		nv.utils.windowResize(chart.update);
		return chart;
	});
}				

//Fonction for generate data array for the dots with our json data
function LoadData(chart) { 
	//array for our dot data
	var data=[];
	//parameters which reprents our dimension
	var dimX; //first dimension
	var dimY; //second dimension
	var dimSize; //third dimension
	var dimColor; //fourth dimension
	
	/*Get names of dimensions*/
	//Get our dimension: search in json "DIMENSIONS" object (name= name of feature, value= dimension number)
	if(typeof json["DIMENSIONS"] !== 'undefined'){
		$.each(json["DIMENSIONS"], function(name, value) {
			if(value==1){
				dimX=name;
			}else if(value==2){
				dimY=name;
			}else if(value==3){
				dimSize=name;
			}else if(value==4){
				dimColor=name;
			}
		});
	}
	//If we don't have at least two dimensions there are an error in JSON data: end of exection
	if ((typeof dimX === 'undefined')||(typeof dimY === 'undefined')){
		alert("Erreur le json ne défini pas deux premieres dimensions");
		return;
	}	
	
	/*Parametize labels and tooltips (make this only now because we need to know dimensions)*/
	//Add label for x and label for size
	chart.xAxis.axisLabel(dimX+" Paramêtre de la taille des points: "+dimSize + " Paramêtre de la couleur des points: "+ dimColor );
	//Add label for y 
	chart.yAxis.axisLabel(dimY);
    //Modify tooltips
	chart.tooltip.contentGenerator(function(data){
		var text="";
		//Set the content of tooltip
		//If we have picture
		if(typeof data.point.pictures !== 'undefined' && data.point.pictures !=""){
			text+="<div style='text-align: center'><img src='http://"+data.point.pictures+"' class ='img-circle'></div>";
		}
		text+="<p><b>"+data.point.label+"</b></p>"
		+"<p>"+dimX+":"+data.point.x+"</p>"
		+"<p>"+dimY+":"+data.point.y+"</p>";
		//If we have third dimension
		if(typeof dimSize !== 'undefined'){
			text+="<p>"+dimSize+":"+data.point.size+"</p>";
		}
		//If we have fourth dimension
		if(typeof dimColor !== 'undefined'){
			text+="<p>"+dimColor+":"+data.point.datacolor+"</p>";
		}
		return text;
	});
	
	/*For 4th dimension we must determine min and max value (for calculate rgb values for colors later)*/
	var maxColor;
	var minColor;
	//If 4th dimension exist
	if (typeof dimColor !== 'undefined'){
		//for each product we get his list of features
		$.each(json, function(i, product){
			if (i != "FILTERS" && i !="DIMENSIONS"){
				//we get value for feature == name (feature for 4th dimension)
				var value = parseFloat(product[dimColor],10);
			    //check if it's first time we search min value
				if(typeof minColor === 'undefined'){
					minColor=value;
				}
				//check if it's first time we search max value
				if(typeof maxColor === 'undefined'){
					maxColor=value;
				}
				//check if his value < min
				if(value < minColor){
					minColor=value;
				}
				//check if his value > max
				if(value > maxColor){
					maxColor=value;
				}
			}
		});
	}
	
	/*Add to data each product*/
	var group=0;
	$.each(json, function(name, product) {
		if (name != "FILTERS" && name !="DIMENSIONS"){
			//check  dimensions and get value (sometime few products/objets don't have value for one of this dimensions)
			var valx=0;
			var valy=0;
			var valsize=0;
			var valcolor= "inconnue";
			if(typeof product[dimX] !==undefined && product[dimX]!=""){
				valx=parseFloat(product[dimX],10);
			}
			if(typeof product[dimY] !==undefined && product[dimY]!=""){
				valy=parseFloat(product[dimY],10);
			}
			if(typeof dimSize !== undefined){
				if(typeof product[dimSize] !==undefined && product[dimSize]!="" ){
					valsize=parseFloat(product[dimSize],10);
				}
			}
			if(typeof dimColor !== undefined){
				if(typeof product[dimColor] !==undefined && product[dimColor]!="" ){
					valcolor= parseFloat(product[dimColor],10);
				}
			}
			//create group 
			//Here: one group=one dot.Because,nvd3 set color for each group only 
			//and we can have an unique value for 4th dimension (who set color) for each dot
			data.push({
				key: name,
				values: []
			});
			//add dot in group
			data[group].values.push({
				/*name*/
				label: name, //name of product for tooltip
				/*values for dimensions*/
				x: valx, //set x position with value for first dimension
				y: valy, //set y position with value for second dimension
				size: valsize, //set size with value for third dimension
				datacolor: valcolor, //for set color and for tooltip
				/* data for personalize dots*/
				pictures: product["_IMG_"], //url of picture for tooltip
				dimColorValue: setColor(dimColor,valcolor,maxColor,minColor) //Call function to set a rgb value for color
			});
			group++;
		}
	});
	return data;
}

//Function for generate filters: generate html code and add this in index.html
function GenerateFilter(json){
	//We get in our html files, the id of filter's parts
	var filter = document.getElementById('filters');
	
	//Set variable which contains generated html code 
	var contents="";
	//Variables for subparts of generated html code
	var contentsCheckbox="<div class='page-header'>"
	+"<p class='list-group-item-heading'>Caractéristiques</p>"
	+"</div>";
	var contentsString="";
	var contentsNumber="";
	//for each element in json,check his type and add content in filter part
	$.each(json["FILTERS"], function(name, type) {
		/*Type boolean: one checkbox*/
		if (type=="BooleanValue"){
			//Add checkbox
			contentsCheckbox+="<p class='list-group-item-text' >"
             +"<div class='checkbox checkbox-primary'>"
             +"<label><input type='checkbox' ' name='chbx"+name+"' value='chbx"+name+"'>"+name+"</label>"
             +"</div>";
		/*Type number: slider*/
		}else if (type=="IntegerValue" || type=="RealValue"){
			/*We create slider: we must determine max and min values*/
			//Search the max and min value for this filter
			var min;
			var max;
			//For each product we get his list of features
			$.each(json, function(i, product) {
				if (i != "FILTERS" && i !="DIMENSIONS"){
					var value = parseFloat(product[name],10);
					//We search value for feature == name (feature for this filter)
					//Check if it's first time we search min value
					if(typeof min === 'undefined'){
						min=value;
					}
					//Check if it's first time we search max value
					if(typeof max === 'undefined'){
						max=value;
					}
					//Check if his value < min
					if(value < min){
						min=value;
					}
					//Check if his value > max
					if(value > max){
						max=value;
					}
				}
			});
			//Determine value change when moving sliders
			var datasliderstep=(max-min)/50;
			//Add slider
			contentsNumber+="<div class='page-header'><p class='list-group-item-heading'>"+name+"</p></div><b>"+min+"</b><input id='inter"+name+"' type='text' class='span2' data-value="+min+","+max+" value="+min+","+max+" data-slider-min='"+min+"' data-slider-max='"+max+"' data-slider-step='"+datasliderstep+"' data-slider-value='["+min+","+max+"]'/> <b>"+max+"</b>";
		/*Type string: list of checkbox (one by unique string value)*/
		}else if (type=="StringValue"){
			//Create header of this list
			contentsString+="<div class='page-header'>"
			+"<p class='list-group-item-heading'>"+name+"</p>"
			+"</div>";
			//Serch each string value and make a list of check box
			//List of value
			var ArrValue = [];
			$.each(json, function(i, product) {
				if (i != "FILTERS" && i !="DIMENSIONS"){			
					ArrValue.push(product[name]);		
				}
			});
			
			//Delete duplicate value on list 
			var ArrValueUn = ArrValue.filter(function(elem, pos) {return ArrValue.indexOf(elem) == pos;}); 
			
			//Now we complete html with each element of the list
			$.each(ArrValueUn, function(i,value) {
			//Add checkbox
			contentsString+="<p class='list-group-item-text' >"
					+"<div class='checkbox checkbox-primary'>"
					+"<label><input type='checkbox' name='chbx"+name+value+"' value='chbx"+name+value+"'>"+value+"</label>"
					+"</div>";
			});
		}
	}); 
	//Add subparts
	contents=contentsCheckbox+contentsNumber+contentsString;
	//Add generate html code to index.html
	filter.innerHTML=contents;
	//Create slider with JQuery
	$(".span2").slider({});
}

//Function for personalize each dot
function personalizeDots(chart,data){
	//array for set color for each dot
	var colors=[]
	$.each(data,function(index,value){
		//add to array: rgb color determine in dimColorValue
		colors.push(data[index].values[0].dimColorValue);
	});
	//add in chart.color list of color for each group of dot (for each dot here)
	chart.color(colors);
}

//Function for define color
function setColor(dimColor,valueCol,max,min){
	var color;
	//check if we have a 4th dimension and and a value for this dimension for this objet
	if(typeof dimColor !== 'undefined' && valueCol != "inconnue"){
		//Determine pourcentage 
		var pourc=parseFloat(((valueCol-min)/(max-min))*100,2);
		//Default value for red and green parameters
		var r=80;
		var g=180;
		//If pourcentage <50% color change green to yellow : we just modifiy red value for this
		if (pourc<50){
			r+=pourc*2;
		//If pourcentage >50% color change yellow to red: we add 100 for red parameter and soustring some value for green parameter
		}else{
			r+=100;
			g=g-(180*(((pourc-50)*2)/100)); //example: 180*((5*2)/100) (for 55%)
		}
		color="rgb("+r+","+g+",40)";
	}else{
		color="rgb(21,120,169)";
	}
	return color;
	
}