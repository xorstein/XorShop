var dropdownCountries;
var dropdownStates;

$(document).ready(function() {
	dropdownCountries = $("#country");
	dropdownStates = $("#listStates");

	dropdownCountries.on("change", function() {
		loadStates4Country();
		$("#state").val("").focus();
	});	

	loadStates4Country();

	$('#buttonCancel').click(function() { 
		window.location = moduleURL;	
	});		
});

function loadStates4Country() {
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();

	url = contextPath + "states/list_by_country/" + countryId;
  
	$.get(url, function(responseJson) {
		
		dropdownStates.empty();
		
	  $.each(responseJson, function(index, state) {
          //  $("<option>").attr("value", state.name).appendTo(dropdownStates);
            dropdownStates.append($('<option>').val(state.name).text(state.name))
        });
	}).fail(function() {
		ShowModalDialog("Erreur", "Erreur lors du chargement des états/provinces pour le pays sélectionné.");
	})	
}	