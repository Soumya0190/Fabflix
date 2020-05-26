/*
* CS 122B Project 4. Autocomplete Example.
*
* This Javascript code uses this library: https://github.com/devbridge/jQuery-Autocomplete

*/

let cachedItems =[];

function findItem(query, cachedItems) {
    if (cachedItems != "undefined" && cachedItems != undefined) {
        //{data:data, query:query};
        for (let i = 0; i < cachedItems.length; i++) {
            if (cachedItems[i] != "undefined" && cachedItems[i] != undefined) {
                if (cachedItems[i]["query"] === query) {
                    return cachedItems[i]["data"];
                }
            }

        }
        return "";
    }
}


function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated for query ="+query);
    let makeAJaxCall = true;
    if (typeof(Storage) !== "undefined") {
        // var existingEntries = JSON.parse(localStorage.getItem("allEntries"));

        let cachedItems = JSON.parse(localStorage.getItem("cachedQry"));
        if(cachedItems == null) cachedItems = [];
        let cacheddata = findItem(query, cachedItems);
        if (cacheddata != "undefined" && cacheddata != undefined) {
            if (cacheddata.length > 0) {
                console.log("Get query results from local storage for query=" + query);
                handleLookupAjaxSuccess(cacheddata, query, doneCallback);
                makeAJaxCall = false;
            }
        }
        if (makeAJaxCall) {
            // sending the HTTP GET request to the Java Servlet endpoint  // with the query data
            console.log("Get query results from api/database for query=" + query);
            console.log("sending AJAX request to backend Java Servlet")
            jQuery.ajax({
                "method": "GET",
                // generate the request url from the query.
                // escape the query string to avoid errors caused by special characters
                "url": "search-suggestion?query=" + escape(query),
                "success": function (data) {
                    // pass the data, query, and doneCallback function into the success handler
                    handleLookupAjaxSuccess(data, query, doneCallback)
                },
                "error": function (errorData) {
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
        let obj = {data:data, query:query};
        //if (cachedItems.length < 10) {

        //  localStorage.setItem("data", data);
        //  localStorage.setItem("query", query);
        let existingEntries = JSON.parse(localStorage.getItem("cachedQry"));
        if(existingEntries == null) existingEntries = [];
        localStorage.setItem("obj", JSON.stringify(obj));
        // Save allEntries back to local storage
        existingEntries.push(obj);
        localStorage.setItem("cachedQry", JSON.stringify(existingEntries));

        //   }
    }

    doneCallback( { suggestions: jsonData } );
}

function parseQuery(suggestion, query, queryLowerCase){
    if (queryLowerCase != "undefined" && queryLowerCase != undefined) {
        var res = queryLowerCase.split(" ");
        if (res != null && res != "undefined" && res != undefined) {
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