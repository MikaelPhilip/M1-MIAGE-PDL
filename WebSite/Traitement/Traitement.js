//Fonction qui va lancer la génération des filtres et du graphiques aprés avori trouvé et charger un json
function traitement(){
	//create a listener when a file is selected: this listener launch treatement
	document.getElementById('fileLoader').addEventListener("change", function(event){
	//We launch treatement
	LoadJson(event); //param: file selected
});
}

//Function for load one of generates Json 
function LoadJson(event){
	var obj;
	//create a reader
	var reader = new FileReader();
    reader.onload = onReaderLoad; //Call an internal function when reading text
    reader.readAsText(event.target.files[0]);
	 
	//internal fonction for create an json obj with file
	function onReaderLoad(event){
        console.log(event.target.result);
        var obj = JSON.parse(event.target.result);
    }
	//Checking obj
	if(typeof obj =='object')
	{
		// It is JSON: call next treatement
		Generate();
		GenerateFilter();
	}else{
		if(obj ===false)
		{
			// the response was a string "false", parseJSON will convert it to boolean false
			alert("Le chargement du JSON à echoué");
		}else{
			// the response was something else
			alert("Erreur lors du chargement: Veuillez utiliser un fichier json valide");
		}
	}
}
function Generate(){
	// register our custom symbols to nvd3
	// make sure your path is valid given any size because size scales if the chart scales.
	nv.utils.symbolMap.set('thin-x', function(size) {
	size = Math.sqrt(size);
	return 'M' + (-size/2) + ',' + (-size/2) +
	'l' + size + ',' + size +
	'm0,' + -(size) +
	'l' + (-size) + ',' + size;
	});
	
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
		
		//Rattachement à une balise html
		d3.select('#graph svg')
		//Appel de la méthode de generation de données
		.datum(LoadData(4,40)) //Méthode temporaire
		//Generation du graph
		.call(chart);
		nv.utils.windowResize(chart.update);
		chart.dispatch.on('stateChange', function(e) { ('New State:', JSON.stringify(e)); });
		return chart;
	});
}				

//Fonction de genration des données (ici un exemple qui genere aléatoirement
function LoadData(groups, points) { //# groups,# points per group
	// smiley and thin-x are our custom symbols!
	var data = [],
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
	return data;
}

//Function for generation of all filters
function GenerateFilter(){
	//on récupére dans une var la partie filtre d enotre site
	var filter = document.getElementById('filters');
	//On va ensuite créer un element que l'on ajoutera dans cette partie
	//Variables qui contiendra tout le code html des filtres 
	var contents="";
	/*On va rajouter dans content le hmtl de chaque type de filtre (syntaxe basé sur le confiugurator.html)*/
	//Search
	//TODO condition pour saovir si on a à rajouter une barre de recherche
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
	filter.innerHTML=contents;
}