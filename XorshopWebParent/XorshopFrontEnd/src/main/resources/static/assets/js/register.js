function checkEmailUnique(form) {
	url = contextPath + "customers/check_unique_email";
	customerEmail = $("#email").val();
	csrfValue = $("input[name='_csrf']").val();
	params = {email: customerEmail, _csrf: csrfValue};

	$.post(url, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicated") {
			showWarningModal("Cette adresse e-mail "+customerEmail+" est déjà utilisée! " );
		} else {
			showErrorModal("Unknown response from server");
		}
	}).fail(function() {
		showErrorModal("Could not connect to the server");
	});

	return false;
}
$(document).ready(function() {
	$("#buttonReset").on("click", function() {
		 var formulaire = document.getElementById("monFormulaire");
            formulaire.reset();
	});
});
	
