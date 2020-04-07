function getMovieList(resultData) {
    console.log("getMovieList: updating movie list");

    let table = $("#movies_list_body");

    for (let i = 0; i < Math.min(20, resultData.length); i++)
    {
        let bodyHTML = "<tr>";
        bodyHTML += "<td>" + '<a href = "single-movie.html?id=' + resultData[i]["movie_id"] + '">';
        bodyHTML += resultData[i]["movie_title"] +"</a></td>"
        bodyHTML += "<td>" + resultData[i]["movie_year"] +"</td>"
        bodyHTML += "<td>" + resultData[i]["movie_director"] +"</td>"
        bodyHTML += "<td>" + resultData[i]["movie_rating"] +"</td>"
        bodyHTML += "<td>" + resultData[i]["movie_genres"] +"</td>"
        bodyHTML += "<td>";

        let stars = JSON.parse(resultData[i]["movie_stars"]);
        var j;
        for (j = 0; j < stars.length; j+=2)
        {
            if (j != 0)
            {
                bodyHTML += ", ";
            }

            bodyHTML += '<a href = "single-star.html?id=' + stars[j] + '">' + stars[j+1] + "</a>";
        }
        bodyHTML += "</td>";
        bodyHTML += "</tr>";
        table.append(bodyHTML);
    }
}


jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/movies",
    success: (resultData) => getMovieList(resultData)
});
