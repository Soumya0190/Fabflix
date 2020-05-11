let addStar_form = $("#addStar_form");
let addMovie_form = $("#addMovie_form");
let pageLoaded = false;
function handleDBupdateResult(resultData, actionData)
{
    console.log("get DB Meta data");

    // alert("status =" +resultData["status"] +", message =" + resultData["message"]);
    let statusMsg = resultData["message"];
    if ((statusMsg=== null || statusMsg ==="undefined" || statusMsg === "null") || (statusMsg != null && statusMsg.length <=0))
    {
        if (actionData === "addstar") statusMsg ="Error occurred while adding star.";
        else statusMsg = "Error occurred while adding movie.";
    }
    displayMsg(statusMsg);
    pageLoaded=true;
    $("#starName").val("");
    $("#starDOB").val("");

    $("#movieTitle").val("");
    $("#movieYear").val("");
    $("#director").val("");
    $("#movieStar").val("");
    $("#genre").val("");


    // displayDBmetadata(resultData);
}
/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitAddStarForm(formSubmitEvent) {
    console.log("submit add star form");
    formSubmitEvent.preventDefault();
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        data: addStar_form.serialize(),
        url: "api/dashboard",
        // success: handleDBupdateResult
        success: (resultData) => handleDBupdateResult(resultData, "addstar")
    });
}

function submitAddMovieForm(formSubmitEvent) {
    console.log("submit add movie form");
    formSubmitEvent.preventDefault();
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "api/dashboard",
        data: addMovie_form.serialize(),
        //success: handleDBupdateResult
        success: (resultData) => handleDBupdateResult(resultData, "addmovie")
    });
}
// Bind the submit action of the form to a handler function
addStar_form.submit(submitAddStarForm);
addMovie_form.submit(submitAddMovieForm);
// Bind the submit action of the form to a handler function
//search_form.submit(submitSearchForm);

function displayDBmetadata(resultData) {


    let table = $("#metadata_body");   let bodyHTML = "";
    if (resultData ==="undefined" || resultData ==="null" || resultData.length <= 0) return;
    let tabData = resultData;  let tabname="";
    for (let i = 0; i < tabData.length; i++) {
        if ( tabData[i]["table_name"] != tabname)
        {
            tabname=tabData[i]["table_name"];
            bodyHTML = bodyHTML + "<tr><td colspan='5' ><b> Table :" + tabData[i]["table_name"] + "</b></td></tr>";
            bodyHTML = bodyHTML + "<tr><td><b>Column Name</b></td><td><b>Type</b></td><td><b>Is Nullable?</b></td><td><b>Column Key</b></td><td><b>Extra Info</b></td></tr><tr>";
            bodyHTML = bodyHTML + "<tr><td colspan='5' ><hr /></td></tr>";
        }
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




if (pageLoaded === false){
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "api/dashboard",
        success: (resultData) => displayDBmetadata(resultData)
    });
}

function displayMsg(addMsg){

    msgDiv = document.getElementById("cartMsg");
    modal = document.getElementById("diveAddData");
    if (msgDiv !== "undefined" && msgDiv != "null")
        msgDiv.textContent = addMsg;
    if (modal !== "undefined" && modal != "null") modal.style.display = "block";
    else alert (addMsg);
}


function hidePopUp()
{
    modal = document.getElementById("diveAddData");
    if (modal !== "undefined" && modal != "null") modal.style.display = "none";
}
/*

window.onclick = function(event) {
   modal = document.getElementById("diveAddData");
   if (modal !== "undefined" && modal != "null")
   {
       if (event.target == modal) {
           modal.style.display = "none";
       }
   }
}

*/