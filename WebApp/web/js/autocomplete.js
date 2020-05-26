/*
* CS 122B Project 4. Autocomplete Example.
*
* This Javascript code uses this library: https://github.com/devbridge/jQuery-Autocomplete

*/


/*
* This function is called by the library when it needs to lookup a query.
*
* The parameter query is the query string.
* The doneCallback is a callback function provided by the library, after you get the
*   suggestion list from AJAX, you need to call this function to let the library know.
*/
function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated for query ="+query);

    if (typeof(Storage) !== "undefined") {
        // Store
        if (query === localStorage.getItem("query")) {
            var data = localStorage.getItem("data");
            console.log("Get query results from local storage for query="+ query);
            handleLookupAjaxSuccess(data, query, doneCallback)
        } else
        {
            localStorage.setItem("query", query);
            // sending the HTTP GET request to the Java Servlet endpoint  // with the query data
            console.log("Get query results from api/database for query="+ query);
            console.log("sending AJAX request to backend Java Servlet")
            jQuery.ajax({
                "method": "GET",
                // generate the request url from the query.
                // escape the query string to avoid errors caused by special characters
                "url": "search-suggestion?query=" + escape(query),
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
    }

}


/*
* This function is used to handle the ajax success callback function.
* It is called by our own code upon the success of the AJAX request
*
* data is the JSON data string you get from your Java Servlet
*
*/
function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful")

    // parse the string into JSON
    var jsonData = JSON.parse(data);
    console.log(jsonData)

    //Cache in windows storage
    if (typeof(Storage) != "undefined") {
        localStorage.setItem("data", data);
        localStorage.setItem("query", query);
    }
    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: jsonData } );
}

function parseQuery(suggestion, query, queryLowerCase){
    if (queryLowerCase != "undefined") {
        var res = queryLowerCase.split(" ");
        if (res != null && res != "undefined") {
            if (res.length === 2) {
                return suggestion.value.toLowerCase().indexOf(res[0]) || suggestion.value.toLowerCase().indexOf(res[1]);
            } else if (res.length > 2){
                var temp ="";
                for (i=0; i<res.length; i++){
                    if (i===0)  temp = suggestion.value.toLowerCase().indexOf(res[i]);
                    else temp = temp + " || " + suggestion.value.toLowerCase().indexOf(res[i]);
                }
                return temp;
            }

        }
    }
    return suggestion.value.toLowerCase().indexOf(queryLowerCase)
}

/*
* This function is the select suggestion handler function.
* When a suggestion is selected, this function is called by the library.
*
* You can redirect to the page you want using the suggestion data.
*/
function handleSelectSuggestion(suggestion) {

    let id = "";
    if (suggestion["data"] != null) {
        console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["ID"]);
        id = suggestion["data"]["ID"];
    }

    let url = "single-movie.html?id=" + escape(id);
    window.location.replace(url);
}



/*
* This statement binds the autocomplete library with the input box element and
*   sets necessary parameters of the library.
*
* The library documentation can be find here:
*   https://github.com/devbridge/jQuery-Autocomplete
*   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
*
*/
// $('#searchmTitle') is to find element by the ID "autocomplete"
$('#searchmTitle').autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    lookupFilter:function (suggestion, query, queryLowerCase) {
        parseQuery(suggestion, query, queryLowerCase)
    },
    deferRequestBy: 300,
    minChars:3,   // TODO: add other parameters, such as minimum characters
    showNoSuggestionNotice:true,
    lookupLimit:10,
    noCache:true,
    noSuggestionNotice: 'Sorry, no matching results'
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});


/*
* do normal full text search if no suggestion is selected
*/
function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here
    console.log("doing normal search with query: " + query);
    var data = "movie-list.html?searchTitle="+ $("#searchmTitle").val()+"&searchDirector="+$("#searchDirector").val();
    data = data +"&searchYear="+$("#searchYear").val();
    data = data +"&searchStar="+$("#searchStar").val();
    data = data +"&recordPerPage="+$("#recordPerPage").val();
    data = data +"&usertype="+$("#usertype").val();
    //alert(data);
    console.log("search submit: " +data);
    window.location.replace(data);

}

// bind pressing enter key to a handler function
$('#searchmTitle').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        handleNormalSearch($('#searchmTitle').val())
    }
})