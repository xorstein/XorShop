var buttonLoad4States;
var dropDownCountry4States;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function() {
	buttonLoad4States = $("#buttonLoadCountriesForStates");
	dropDownCountry4States = $("#dropDownCountriesForStates");
	dropDownStates = $("#dropDownStates");
	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");
	
	fieldStateName.prop("disabled", true);

	buttonLoad4States.click(function() {
		loadCountries4States();
		buttonAddState.prop("disabled", false);
		
	});

	dropDownCountry4States.on("change", function() {
		loadStates4Country();
	});

	dropDownStates.on("change", function() {
		changeFormStateToSelectedState();
	});

	buttonAddState.click(function() {
		if (buttonAddState.text().trim() == "Sauvegarder") {
			checkUniqueState(function(isUnique) {
            if (isUnique) {
                addState();
            } else {
                changeFormStateToNew();
            }
        });
    } else {
        // If the button text is not "Sauvegarder," simply change the form state
        changeFormStateToNew();
    }
});

	buttonUpdateState.click(function() {
		updateState();
	});

	buttonDeleteState.click(function() {
		deleteState();
	});
});

function deleteState() {
	stateId = dropDownStates.val();

	url = contextPath + "states/delete/" + stateId;

	$.ajax({
		type: 'GET',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownStates option[value='" + stateId + "']").remove();
		changeFormStateToNew();
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'la province a été supprimé!'})
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});		
}

function updateState() {
	
	if (!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateId = dropDownStates.val();
	stateName = fieldStateName.val();

	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();

	jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		$("#dropDownStates option:selected").text(stateName);
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'La province a été mise a jour!'})
		changeFormStateToNew();
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});	
}

function addState() {
	
	if (!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();

	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();

	jsonData = {name: stateName, country: {id: countryId, name: countryName}};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectNewlyAddedState(stateId, stateName);
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'La nouvelle province a été ajoutée'})
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});

}

function selectNewlyAddedState(stateId, stateName) {
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

	$("#dropDownStates option[value='" + stateId + "']").prop("selected", true);

	fieldStateName.val("").focus();
}

function changeFormStateToNew() {
	//buttonAddState.val("Add");
	buttonAddState.html("<i class='si si-globe fa-1x text-primary'></i> Sauvegarder");
	labelStateName.text("Nom de l'État/de la province");

	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);
	selectedStateNameOption = $("#dropDownStates option:selected");
    if (selectedStateNameOption.length > 0) {
        // Unselect the selected option
        selectedStateNameOption.prop("selected", false);
    }
	fieldStateName.prop("disabled", false);
	
	fieldStateName.val("").focus();	
}

function changeFormStateToSelectedState() {
	//buttonAddState.prop("value", "New");
    buttonAddState.html("<i class='si si-globe fa-1x text-primary'></i> Nouveau");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);
	
	fieldStateName.prop("disabled", false);

	labelStateName.text("État/Province sélectionné");

	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);

}

function loadStates4Country() {
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	url = contextPath + "states/list_by_country/" + countryId;

	$.get(url, function(responseJSON) {
		dropDownStates.empty();

		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});

	}).done(function() {
		changeFormStateToNew();
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'Tous les états ont été chargés pour : '+selectedCountry.text()})
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});	
}

function loadCountries4States() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry4States.empty();

		$.each(responseJSON, function(index, country) {
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountry4States);
		});

	}).done(function() {
		buttonLoad4States.val("Refresh Country List");
		One.helpers('jq-notify', {from: 'bottom', align: 'center', message: 'Tous les pays ont été chargés'})
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
	});
} 

function validateFormState() {
	formState = document.getElementById("formState");
	if (!formState.checkValidity()) {
		formState.reportValidity();
		return false;
	}	

	return true;
}

function checkUniqueState(callback) {

	console.log("checkUnique is working");
	
	stateName = $("#fieldStateName").val();
	
	console.log(stateName);
	
	csrfValue = $("input[name='_csrf']").val();
	
	jsonData = {name: stateName, _csrf: csrfValue};
	
	checkUniqueUrl = contextPath + "states/check_unique";
	
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
			One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'Etat déjà existe!' + countryName});
			callback(false);
		} else {
			One.helpers('jq-notify', {type: 'warning', icon: 'fa fa-exclamation me-1', message: 'Réponse inconnue du serveur'});
			callback(false);
		}
	}).fail(function() {
		One.helpers('jq-notify', {type: 'danger', icon: 'fa fa-times me-1', message: 'ERREUR : Impossible de se connecter au serveur ou le serveur a rencontré une erreur!'});
		callback(false);
	});

}	 