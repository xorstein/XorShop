dropdownBrands = $("#brand");
dropdownCategories = $("#category");
  function CheckUnique(form) {
		
        productName = $("#name").val();
        productAlias = $("#alias").val();
        productID = $("#id").val();
        csrfValue = $("input[name='_csrf']").val();
        params = { id: productID, name: productName,alias: productAlias, _csrf: csrfValue };
        $.post(  ChekUnniqueUrl, params, function (response) {
          if (response == "OK") {
            form.submit();
          }
          else if (response == "Nom Dupliqué") {
            ShowModalDialog("Attention", "Le nom du produit " + productName + " est déjà utilisé!");
          }
          else if (response == "Alias Dupliqué") {
            ShowModalDialog("Attention", "L'alias' du produit " + productAlias + " est déjà utilisé!");
          }
          else {
            ShowModalDialog("Erreur", "Une erreur inconnue s’est produite lors du traitement de la requete!");
          }
        }).fail(function () {
          ShowModalDialog("Erreur", "Impossible de se connecter au serveur!");
        });
        return false;
      }
      
      $(document).ready(function() {


	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});	

	getCategoriesForNewForm();

});

function getCategoriesForNewForm() {
	catIdField = $("#categoryId");
	editMode = false;

	if (catIdField.length) {
		editMode = true;
	}

	if (!editMode) getCategories();
}

function getCategories() {
	brandId = dropdownBrands.val(); 
	if(brandId!=0){
		url = brandModuleURL + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});			
	});
	}
	
}