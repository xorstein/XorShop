var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
	
	fieldCountryName.prop("disabled", true);
	fieldCountryCode.prop("disabled", true);

	buttonLoad.click(function() {
		loadCountries();
	});

	dropDownCountry.on("change", function() {
		changeFormStateToSelectedCountry();
	});

	buttonAddCountry.click(function() {
    if (buttonAddCountry.text().trim() == "Sauvegarder") {
        checkUnique(function(isUnique) {
            if (isUnique) {
                addCountry();
            } else {
                changeFormStateToNewCountry();
            }
        });
    } else {
        // If the button text is not "Sauvegarder," simply change the form state
        changeFormStateToNewCountry();
    }
});

	buttonUpdateCountry.click(function() {
		updateCountry();
	});

	buttonDeleteCountry.click(function() {
		deleteCountry();
	});
});

function deleteCountry() {
	optionValue = dropDownCountry.val();
	countryId = optionValue.split("-")[0];
   
	url = contextPath + "countries/delete/" + countryId;
	$.ajax({
		type: 'GET',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeFormStateToNewCountry();
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'Le pays a été supprimé!'})
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});		
}

function updateCountry() {
	
	if (!validateFormCountry()) return;
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();

	countryId = dropDownCountry.val().split("-")[0];

	jsonData = {id: countryId, name: countryName, code: countryCode};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'Le pays a été mis à jour'})

		changeFormStateToNewCountry();
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});	
}

function addCountry() {
	
	if (!validateFormCountry()) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = {name: countryName, code: countryCode};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'Le nouveau pays a été ajouté'})
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});

}

function selectNewlyAddedCountry(countryId, countryCode, countryName) {
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function changeFormStateToNewCountry() {
	buttonAddCountry.html("<i class='si si-globe fa-1x text-primary'></i> Sauvegarder");
	//buttonAddCountry.val("Add");
	labelCountryName.text("Nom du pays");
	selectedCountryOption = $("#dropDownCountries option:selected");
    if (selectedCountryOption.length > 0) {
        // Unselect the selected option
        selectedCountryOption.prop("selected", false);
    }

	fieldCountryName.prop("disabled", false);
	fieldCountryCode.prop("disabled", false);

	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);

	fieldCountryCode.val("");
	fieldCountryName.val("").focus();	
}

function changeFormStateToSelectedCountry() {
	buttonAddCountry.html("<i class='si si-globe fa-1x text-primary'></i> Nouveau");
	
	fieldCountryName.prop("disabled", false);
	fieldCountryCode.prop("disabled", false);
	
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);

	labelCountryName.text("Pays sélectionné");

	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);

	countryCode = dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);

}

function loadCountries() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry.empty();
		
		$.each(responseJSON, function(index, country) {
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
		
	}).done(function() {
		buttonLoad.val("Refresh Country List");
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'Tous les pays ont été chargés'})
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});
}



function validateFormCountry() {
	formCountry = document.getElementById("formCountry");
	if (!formCountry.checkValidity()) {
		formCountry.reportValidity();
		return false;
	}	

	return true;
}


function checkUnique(callback) {
    console.log("checkUnique is working");

    countryName = $("#fieldCountryName").val();
    console.log(countryName);

    csrfValue = $("input[name='_csrf']").val();

    jsonData = {name: countryName, _csrf: csrfValue};

    checkUniqueUrl = contextPath + "countries/check_unique";
    console.log(checkUniqueUrl);
    
    $.ajax({
        type: 'POST',
        url: checkUniqueUrl,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(response) {
        if (response == "OK") {
            callback(true);
        } else if (response == "Duplicate") {
            One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'Pays déjà existe!' + countryName});
            callback(false);
        } else {
            One.helpers('jq-notify', {type: 'warning', icon: 'fa fa-exclamation me-1', message: 'Réponse inconnue du serveur'});
            callback(false);
        }
    }).fail(function() {
        One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
        callback(false);
    });
}
 