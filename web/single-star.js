/* Code used and changed form Professor Chen Li's project1-api-example  */

function getParameterByName(target)
{
    let url = window.location.href;
    target = target.replace(/[\[\]]/g, "\\$&");
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function getStarInfo(resultData)
{
    console.log("getStarInfo: updating star info");
    let starInfoDiv = $("#star_info");
    let info = "<h2>" + "Star Name: " + resultData["name"] + "</h2>";
    if (resultData["birth"] == null)
    {
        info += "<h3>Birth Year: N/A</h3>";
    }
    else
    {
        info += "<h3>Birth Year: " + resultData["birth"] + "</h3>";
    }
    starInfoDiv.append(info);
    let table = $("#single_star_body");
    let movieList = JSON.parse(resultData["movies"]);
    let movieBodyHTML = "";
    var j;
    for (j = 0; j < movieList.length; j+=2)
    {
        movieBodyHTML += "<tr><td>";
        movieBodyHTML += '<a href = "single-movie.html?id=' + movieList[j] + '">' + movieList[j+1] + "</a>";
        movieBodyHTML += "</td></tr>";
    }
    table.append(movieBodyHTML);
}



let starId = getParameterByName('id');
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/stars?id=" + starId,
    success: (resultData) => getStarInfo(resultData)
});
