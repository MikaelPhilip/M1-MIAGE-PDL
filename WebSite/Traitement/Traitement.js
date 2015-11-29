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
		.showLegend(false)
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
		chart.yAxis.orient("left").ticks(15);
		chart.xAxis.orient("bottom").ticks(15);
		//call méthode for generate data and personalize axis,tooltips
		var data=LoadData(chart);
		//manage color or picture for each dot (in data, product,chart)
		personalizeDots(chart,data);
		//add chart in html
		
		
		d3.select('#graph svg')
		//add data, product on chart
		.datum(data)
		//chart generation
		.call(chart);
		//nv.utils.windowResize(chart.update);
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
	
	/*Parametize labels and tooltips*/
	//Add label for x and y 
	chart.xAxis.axisLabel(dimX+" Taille des points:"+dimSize);
	//Add label for x and y 
	chart.yAxis.axisLabel(dimY);
    //Modify tooltips
	chart.tooltip.contentGenerator(function(data){
		console.log(data);
		//Set the content of tooltip
		var text="<p><b>Nom Produit: "+data.point.label+"</b></p>"
		+"<p>"+dimX+":"+data.point.x+"</p>"
		+"<p>"+dimY+":"+data.point.y+"</p>";
		if(typeof dimSize !== 'undefined'){
			text+="<p>"+dimSize+":"+data.point.size+"</p>";
		}
		if(typeof dimColor !== 'undefined'){
			text+="<p>"+dimColor+":"+data.point.datacolor+"</p>";
		}
		return text;
	});
	
	/*Add to data each product*/
	var group=0;
	$.each(json, function(name, product) {
		if (name != "FILTERS" && name !="DIMENSIONS"){
			var col = undefined;
			//check  dimensions and get value
			var valx=0;
			var valy=0;
			var valsize=0;
			var valcolor=0;
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
					valcolor= product[dimColor];
				}
			}
			data.push({
				key: name,
				values: []
			});
			data[group].values.push({
				/*name*/
				label: name, //add an object to stock name of product
				/*values for dimensions*/
				x: valx, //set x position with value for first dimension
				y: valy, //set y position with value for second dimension
				size: valsize, //set size with value for third dimension
				datacolor: valcolor,
				/* data for personalize dots*/
				image: undefined, //TODO: Call dans le json le parametre  url images
				dimColorValue: setColor(col,dimColor) 
			});
			group++;
		}
	});
	
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

//Function for manage picture for each dot (in data, product,chart)
function personalizeDots(chart,data){
	//Recherche pour cela d3 linear color scale
	//TODO: 
	//1)Faire un groupe pour un point (dans generation) OK
	//2)Definir couleur de ce groupe en récupérant code couleur dans data[i].value[0].couleur  OK
	//3)Code couleur géneré avec méthode SetColor(value) TODO
	//4)Créer un array de taille nbPoint qui contient le code couleur et le fournir dans cette méthode OK
	
	//Change color of dots or change to picture
	//array for set color for each dot
	var colors=[]
	$.each(data,function(index,value){
		//TODO: gestion image
		//Cas image = undefined , on appelle méthode pour regler couleur suivant valeur dimColorValue sinon on change le contenue du point avec l'image
		//get color value
		colors.push(data[index].values[0].dimColorValue);
	});
	chart.color(colors);
}

//Function for define color
function setColor(valueCol,dimColor){
	var color;
	if(typeof valueCol !== 'undefined'){
		//Trouver un myen effecise de determiner un %
		var pourc= 100;
		var r=21;
		var g=169;
		if(pourc<=50){
			r+=pourc;
		}
		if(pourc>50){
			g=g-pourc;
		}
		//color="rgb("+r+","+g+",60)";
	}else{
		color="rgb(21,120,169)";
	}
	return color;
	
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
	//return(contentsImg);
	document.getElementById('pictures').innerHTML=contentsImg;
}