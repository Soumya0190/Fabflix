function getMovieInfo(resultData, movieId, price) {
    console.log("getMovieInfo: updating movie info");

    let movieInfoDiv = $("#movie_info");
    let info = "<h2>" + "Movie Name: " + resultData["title"] + "</h2>";
    info += "<h3>Year: " + resultData["year"] + "</h3>";
    info += "<h3>Director: " + resultData["director"] + "</h3>";
    info += "<h3>Rating: " + resultData["rating"] + "</h3>";

    var tmp = "'" + movieId + "','" + resultData["title"] + "',"+ price;
    info += '<h3><a href = "javascript:addToCart('+ tmp + ')"; class="app_button"> Add to Shopping Cart</a></h3>';
    movieInfoDiv.append(info);

    let starTable = $("#movie_star_body");

    let starList = JSON.parse(resultData["stars"]);

    let starBodyHTML = "";
    var j;
    for (j = 0; j < starList.length; j+=2)
    {
        starBodyHTML += "<tr><td>";
        starBodyHTML += '<a href = "single-star.html?id=' + starList[j] + '">' + starList[j+1] + "</a>";
        starBodyHTML += "</td></tr>";
    }
    starTable.append(starBodyHTML);


    let genreTable = $("#movie_genre_body");

    let genreList = JSON.parse(resultData["genres"]);

    let genreBodyHTML = "";
    var k;
    for (k = 0; k < genreList.length; ++k)
    {
        genreBodyHTML += "<tr><td>";
        genreBodyHTML += genreList[k];
        genreBodyHTML += "</td></tr>";
    }
    genreTable.append(genreBodyHTML);
}

let movieId = getParameterByName('id');
let price = getParameterByName('price');

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/single-movie?id=" + movieId,
    success: (resultData) => getMovieInfo(resultData, movieId, price)
});



//var span = document.getElementsByClassName("closeCartMsg")[0];


function displayMsg(addtocartMsg){
    modal = document.getElementById("divAddToCart");
    msg = document.getElementById("cartMsg");
    msg.textContent = addtocartMsg;
    modal.style.display = "block";
}
/*
span.onclick = function() {
    modal.style.display = "none";
}
*/


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
function hidePopUp()
{
    modal = document.getElementById("divAddToCart");
    if (modal != "undefined" && modal != "null") modal.style.display = "none";
}

window.onclick = function(event) {
    modal = document.getElementById("divAddToCart");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}