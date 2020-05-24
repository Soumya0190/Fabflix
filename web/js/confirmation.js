function getCardInfo(resultData) {
    try {
        console.log("getCardInfo: updating card info");
        if (resultData === null || resultData === undefined || resultData["status"] ==="fail" ){
            $("#confirmationMsg").text(resultData["errorMessage"]);
            $("#order_info").hide();
            $("#cart_error_message").show();

        }
        else {

            let cardInfoDiv = $("#card_info");
            let cardInfo = "<h2>" + "Card Information" + "</h2>";
            cardInfo += "<h2>" + "Card Holder Name: " + resultData["cardFirstName"] + " " + resultData["cardLastName"] + "</h2>";
            cardInfoDiv.append(cardInfo);

            let table = $("#movie_list_body");
            let movie_list = JSON.parse(resultData["allMovies"]);
            let tableString = "";
            var i = movie_list.length;

            tableString = "";
            for (j = 0; j < i; j++) {
                tableString += "<tr>";
               /* tableString += "<td>" + resultData[j]["title"] + "</td>";
                tableString += "<td>" + resultData[j]["price"] + "</td>";
                tableString += "<td>" + resultData[j]["quantity"] + "</td>";
                tableString += "<td>" + resultData[j]["saleId"] + "</td>";
                */
                tableString += "<td>" + movie_list[j]["title"] + "</td>";
                tableString += "<td>" + movie_list[j]["price"] + "</td>";
                tableString += "<td>" + movie_list[j]["quantity"] + "</td>";
                tableString += "<td>" + movie_list[j]["saleId"] + "</td>";
                tableString += "</tr>";
            }
            //table.append(orderInfo);
            table.append(tableString);

        }
    }catch (e) {
        console.log("CAUGHT CONFIRMATION ERROR" + e);
        $("#confirmationMsg").text(resultData["errorMessage"]);
        $("#order_info").hide();
        $("#cart_error_message").show();
    }
/*
    try
    {
        console.log("handle confirmation response");
        console.log(resultData);
        console.log(resultData["status"]);

        if (resultData["status"] === "success") {
            window.location.replace("confirmation.html");
        } else {
            console.log("show error message");
            console.log(resultData["message"]);
            $("#confirmation_error_message").text(resultData["message"]);
        }
    } catch (err) {
        alert(err);
    }*/
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/confirmation",
    success: (resultData) => getCardInfo(resultData)
});




