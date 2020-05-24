let login_form = $("#login_form");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString)
{
    //let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataString);
    console.log(resultDataString["status"]);
    //alert("usertype="+ resultDataString["usertype"] +",  status = "+resultDataString["status"]);

    // If login succeeds, it will redirect the user to search.html
    if (resultDataString["status"] === "success")
    {
        if (resultDataString["usertype"] === "admin") {
            window.location.replace("dashboard.html");
        }
        else
        {
            window.location.replace("search.html");
        }
    } else {
        console.log("show error message");
        console.log(resultDataString["message"]);
        $("#login_error_message").text(resultDataString["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent)
{
    console.log("submit login form");
    formSubmitEvent.preventDefault();
    $.ajax(
        "api/login", {
       // "http://192.168.1.129:8080/cs122b_spring20_team_3_war_exploded/api/login", {
     //   "http://10.0.2.2:8080/cs122b_spring20_team_3_war_exploded/api/login",{
            method: "POST",
            data: login_form.serialize(),
            success: handleLoginResult
        }
    );
}

// Bind the submit action of the form to a handler function
if ($("#login_form")) {
    login_form.submit(submitLoginForm);
}

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

if ($("#usertype")) {
    let usrType = getParameterByName('usertype');
    $("#usertype").val(usrType);
    if ( usrType === "admin")
    {
        $("#spnEmpLogin").hide();
        $("#divRecaptcha").hide();
        $("#empMsg").textContent= "Employee Login";
    } else
    {
        $("#spnEmpLogin").show();
        $("#divRecaptcha").show();
        $("#empMsg").textContent= "Customer Login";
    }

}