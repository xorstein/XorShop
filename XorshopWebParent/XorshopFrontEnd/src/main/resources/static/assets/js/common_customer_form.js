var dropDownCountry;
var dataListState;
var fieldState;

$(document).ready(function() {
	dropDownCountry = $("#country");
	dataListState =  $("#listStates"); // 1.Option Dependent Dropdown $("#listStates")
	//dataListState =  $("#stateList"); // 2.Option Dependent Dropdown $("#stateList")
	fieldState = $("#state");

	dropDownCountry.on("change", function() {
		loadStatesForCountry();
		fieldState.val("").focus();
	});
});

function loadStatesForCountry() {
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	url = contextPath + "settings/list_states_by_country/" + countryId;

	$.get(url, function(responseJSON) {
		dataListState.empty();
         var optionsHTML = "";
		$.each(responseJSON, function(index, state) {
			 optionsHTML += '<option value="' + state.name + '">' + state.name + '</option>';
		});
        dataListState.html(optionsHTML);
	}).fail(function() {
		alert('failed to connect to the server!');
	});
}

function checkPasswordMatch(confirmPassword) {
	if (confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity("Passwords do not match!");
	} else {
		confirmPassword.setCustomValidity("");
	}
}

