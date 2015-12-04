# OpenCompare - Procuct Chart
PDL Project - M1 Miage

OpenCompare.org aims to help a community of users to import,edit, view, and use comparison matrices,typically for selecting
products in a given field.

##Objectives

This Software Development project ( PDL) has for objectives:
- To understand the existing project (requirements, specificity of the technologies and the field of application, the architecture,...).
- To write a specifications.
- To contribute very concretely to the project both at the code level(case of tests, additions of features, refactoring..)of the documentation, or the implementation of the continuous integration.
- To validate in a continue way the requirements and the implementation with the customer.
- To Generate a graph and a set of filter from a table of matrices.
- To Visualize the graph on a web page.
- To Allow the user to interact with the graph with filters and the possibility To add dimensions.

##Results

The Open project and Compare "charts product" is intended to generate, from a Comparison Matrix, an interactive display(visualization) called "product chart". That is Conceive a module which will allow to transform a table of matrix into an interactive graph.
The Treatment will be done by reusing Java API for handling matrices.Un "product chart" consists of:
- An placement of products on an axis in 2 dimensions with abscissas X and orderly Y. X and Y corresponding in characteristics of products (price, size, weight, performance, security,...);
- A panel of configuration / filtering which dynamically is able to update the placement of products (and to eliminate the products which do not correspond any more to the wishes of the user).


##License

 Apache 2
 

##Development tools

###Languages:

- JAVA for treatment.
- HTML / JS / CSS for display.

###Library:

JAVA

 Open Compare API: library of open pcm compared to manipulate files.
- JUNIT 4: Unit Testing Framework for the Java language.
- Log4js: To save the results in a journal.
- JSON: To manipulate JSON files.

JS:

- ANGULAR JS: open-source JavaScript Framework, which modifies and creates a page html dynamically monitors and detects changes on a JavaScript object.
- JSON (Javascript Object Notation) : is a lightweight data-interchange format (syntax for storing and exchanging data).
- Bootstrap: collection of tools useful for the design of sites and Web applications.
- D3:library for displaying a graph.

Tools:

- For the part conception of the project,  we will use UML as language ofModelling and PowerAMC as software to generate the models and Diagrams.
- For the part JAVA, we use IDE Eclipse  and Maven2 to the structure project.
- For the Web Part will be tested locally with UwAmp (a Wamp package type,combining several technologies that simplifies the creation of a Web serverWindows).


##Project Architecture 

PART JAVA:
  Purpose: Treatment of matrices and sending data to the website
	->A package: data recovery and treatment of the matrix
		- Retrieve matrices.
		- Verify the integrity of the data and the filters.
	-> A package: generation of data for web pages and sending data
		- Transform the data and put it into a JSON file.
		- Transforming the filters and put them in a JSON file.
		- Manage dimensions for chart and put information in a JSON file.

PART JS:
  Purpose: Reception graphics and visualization of data sent / Management of interactions
	-> Folder Basic Structure (html + css + js)
		- Html file (skeleton of the page and page launched by the Web server).
		- Css file that contains some formatting of our objects.
		- Js file which assures the interaction (generic methods on each type filters).
		- File js treatments(methods called by by the file js interaction). 
	-> File Handling
		- Js file that will contain all methods to create a chart and filters(modification / fill html file)
	-> Folder library
		- Sets scripts and files of the various libraries used.

##Instructions for deployment

1)Create a folder and put the GenerateChart.jar file (bin/GenerateChart.jar).

2)Put in root of this folder pcms,testParameters and WebSite folders (If you want, you can delete examples) and create folder json. 

3)To test applications with one of your data: Add one file "data.pcm" (created by use openCompare) in folder pcm and create "JSONParameters.json" where you write your dimension choice (see examples for syntax).

4)Launch serveur (UWamp,EasyPhp,..) and set his root in Website folder.

5)Go to the main page of web site (index.html) : you can see generated filters and chart.

6)Change contents of data.pcm and JSONParameters.json to create new chart. 

If you import project in IDE (like Eclipse), just don't forget to launch server and set his root in Website folder. 

##Execution of the Demonstration

List of pcm examples, to use them put theirs content in data.pcm and theirs JSONParameters in JSONParameters.json and launch jar (or run project in IDE):

-->Classic Examples (4 dimensions): Camera.pcm, Commerce.pcm, FootPerformance.pcm
-->Example with pictures for only few objects: CameraWithImg.pcm
-->Example with pictures for each objects: FootImage.pcm
-->Example with 3 dimensions: Gender.pcm
-->Example with 2 dimensions: VenteDisque.pcm
-->Example with incomplete data (some case with no values for features): Voiture.pcm
-->Example with lot of data: PaysDetteExterieure.pcm




