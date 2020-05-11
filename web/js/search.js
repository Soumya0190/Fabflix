let search_form = $("#search_form");
let browse_genre = $("#browse_genre");


function handleLoginResult(resultDataString)
{
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to search.html
    if (resultDataJson["status"] === "success") {
        //window.location.replace("search.html");
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#search_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
        //formSubmitEvent.preventDefault();
        //window.location.replace("search.html");
    var data = "?searchTitle="+ $("#searchTitle").val()+"&searchDirector="+$("#searchDirector").val();
    data = data +"&searchYear="+$("#searchYear").val();
    data = data +"&searchStar="+$("#searchStar").val();
    data = data +"&recordPerPage="+$("#recordPerPage").val();
    console.log("search submit: " +data);
    window.location.replace("movie-list.html"+data)
}

// Display Genre
function getGenreList(resultData) {
    console.log("getGenreList: get genre list from database");
    var divText = "";
    var genList = JSON.parse(resultData["genre"]);//JSON.parse
    for (let i = 0; i < genList.length; i++) {
        divText = divText + " <a href='movie-list.html?pagination=N&genreid=" + genList[i]["genreID"] + "'>" + genList[i]["name"] + " </a> &nbsp;&nbsp; ";
    }
    if (divText.length <= 0){
        if (resultDataJson["errorMessage"]) {
            if (length(resultDataJson["errorMessage"]) >= 0) $("#search_error_message").text(resultDataJson["errorMessage"]);
        }
    }
    else  $("#divGenreList").html(divText);
}

function printAlphanumericList() {
    var divText = "";
    divText = getAlphanumericList(48,58, divText);
    divText = getAlphanumericList(65,91, divText);
    divText = divText + " <a href='movie-list.html?browsetitle=spchr&pagination=N'>*</a> ";

    $("#divTitleList").html(divText);
}
function getAlphanumericList(i, j, divText){
    for(k = i; k < j; k++) {
        var str = String.fromCharCode(k);
        divText = divText + " <a href='movie-list.html?browsetitle=" + str + "'&pagination='N' >" + str + " </a> &nbsp;&nbsp; ";
    }
    return divText;
}
function displaySearchScreen (resultData) {

    printAlphanumericList();
    if (resultData["usertype"]=="admin")
    {
        $("#empOptnavigation").show(); $("#custOptnavigation").hide();
    }
    else
    {
        $("#empOptnavigation").hide();$("#custOptnavigation").show();
    }
    getGenreList(resultData);
}
// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);

$("#moviesResult").hide();
//printAlphanumericList();
//($("#searchTitle")).click(submitSearchForm);

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/search",
    success: (resultData) => displaySearchScreen(resultData)
});