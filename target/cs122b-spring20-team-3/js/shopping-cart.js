function displayTotalAmt(price, qty){
    return price*qty;
}
function viewShoppingCart(resultData) {
    console.log("viewShoppingCart: view shopping cart: ");
    //console.log("viewShoppingCart: "+ resultData[0]["title"]+": " + resultData.length);
    let table = $("#shopping_cart_body");
    let bodyHTML = "";
    if (resultData === null || resultData === undefined || resultData["status"] ==="fail" ){
        $("#cart_error_message").text(resultData["errorMessage"]);
        $("#movie_info").hide();
        $("#cart_error_message").show();
        $("#navCheckout").hide();
    }
    else {
        let price, qty, title, totalAmt =0, id, cost = 0, rowcount =1; tmp="";
        console.log(resultData.length);
        for (let i = 0; i < resultData.length; i++) {

            id = resultData[i]["id"];
            price = resultData[i]["price"];
            qty = resultData[i]["quantity"];
            title = resultData[i]["title"];
            cost = displayTotalAmt(price, qty);
            bodyHTML += "<tr>";
            bodyHTML += "<td>" + title + "</td>";
            bodyHTML += "<td>$" + price + "</td>";
            bodyHTML += "<td><form method='GET' action='#'>";
            bodyHTML += "<input type='hidden' id = 'id" + rowcount + "' value='"+ id +"'>";
            bodyHTML += " <input type ='text' id ='movieQty"+ rowcount+"' required onchange='javascript:chkVal(this);'  value='" + qty + "' /></form></td>"
            bodyHTML += "<td><span name='spnAmt'> $" + cost + " </span></td>";
            //let tmp = updateMovie(id, qty);
            tmp = "javascript:updateMovie(" + rowcount + ",'"+ id +"','upd');";
            bodyHTML += "<td><a name ='updateLink'  class='app_button' href='"+ tmp +"' >Update</a></td>";
            tmp = "javascript:updateMovie(" + rowcount + ",'"+ id +"','del');";
            bodyHTML += "<td><a name ='deleteLink'  class='app_button' href='"+ tmp +"' >Delete</a></td>";
            //bodyHTML += "<td><input type='submit' value='Delete' class='app_button'  onclick = 'javascript:updateMovie(" + rowcount + ", this);' /></td>";
            totalAmt = totalAmt + cost;
            //alert(bodyHTML);
            bodyHTML += "</tr>";
            rowcount++;
        }
        bodyHTML += "<tr><td colspan='6'> Total Cost : $" + totalAmt + "</td></tr>";

        $("#movie_info").show();
        $("#shopping_cart_body").show();
        $("#cart_error_message").hide();
        $("#navCheckout").show();
    }
    console.log(bodyHTML);
    table.append(bodyHTML);
}
function chkVal(obj){

    var objval = obj.value;
    if (Number.isInteger(objval)){
        objval = parseInt(obj.value);
        if (objval < 0){
            alert("Please enter positive number");
        }
    } else {
        alert("Please enter positive number");
    }
}
function updateMovie(num, id, btnAction){
    try {
        //alert("updtae movie =" + num+", "+ id+","+btnAction);
        let movieid = id; //eval('$("#id' + num + '")').val();
        let qty = 0;
        if (btnAction ==="del") qty = 0;
        else {
            let qty = eval('$("#movieQty' + num + '")').val();
        }
        let data = "?movieId=" + movieid + "&qty=" + qty;
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: "api/viewcart" + data,
            success: (resultData) => viewShoppingCart(resultData)
        });
    }
    catch (ex)
    {
        alert(ex);
    }
}
/*
let resultDataJson = JSON.parse(resultDataString);

console.log("view shopping cart");
console.log(resultDataJson);
console.log(resultDataJson["status"]);

// If login succeeds, it will redirect the user to search.html
if (resultDataJson["status"] === "success")
{
    window.location.replace("shopping-cart.html");
} else {
    console.log("show error message");
    console.log(resultDataJson["message"]);
    $("#cart_error_message").text(resultDataJson["message"]);
}
 */

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/viewcart",
    success: (resultData) => viewShoppingCart(resultData)
});

