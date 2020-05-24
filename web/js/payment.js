let payment_form = $("#payment_form");
payment_form.submit(submitPaymentForm);

function submitPaymentForm(formSubmitEvent)
{
    console.log("submit payment form");
    var data = "?cardNum="+ $("#cardNum").val()
        + "&txtFirstName=" + $("#txtFirstName").val()
        + "&txtLastName=" + $("#txtLastName").val()
        + "&txtExpDate=" + $("#txtExpDate").val();

    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "api/payment" + data,
        success: (resultData) => handlePaymentResult(resultData)
    });
}


function handlePaymentResult(resultDataJson)
{
    try {
        console.log("handle payment response");
        console.log(resultDataJson);
        console.log(resultDataJson["status"]);

        console.log("Handle payment response : status" + resultDataJson["status"]);
        if (resultDataJson["status"] === "success") {
            alert(resultDataJson["status"]);
            window.location.replace("confirmation.html");
        } else {
            alert("NOT SUCCESSFUL" + resultDataJson["status"]);
            console.log("show error message");
            console.log(resultDataJson["message"]);
            $("#payment_error_message").text(resultDataJson["message"]);
        }
    }
    catch(err){
        alert(err);
    }
}





