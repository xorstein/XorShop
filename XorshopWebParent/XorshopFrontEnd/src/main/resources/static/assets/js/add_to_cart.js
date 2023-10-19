$(document).ready(function() {
	$("#buttonAdd2Cart").on("click", function(evt) {
		addToCart();
	});
});

function addToCart() {
	quantity = $("#quantity" + productId).val();
	url = contextPath + "cart/add/" + productId + "/" + quantity;

	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		if (!response.includes("Impossible")) {
			updateCartDetail(response);
		}
		showModalDialog("Ajouter un article au Panier", response);
	}).fail(function() {
		showErrorModal("Erreur lors de l'ajout du produit au panier.");
	});
}
function updateCartDetail() {
	decimalDigits = "[[${DECIMAL_DIGITS}]]";
	decimalPointType = "[[${DECIMAL_POINT_TYPE}]]";
	thousandsPointType = "[[${THOUSANDS_POINT_TYPE}]]"
	decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
	thousandsSeparator = thousandsPointType == 'COMMA' ? ',' : '.';
	var countProduct = 0;
    console.log(contextPath+"cart/count_products");
	// Request to get the count of products
	$.ajax({
		type: "GET",
		url: contextPath + "cart/count_products",
	}).done(function(response) {
		countProduct = response;

		// Request to get the total amount
		$.ajax({
			type: "GET",
			url: contextPath + "cart/total_products",
		}).done(function(response) {
			var total = response;
			total = $.number(total, decimalDigits, decimalSeparator, thousandsSeparator);

			// Update the count and total elements
			$("#total_number_product").text(countProduct);
			$("#total_amount").text(total);
		}).fail(function() {
			console.log("Erreur lors de la récupération du total des produits.");
		});
	}).fail(function() {
		console.log("Erreur lors de la récupération du nombre de produits.");
	});
}