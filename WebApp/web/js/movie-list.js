/*
Code used form Professor Chen Li's project1-api-example
Sort code used from w3 schools and modified
*/
function sortTable(n, numberOpr) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("movies_list_table");
    switching = true;
    dir = "asc";
    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
            if (dir == "asc") {
                if (numberOpr=="number" && (Number(x.textContent) > Number(y.textContent))) {
                    shouldSwitch = true; break;
                }
                if (x.textContent.toLowerCase() > y.textContent.toLowerCase()) {
                    shouldSwitch= true; break;
                }
            } else if (dir == "desc") {
                if (numberOpr=="number" && (Number(x.textContent) < Number(y.textContent))) {
                    shouldSwitch = true;
                    break;
                }
                if (x.textContent.toLowerCase() < y.textContent.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount ++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}

//$("#tdTitle").onclick=sortTable(0);
//$("#tdRating").onclick=sortTable(1);

function getMovieList(resultData,recordsPerPage, pgOffset, usertype) {
    try {

        console.log("getMovieList: updating movie list");
        // console.log("getMovieList: resultset length ="+ resultData["totalRecords"]);
        usertype = resultData["usertype"];
        if (usertype === "admin"){

            $("#empOptnavigation").show(); $("#custOptnavigation").hide();
        }
        else
        {
            $("#empOptnavigation").hide();$("#custOptnavigation").show();
        }
        let table = $("#movies_list_body");
        let srchDiv = $("#divSearchResults");

        let totalRecords = resultData["totalRecords"];
        let recordsPerPage = resultData["recordsPerPage"];
        let pgOffset = resultData["pgOffset"];
        if (totalRecords === "null" || totalRecords ==="undefined")
        {
            totalRecords = 0;
        }
        if (parseInt(totalRecords) <= 0) {

            $("#result_error_message").text("No Data Found for the given search criteria. Please try again.");
            srchDiv.hide();
            return;
        }
        if (totalRecords === "undefined" || totalRecords === "null") totalRecords = 100;
        if (recordsPerPage === "undefined" || recordsPerPage === "null") recordsPerPage = 25;
        if (pgOffset === "undefined" || pgOffset === "null") pgOffset = 0;

        let totalPg = Math.ceil(totalRecords / recordsPerPage);
        //alert("totalrecs=" + totalRecords + ", recsPerPg=" + recordsPerPage + ", pgOffset= " + pgOffset)
        let bodyHTML = "", id = "";
        table.empty();
        let movieList = JSON.parse(resultData["movieArray"]);

        for (let i = 0; i < Math.min(movieList.length, recordsPerPage); i++) {
            bodyHTML = "<tr>";
            //id = resultData[i]["movie_id"]
            bodyHTML += "<td>" + '<a href = "single-movie.html?id=' + movieList[i]["movie_id"] + '&price=' + movieList[i]["price"] + '">';
            bodyHTML += movieList[i]["movie_title"] + "</a></td>"
            bodyHTML += "<td>" + movieList[i]["movie_rating"] + "</td>"
            bodyHTML += "<td>" + movieList[i]["movie_year"] + "</td>"
            bodyHTML += "<td>" + movieList[i]["movie_director"] + "</td>"
            bodyHTML += "<td>" + movieList[i]["movie_genres"] + "</td>"
            bodyHTML += "<td>";

            let stars = JSON.parse(movieList[i]["movie_stars"]);
            var j;
            for (j = 0; j < stars.length; j += 2) {
                if (j != 0) bodyHTML += ", ";
                bodyHTML += '<a href = "single-star.html?id=' + stars[j] + '">' + stars[j + 1] + "</a>";
            }
            bodyHTML += "</td>";
            bodyHTML += "<td> $" + movieList[i]["price"] + "</td>";
            var tmp = "'" + movieList[i]["movie_id"] + "','" + movieList[i]["movie_title"] + "'," + movieList[i]["price"];
            if (usertype === "admin"){
                bodyHTML += '<td></td>';
            }
            else
            {
                bodyHTML += '<td><a href = "javascript:addToCart(' + tmp + ')"; > Add to Shopping Cart</a></td>';
            }
            bodyHTML += "</tr>";

            table.append(bodyHTML);
        }
        if (bodyHTML <= 0) {
            $("#result_error_message").text(resultData["errorMessage"]);
        } else {
            let movieStar = resultData["movieStar"];
            let movieTitle = resultData["movieTitle"];
            let movieDirector = resultData["movieDirector"];
            let movieYear = resultData["movieYear"];
            let genreid = resultData["genreid"];
            let titleStartsWith = resultData["titleStartsWith"];
            createPagination(totalPg, recordsPerPage, pgOffset, movieStar, movieTitle, movieDirector, movieYear, genreid,titleStartsWith ,usertype);
        }
    }
    catch(ex)
    {
        alert(ex);
    }
}
function createPagination(totalPg, recordsPerPage, pgOffset, movieStar, movieTitle, movieDirector, movieYear, genreid,titleStartsWith,usertype){
    let createPg =""; let currentPage=pgOffset/recordsPerPage;  let tmp ="";
    if (pgOffset >= recordsPerPage)
    {
        pgOffset = pgOffset - recordsPerPage;
        tmp = "javascript:getMovieLstFromSession(" + recordsPerPage+"," + pgOffset+",'" + movieStar +"','" + movieTitle;
        tmp = tmp +"','"+ movieDirector +"','"+ movieYear +"','"+ genreid +"','"+titleStartsWith+"','"+usertype+"');";
        createPg += '<a href="' + tmp + '">&laquo;</a>';
    }
    let lastPage= 0; let initPg = 0;
    if  (totalPg > currentPage + 10 ) initPg = currentPage;
    for (let i = initPg; i <  Math.min(totalPg,currentPage+10) ; i++)
    {
        let tmp="";
        if (i === currentPage) tmp = 'class="active"'; lastPage = i;
        pgOffset = i*recordsPerPage
        tmp = "javascript:getMovieLstFromSession(" + recordsPerPage+"," + pgOffset+",'" + movieStar +"','" + movieTitle;
        tmp = tmp +"','"+ movieDirector +"','"+ movieYear +"','"+ genreid +"','"+titleStartsWith+"','"+usertype+"');";
        createPg += '<a href="' + tmp + '">'+i+'</a>';
    }
    if (totalPg > lastPage+1) {
        pgOffset = lastPage*recordsPerPage + recordsPerPage;
        tmp = "javascript:getMovieLstFromSession(" + recordsPerPage+"," + pgOffset+",'" + movieStar +"','" + movieTitle;
        tmp = tmp +"','"+ movieDirector +"','"+ movieYear +"','"+ genreid +"','"+titleStartsWith+"','"+usertype+"');";
        createPg += '<a href="' + tmp + '">&raquo;</a>';
    }
    $("#divPg").html(createPg);
    $("#divPg").show();
}

function getMovieLstFromSession(recordsPerPage,pgOffset,movieStar, movieTitle, movieDirector, movieYear, genreid,titleStartsWith, usertype){
//movieStar, movieTitle, movieDirector, movieYear, genreid,titleStartsWith
    if (recordsPerPage == null || recordsPerPage.length <=0)  pgLimit = 25;
    let data = "movie-list.html?pagination=Y&recordsPerPage=" + recordsPerPage;
    // if (pgOffset != null) data = data+ "&pgOffset="+ pgOffset;
    if (movieTitle != "null" && movieTitle != "undefined") data = data + "&searchTitle=" + movieTitle;
    if (movieDirector != "null" && movieTitle != "undefined") data = data + "&searchDirector=" + movieDirector;
    if (movieYear != "null" && movieYear != "undefined") data = data + "&searchYear=" + movieYear;
    if (movieStar != "null" && movieStar != "undefined") data = data + "&searchStar=" + movieStar;
    if (genreid != "null" && genreid != "undefined") data = data + "&genreid=" + genreid;
    if (titleStartsWith != "null" && titleStartsWith != "undefined") data = data + "&browsetitle=" + titleStartsWith;
    if (pgOffset != "null" && pgOffset != "undefined") data = data + "&pgOffset=" + pgOffset;
    if (usertype != "null" && usertype != "undefined") data = data + "&usertype=" + usertype;
    //alert(data);
    window.location.replace(data);
    /* direct ajax call to end point is ovrwritten by on page load
       jQuery.ajax({
           dataType: "json",
           method: "GET",
           url: data,
           success: (resultData) => getMovieList(resultData)
       });*/
}

let searchTitle = getParameterByName('searchTitle');
let searchDirector = getParameterByName('searchDirector');
let searchYear = getParameterByName('searchYear');
let searchStar = getParameterByName('searchStar');
let recordsPerPage = getParameterByName('recordsPerPage');
let genreid = getParameterByName('genreid');
let browsetitle = getParameterByName('browsetitle');
let pgOffset = getParameterByName('pgOffset');
let pagination = getParameterByName('pagination');
let ftMovieTitle = getParameterByName('ftMovieTitle');
let fsMovieTitle = getParameterByName('fsMovieTitle');
if (recordsPerPage == null || recordsPerPage.length <= 0) recordsPerPage = 25;
if (pgOffset == null || pgOffset.length <= 0) pgOffset = 0;
if (pagination == null || pagination.length <= 0) pagination = "N";
let usertype = getParameterByName('usertype');

let data = "api/movies?recordsPerPage=" + recordsPerPage+"&usertype="+usertype;
if (searchTitle != null) data = data + "&searchTitle=" + searchTitle;
if (searchDirector != null) data = data + "&searchDirector=" + searchDirector;
if (searchYear != null) data = data + "&searchYear=" + searchYear;
if (searchStar != null) data = data + "&searchStar=" + searchStar;
if (genreid != null) data = data + "&genreid=" + genreid;
if (browsetitle != null) data = data + "&browsetitle=" + browsetitle;
if (pgOffset != null) data = data + "&pgOffset=" + pgOffset;
if (pagination != null) data = data + "&pagination=" + pagination;
if (usertype != null) data = data + "&usertype=" + usertype;
if (ftMovieTitle != null) data = data + "&ftMovieTitle="+ftMovieTitle;
if (fsMovieTitle != null) data = data + "&fsMovieTitle="+fsMovieTitle;

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: data,
    success: (resultData) => getMovieList(resultData, recordsPerPage, pgOffset,usertype)
});




function addToCart(movieId, movieTitle, price){
    console.log("Ajax call to shopping servelet");
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "api/addtocart?id=" + movieId + "&title=" + movieTitle + "&price=" + price,
        success: (resultData) => handleAddInCartResponse(resultData)
    });
    console.log("Ajax call done for shopping servlet");

}
//Read JSON response of shopping cart
function handleAddInCartResponse(resultDataJson){

    console.log("addInCart: "+ resultDataJson["message"]);

    if (resultDataJson["status"] === "success")
    {
        console.log(resultDataJson["message"]);
        console.log(resultDataJson["totalItems"]);

        var span = $(".close")[0];
        displayMsg(resultDataJson["message"]);

    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        // $("#login_error_message").text(resultDataJson["message"]);
    }
}

function displayMsg(addtocartMsg){
    msgDiv = document.getElementById("cartMsg");
    modal = document.getElementById("divAddToCart");
    if (msgDiv !== "undefined" && msgDiv != "null")
        msgDiv.textContent = addtocartMsg;
    if (modal !== "undefined" && modal != "null") modal.style.display = "block";
    else alert ("Some error");
}

/*var span = document.getElementsByClassName("closeCartMsg")[0];
span.onclick = function() {
   modal.style.display = "none";
}*/

function hidePopUp()
{
    modal = document.getElementById("divAddToCart");
    if (modal !== "undefined" && modal != "null") modal.style.display = "none";
}

window.onclick = function(event) {
    modal = document.getElementById("divAddToCart");
    if (modal !== "undefined" && modal != "null")
    {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}