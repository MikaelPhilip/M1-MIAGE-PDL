//Création d'une variable qui représente notre graphque
function generate() {
	//On indique ici la zone ou on fera apparaitre le graphique (id du div)
    $('#graphique').highcharts({
		//Type
        chart: {
            type: 'bar' 
        },
		//Titre
        title: {
            text: 'Consommation de fruit'
        },
		//Axe x
        xAxis: {
            categories: ['Pommes', 'Bananes', 'Oranges']
        },
		//Axe y
        yAxis: {
            title: {
                text: 'Nombre de fruits mangés'
            }
        },
        //Data
        series: [{
            name: 'Jane',
            data: [1, 0, 4]
        }, {
            name: 'John',
            data: [5, 7, 3]
        }]
    });
}