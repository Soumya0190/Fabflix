let addStar_form = $("#addStar_form");
let addMovie_form = $("#addMovie_form");
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
        $("#error_message").text(resultDataJson["message"]);
    }
}
/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitAddStarForm(formSubmitEvent) {
    console.log("submit add star form");
    alert($("#starName").val());
    formSubmitEvent.preventDefault();
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "api/dashboard",
        data: addStar_form.serialize(),
        success: (resultData) => displayScreen(resultData)
    });
}

function submitAddMovieForm(formSubmitEvent) {
    console.log("submit add movie form");
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        data: addMovie_form.serialize(),
        url: "api/dashboard",
        success: (resultData) => displayScreen(resultData)
    });
}
// Bind the submit action of the form to a handler function
addStar_form.submit(submitAddStarForm);
addMovie_form.submit(submitAddMovieForm);
// Bind the submit action of the form to a handler function
//search_form.submit(submitSearchForm);

function displayScreen(resultData) {
    console.log("get DB Meta data");
    let table = $("#metadata_body");   let bodyHTML = "";
    if (resultData ==="undefined" || resultData ==="null" || resultData.length <= 0) return;
    let tabData = resultData;

    for (let i = 0; i < tabData.length; i++) {
        bodyHTML = bodyHTML + "<tr><td colspan='5'><b>" + tabData[i]["table_name"] + "</b></td></tr>";
        bodyHTML = bodyHTML + "<tr><td><b>Column Name</b></td><td><b>Type</b></td><td><b>Is Nullable?</b></td><td><b>Column Key</b></td><td><b>Extra Info</b></td></tr><tr>";
        bodyHTML = bodyHTML + "<td>" + tabData[i]["column_name"] + "</td>";
        bodyHTML = bodyHTML + "<td>" + tabData[i]["column_type"] + "</td>";
        bodyHTML = bodyHTML + "<td>" + tabData[i]["is_nullable"] + "</td>";
        bodyHTML = bodyHTML + "<td>" + tabData[i]["colkey"] + "</td>";
        bodyHTML = bodyHTML + "<td>" + tabData[i]["extra"] + "</td></tr>";
        bodyHTML = bodyHTML + "<tr><td colspan='5' height='15'></td></tr>";
    }
    if (bodyHTML.length <= 0){
        if (resultDataJson["errorMessage"]) {
            if (length(resultDataJson["errorMessage"]) >= 0) $("#error_message").text(resultDataJson["errorMessage"]);
        }
    }
    else  table.append(bodyHTML);
}


jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/dashboard",
    success: (resultData) => displayScreen(resultData)
});

