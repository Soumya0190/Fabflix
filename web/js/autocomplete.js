

function handleLookup(query, actionn, doneCallback) {
    console.log("autocomplete initiated")
    console.log("sending AJAX request to backend Java Servlet")

    // TODO: if you want to check past query results first, you can do it here

    // sending the HTTP GET request to the Java Servlet endpoint
    // with the query data
    let url = "search-suggestion?query=" + escape(query) + "&action=" + actionn;// escape the query string to avoid errors caused by special characters

    jQuery.ajax({
        "method": "GET",
        "url": url,
        "success": function (data) {
            // pass the data, query, and doneCallback function into the success handler
            handleLookupAjaxSuccess(data, doneCallback)
        },
        "error": function (errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}

function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
	
	// TODO: if you want to check past query results first, you can do it here
	
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	jQuery.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "hero-suggestion?query=" + escape(query),
		"success": function(data) {
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})

}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
<<<<<<< HEAD
 * data is the JSON data string you get from your Java Servlet
 */
function handleLookupAjaxSuccess(data, doneCallback) {
    console.log("lookup ajax successful")
    // parse the string into JSON
    var jsonData = JSON.parse(data);
    console.log(jsonData)

    // TODO: if you want to cache the result into a global variable you can do it here
  //  var moviesArray = $.map(movies, function (value, key) { return { value: value, data: key }; });
    // call the callback function provided by the autocomplete library
  //  add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: jsonData } );
}


function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion
    let id = "";
    if (suggestion["data"] != null) {
        console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["ID"]);
        id = suggestion["data"]["ID"];
    }

    let url = "single-movie.html?id=" + escape(id);
    window.location.replace(url);
}
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	console.log(jsonData)
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["heroID"])
}



$('#searchmTitle').autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query,doneCallback) {
        handleLookup(query,"searchmTitle" , doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    minChars:3,   // TODO: add other parameters, such as minimum characters
    showNoSuggestionNotice:true,
    lookupLimit:10,
    noCache:true,
    noSuggestionNotice: 'Sorry, no matching results'

});

/*
 * do normal full text search if no suggestion is selected
 */
function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    var data = "movie-list.html?searchTitle="+ $("#searchmTitle").val()+"&searchDirector="+$("#searchDirector").val();
    data = data +"&searchYear="+$("#searchYear").val();
    data = data +"&searchStar="+$("#searchStar").val();
    data = data +"&recordPerPage="+$("#recordPerPage").val();
    data = data +"&usertype="+$("#usertype").val();
    //alert(data);
    console.log("search submit: " +data);
    window.location.replace(data);
    // TODO: you should do normal search here
}

// bind pressing enter key to a handler function
$('#searchmTitle').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        handleNormalSearch($('#searchmTitle').val())
    }
})
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
}

// bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#autocomplete').val())
	}
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button


