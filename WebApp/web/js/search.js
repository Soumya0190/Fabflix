let search_form = $("#search_form");
let browse_genre = $("#browse_genre");
let search_movie_form = $("#search_movie_form");
let searchfsButton= $("#searchfsButton");
let usertype=$("#usertype");

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
    formSubmitEvent.preventDefault();
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
    data = data +"&usertype="+$("#usertype").val();
    console.log("search submit: " +data);
    window.location.replace("movie-list.html"+data);
}

// Display Genre
function getGenreList(resultData, usertype) {
    console.log("getGenreList: get genre list from database");
    var divText = "";
    var genList = JSON.parse(resultData["genre"]);//JSON.parse
    for (let i = 0; i < genList.length; i++) {
        divText = divText + " <a href='movie-list.html?pagination=N&usertype="+usertype+"&genreid=" + genList[i]["genreID"] + "'>" + genList[i]["name"] + " </a> &nbsp;&nbsp; ";
    }
    if (divText.length <= 0){
        if (resultDataJson["errorMessage"]) {
            if (length(resultDataJson["errorMessage"]) >= 0) $("#search_error_message").text(resultDataJson["errorMessage"]);
        }
    }
    else  $("#divGenreList").html(divText);
}

function printAlphanumericList(usertype) {
    var divText = "";
    divText = getAlphanumericList(48,58, divText, usertype);
    divText = getAlphanumericList(65,91, divText, usertype);
    divText = divText + " <a href='movie-list.html?browsetitle=spchr&pagination=N&usertype="+usertype+"'>*</a> ";

    $("#divTitleList").html(divText);
}
function getAlphanumericList(i, j, divText, usertype){
    for(k = i; k < j; k++) {
        var str = String.fromCharCode(k);
        divText = divText + " <a href='movie-list.html?browsetitle=" + str + "'&pagination=N&usertype="+usertype+"' >" + str + " </a> &nbsp;&nbsp; ";
    }
    return divText;
}
function displaySearchScreen (resultData) {

    usertype = resultData["usertype"];
    printAlphanumericList(usertype);

    if (resultData["usertype"]=="admin")
    {
        $("#empOptnavigation").show(); $("#custOptnavigation").hide();
    }
    else
    {
        $("#empOptnavigation").hide();$("#custOptnavigation").show();
    }
    getGenreList(resultData, usertype);
}

function submitmovieSearchForm(formSubmitEvent) {
    formSubmitEvent.preventDefault();
    console.log("submit movie search form");
    let ftTitle = $("#searchmTitle").val() != null || $("#searchmTitle").val() == undefined ?$("#searchmTitle").val():"";
    let fsTitle = $("#searchfsTitle").val() != null || $("#searchfsTitle").val() == undefined ?$("#searchfsTitle").val():"";
    var data = "?ftMovieTitle="+ ftTitle;
    var data = "?fsMovieTitle="+ fsTitle;

    data = data +"&recordPerPage=25";
    data = data +"&usertype="+$("#usertype").val();
    console.log("search submit: " +data);
    // alert(data);
    window.location.replace("movie-list.html"+data);
}

function displayAdvancedSrch(flag) {
    console.log("display advanced search");
    // if ($("#divAdvSearch"))
    //    $("#divAdvSearch").show();
    if (flag === "Y") {
        $("#divAdvSearch").hide(); $("#divBasicSearch").show();
        //document.getElementById("divAdvSearch").style.visibility = "hidden";

    }
    else {
        $("#divAdvSearch").show(); $("#divBasicSearch").hide();
        //document.getElementById("divAdvSearch").style.display = "block";
        // document.getElementById("divAdvSearch").style.visibility = "visible";
    }
}

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);
search_movie_form.submit(submitmovieSearchForm);
searchfsButton.submit(submitmovieSearchForm);
$("#moviesResult").hide();

//printAlphanumericList();
//($("#searchTitle")).click(submitSearchForm);
displayAdvancedSrch("Y");
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/search",
    success: (resultData) => displaySearchScreen(resultData)
});