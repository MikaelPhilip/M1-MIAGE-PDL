var json;
//Fonction qui va lancer la génération des filtres et du graphiques aprés avori trouvé et charger un json
function traitement(){
	/* functionality for allow user to choose json
	//create a listener when a file is selected: this listener launch treatement
	document.getElementById('fileLoader').addEventListener("change", function(event){
	//We launch treatement
	LoadJson(event); //param: file selected
	});*/
	//Neutralisation du cache pour éviter de conserver des vielles données de json
	$(document).ready(function() {
		$.ajaxSetup({ cache: false });
	});
	LoadJson();
}

//Function for load one of generates Json 
function LoadJson(){
	/* functionality for allow user to choose json
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
	//Serch a specific json file in file,if parse fail an error message appear
	$.get("../../json/generation.json").success(function(file){	
		json = file;
		GenerateFilter(json);
		Generate(json);	
		//browseJson(json);
	})
	.fail(function(jqXHR, status, error){
       alert("Erreur lors du chargement: Veuillez utiliser un fichier json valide.Message: "+ error);
    });

}
//Fonction generate chart (dimension)
function Generate(json){

	// create the chart
	var chart; //Var Chart
	nv.addGraph(function() {
		//Initialisation
		chart = nv.models.scatterChart()
		.showDistX(true)
		.showDistY(true)
		.useVoronoi(true)
		.color(d3.scale.category10().range())
		.duration(300)
		;
		chart.dispatch.on('renderEnd', function(){
		console.log('render complete');
		});
		chart.xAxis.tickFormat(d3.format('.02f'));
		chart.yAxis.tickFormat(d3.format('.02f'));
		
		//add chart in html
		d3.select('#graph svg')
		//add data, product on chart
		.datum(LoadData(chart)) //Méthode temporaire
		//chart generation
		.call(chart);
		nv.utils.windowResize(chart.update);
		chart.dispatch.on('stateChange', function(e) { ('New State:', JSON.stringify(e)); });
		return chart;
	});
}				

//Fonction de genration des données (ici un exemple qui genere aléatoirement
function LoadData(chart) { 
	//array for our dot on chart 
	var data=[];
	//params reprents our dimension
	var dimX; //first dimension
	var dimY; //second dimension
	var dimSize; //third dimension
	var dimColor; //fourth dimension
	
	var dimColorLow; //max value for first color
	var dimColorMed; //max value for second color
	var dimColorHigh; //max value for third color
	
	/*Get names of dimensions*/
	//Get our dimension (name= name of feature, value= dimension number)
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
	if ((typeof dimX === 'undefined')||(typeof dimY === 'undefined')){
		alert("Erreur le json ne défini pas deux premieres dimensions");
	}	
	//Add label for x and y 
	chart.xAxis.axisLabel(dimX+" Taille des points:"+dimSize);
	//Add label for x and y 
	chart.yAxis.axisLabel(dimY);
    //Modify tooltips
	chart.tooltip.contentGenerator(function(data){
		//Set the content of tooltip
		var text="Nom Produit: "+data.point.label+"\n"+dimX+":"+data.point.x+"\n"+dimY+":"+data.point.y+"\n"+dimSize+":"+data.point.size
		return text;
	});
	
	
	/*Create group of color for fourth dimension*/
	//If 4th dimension exist
	if (typeof dimColor !== 'undefined'){
		//Define groups (for fourth dimension (color)): each groupe has got one color
		//Determine max for dimColor feature
		var max;
		var min;
		//for each product we get his list of features
		$.each(json, function(i, product) {
			if (i != "FILTERS" && i !="DIMENSIONS"){
				var value = parseFloat(product[dimColor],10);
				//we search value for feature == name (feature for filter)
			    //check if it's first time we search min value
				if(typeof min === 'undefined'){
					min=value;
				}
				//check if it's first time we search max value
				if(typeof max === 'undefined'){
					max=value;
				}
				//check if his value < min
				if(value < min){
					min=value;
				}
				//check if his value > max
				if(value > max){
					max=value;
				}
			}
		});
		//calcul difference
		var ecart= max-min;
		//Determine limit for each group/color
		dimColorLow= min+(ecart/4);
		dimColorMed= min+(ecart/2);
		dimColorHigh= min+(ecart*3/4);
		//Create four group in data
		for (i = 0; i < 4; i++) {
			//var to show value of each group
			var limit;
			if (i==0){
				limit="<"+dimColorLow;
			}else if (i==1){
				limit="<"+dimColorMed;
			}else if (i==2){
				limit="<"+dimColorHigh;
			}else if (i==3){
				limit=">"+dimColorHigh;
			}
			data.push({
				key: dimColor + limit,
				values: []
			});
		}
	}else{
	//No 4th dimension: just one color,one group
	data.push({
		key: 'Produit',
			values: []
		});
	}
	console.log(data);
	
	/*Add to data each product*/
	$.each(json, function(name, product) {
		if (name != "FILTERS" && name !="DIMENSIONS"){
			//If dimension 4 exist compare value to know the group where the dot will add
			var group=0;
			if (typeof dimColor !== 'undefined'){
				var val= parseFloat(product[dimColor],10);
				if(val>dimColorLow && val<dimColorMed){
					group=1;
				}else if(val>dimColorMed && val<dimColorHigh){
					group=2;
				}else if(val>dimColorHigh){
					group=3;
				}
			}
			data[group].values.push({
				x: parseFloat(product[dimX],10), //set x position with value for first dimension
				y: parseFloat(product[dimY],10), //set y position with value for second dimension
				size: parseFloat(product[dimSize],10), //set size with value for third dimension
				label: name //add an object to sotck name of product
			});
		}
	});
	console.log(data);
	return data;
}

//Function for generation of all filters
function GenerateFilter(json){
	//On récupére dans une var la partie filtre de notre site
	var filter = document.getElementById('filters');
	//On va ensuite créer un element que l'on ajoutera dans cette partie
	//Variables qui contiendra tout le code html des filtres 
	var contents="";
	var contentsCheckbox="<div class='page-header'>"
	+"<p class='list-group-item-heading'>Caractéristiques</p>"
	+"</div>";
	var contentsString="";
	var contentsNumber="";
	//Test print object filters
	//console.log(json["FILTERS"]);
	//for each element in json,check his type and add content in filter part
	$.each(json["FILTERS"], function(name, type) {
		if (type=="BooleanValue"){
			contentsCheckbox+="<p class='list-group-item-text' >"
             +"<div class='checkbox checkbox-primary'>"
             +"<label><input type='checkbox' ' name='chbx"+name+"' value='chbx"+name+"'>"+name+"</label>"
             +"</div>";
		}else if (type=="IntegerValue" || type=="RealValue"){
			//Search the max and min value for this filter
			var min;
			var max;
			//for each product we get his list of features
			$.each(json, function(i, product) {
				if (i != "FILTERS" && i !="DIMENSIONS"){
					var value = parseFloat(product[name],10);
					//we search value for feature == name (feature for filter)
					//check if it's first time we search min value
					if(typeof min === 'undefined'){
						min=value;
					}
					//check if it's first time we search max value
					if(typeof max === 'undefined'){
						max=value;
					}
					//check if his value < min
					if(value < min){
						min=value;
					}
					//check if his value > max
					if(value > max){
						max=value;
					}
				}
			});
			//determine value change when moving object
			var datasliderstep=(max-min)/50;
			contentsNumber+="<div class='page-header'><p class='list-group-item-heading'>"+name+"</p></div><b>"+min+"</b><input id='inter"+name+"' type='text' class='span2' data-value="+min+","+max+" value="+min+","+max+" data-slider-min='"+min+"' data-slider-max='"+max+"' data-slider-step='"+datasliderstep+"' data-slider-value='["+min+","+max+"]'/> <b>"+max+"</b>";
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
			
			//Delete duplicate value 
			var ArrValueUn = ArrValue.filter(function(elem, pos) {return ArrValue.indexOf(elem) == pos;}); 
			$.each(ArrValueUn, function(i,value) {
			//Now we complete html with the list
			contentsString+="<p class='list-group-item-text' >"
					+"<div class='checkbox checkbox-primary'>"
					+"<label><input type='checkbox' name='chbx"+name+""+value+"' value='chbx"+name+""+value+"'>"+value+"</label>"
					+"</div>";
			});
		}
	}); 
	//Ajout du contenu html crée
	contents=contentsCheckbox+contentsNumber+contentsString;
	filter.innerHTML=contents;
	//Création de l'objet Slider With JQuery
	$(".span2").slider({});
}

//Function that display the product's image
function DisplayImg(urlImg){
	//Préconditions : 
	//Existance d'une URL
	//L'URL est valide
	// ********************
	//On va ensuite créer un element que l'on ajoutera dans cette partie
	//Variables qui contiendra tout le code html de l'image
	var contentsImg="<div>			<img src = '";
	contentsImg+=urlImg;
	//contentsImg+="http://www.nobelcar.fr/public/img/big/lamborghini-aventador-9-1024x680.jpg" 
	contentsImg+="'class = 'img-circle'><div>";
	return(contentsImg);
}