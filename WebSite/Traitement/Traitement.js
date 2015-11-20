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
	/*// register our custom symbols to nvd3
	// make sure your path is valid given any size because size scales if the chart scales.
	nv.utils.symbolMap.set('thin-x', function(size) {
	size = Math.sqrt(size);
	return 'M' + (-size/2) + ',' + (-size/2) +
	'l' + size + ',' + size +
	'm0,' + -(size) +
	'l' + (-size) + ',' + size;
	});*/
	
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
		.datum(LoadData()) //Méthode temporaire
		//chart generation
		.call(chart);
		nv.utils.windowResize(chart.update);
		chart.dispatch.on('stateChange', function(e) { ('New State:', JSON.stringify(e)); });
		return chart;
	});
}				

//Fonction de genration des données (ici un exemple qui genere aléatoirement
function LoadData() { //# groups,# points per group
	// recupérerer les dimensions
	//1ere et 2nd dimension et 3eme  et 4 éme dimension on sauvegarde le nom (servira pour récuperer valeur x et y,pour detemriner la taille, et les groupes de couleurs)
	//si pas de 3 éme ou 4 éme on a y= undefined
	
	// créer les groupe (couleurs) pour la 4éme dimensions
	//4éme dimension : determiner min et max: faire 3 valeur (<1/4 max ,<1/2 max,<3/4 max)
	//si pas de 4 éme dimension on fait qu'un goupe
	//si 4éme dimension:On crée 4 groupe pour la 4 éme dimensions
	
	//TODO: Pour chaque produit: 
	//si on a plusieurs groupe : on récupére la valeur de la 4 éme dimension on la conpore pour saovir dans quel groupe de couleur mettre le point
	//sinon on le rajoute dans le seul groupe
	//aprés on indique les coordonnées x et y suivant les valeurs de la 1er et 2 nd dimension
	//On determine la taille suivant la vlauer de la 3éme dimensin
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
	/*Create group of color for fourth dimension*/
	//If 4th dimension exist
	if (typeof dimColor !== 'undefined'){
		//Define groups (for fourth dimension (color)): each groupe has got one color
		//Determine max for dimColor feature
		var max;
		//for each product we get his list of features
		$.each(json, function(i, product) {
			if (i != "FILTERS" && i !="DIMENSIONS"){
				var value = parseFloat(product[dimColor],10);
				//we search value for feature == name (feature for filter)
				//check if it's first time we search max value
				if(typeof max === 'undefined'){
					max=value;
				}
				//check if his value > max
				if(value > max){
					max=value;
				}
			}
		});
		//Determine limit for each group/color
		dimColorLow= max/4;
		dimColorMed= max/2;
		dimColorHigh= max*3/4;
		//Create four group in data
		for (i = 0; i < 4; i++) {
			data.push({
				key: 'Group ' + i,
				values: []
			});
		}
	}else{
	//No 4th dimension: just one color,one group
	data.push({
		key: 'Group1',
			values: []
		});
	}
	
	/*Add to data each product*/
	$.each(json, function(name, product) {
		if (name != "FILTERS" && name !="DIMENSIONS"){
			//If dimension 4 exist compare value to know the group where the dot will add
			/*data[i].values.push({
			x: random(),
			y: random(),
			size: Math.round(Math.random() * 100) / 100,
			shape: shapes[j % shapes.length]
			});*/
		}
	}
	return data;
	
	
	//smiley and thin-x are our custom symbols!
	/*var data = [],
	shapes = ['thin-x', 'circle', 'cross', 'triangle-up', 'triangle-down', 'diamond', 'square'],
	random = d3.random.normal();
	for (i = 0; i < groups; i++) {
		data.push({
			key: 'Group ' + i,
			values: []
		});
		for (j = 0; j < points; j++) {
			data[i].values.push({
			x: random(),
			y: random(),
			size: Math.round(Math.random() * 100) / 100,
			shape: shapes[j % shapes.length]
			});
		}
	}
	return data;*/
}

function browseJson(){
	console.log(json);
	// parcourir le premier tableau Json pour recuperer les cle et les objets B associe
	for(var value in json){
	//parcourir l objet B pour recupere les cle et les valeur qui sont dans B
	json2  = json[value];
	// Pour chaque gRand objet on affiche les petits objets cle/valeur	    
	for(var key in json2){
		if(value != "FILTERS"){
            console.log(value + " "+key +" -> "+ json2[key]);
        }
    }
    }
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
             +"<label><input type='checkbox' name=chbx"+name+" value='chbx"+name+"'>"+name+"</label>"
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
			contentsNumber+="<p>"+name+".Type: "+type+",min: "+min+",max: "+max+"</p>";
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
					+"<label><input type='checkbox' name=chbx"+name+""+value+" value='chbx"+name+""+value+"'>"+value+"</label>"
					+"</div>";
			});
		}
	}); 
	//Ajout du contenu html crée
	contents=contentsCheckbox+contentsNumber+contentsString;
	filter.innerHTML=contents;
	
	/*On va rajouter dans content le hmtl de chaque type de filtre (syntaxe basé sur le confiugurator.html)
	//Search
	//TODO condition pour savoir si on a à rajouter une barre de recherche
	contents+="<div class='page-header'>"
			  +"<div class='least-content'>Recherche</div>"
			  +"</div>"
			  +"<div class='row-content'>"
              +"<p class='list-group-item-text'>Product: <input class='form-control floating-label' type='text' placeholder='Find' ng-model='productFilter'></p>"
			  +"<button class='btn btn-primary' type='button'>Rechercher</button>"
			  +"</div>"
			  +"<div class='list-group-separator'></div>"
	//Checkbox
	//TODO for dans le json pour voir si on des filtres de type checkbox
	contents+="<div class='page-header'>"
			 +"<p class='list-group-item-heading'>Caractéristiques</p>"
			 +"</div>"
             +"<p class='list-group-item-text' >"
             +"<div class='checkbox checkbox-primary'>"
             +"<label><input type='checkbox' name='critere1' value='1'> Caractéristiques</label>"
             +"</div>"
			 
	//Barre d'intervalle
	//TODO for dans le json pour voir si on des filtres de type barre d'intervalle
	
	//TODO ajouter d'autres types de filtres
	//Ajout du contenu html crée
	filter.innerHTML=contents; */
}