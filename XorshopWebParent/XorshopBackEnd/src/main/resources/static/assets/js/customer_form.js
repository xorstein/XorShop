function checkEmailUnique(form) {
	customerId = $("#id").val();
	customerEmail = $("#email").val();
	csrfValue = $("input[name='_csrf'").val();

	// url = "[[@{/customers/check_email}]]";
	url = moduleURL + "/check_email";
	
	params = {id : customerId, email: customerEmail, _csrf: csrfValue};

	$.post(url, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicated") {
			ShowModalDialog("Attention","L'adresse E-mail " + customerEmail+" est déjà utilisée!");
		} else {
			showErrorModal("Erreur","Une erreur inconnue s’est produite lors du traitement de la requete!");
		}			
	}).fail(function() {
		ShowModalDialog("Erreur","Impossible de se connecter au serveur!");	
	});

	return false;
}	