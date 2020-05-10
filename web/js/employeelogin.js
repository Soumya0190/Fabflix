

function handleLoginResult(resultDataString)
{
    //let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataString);
    console.log(resultDataString["status"]);
    alert(resultDataString);
    // If login succeeds, it will redirect the user to search.html
    if (resultDataString["status"] === "success")
    {
        window.location.replace("dashboard.html");
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
    alert("submit");
    console.log("submit employee login form");
    formSubmitEvent.preventDefault();
    $.ajax(
        "api/login", {
            method: "POST",
            data: login_form.serialize(),
            success: (resultData) => handleLoginResult(resultData)

        }
    );
}

// Bind the submit action of the form to a handler function
let emp_login_form = document.getElementById("emp_login_form") ;//$("#emp_login_form");
emp_login_form.submit(submitLoginForm);

