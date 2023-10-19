$(document).ready(function() {

	$("#productList").on("click", ".linkRemove", function(e) {

		e.preventDefault();

		if (doesOrderHaveOnlyOneProduct()) {
			showWarningModal("Impossible de supprimer le produit. La commande doit comporter au moins un seul produit!");
		} else {
			rowNumber = $(this).attr('rowNumber');
			link=$(this);
			var text = $('#row' + rowNumber + ' .row.m-2.text-center b').text();
			var modalText = "Voulez-vous vraiment supprimer le produit <strong>"+ text +" </strong>  num\u00e9ro <strong>" + rowNumber + "</strong> de votre commande?";
			$('#modalTitleProduct').text("Suppression du produit de la commande");
			$('#modalBodyProduct').html(modalText);
			$("#yesButton-modal-block-popin").off("click");

			// Add the click event handler to the "yesButton-modal-block-popin" button
			$("#yesButton-modal-block-popin").on("click", function() {
				removeProduct(link);
				updateOrderAmounts();
				$("#modal-block-popin").modal('hide');
			});

			// Show the modal
			$("#modal-block-popin").modal('show');

		}
	});
});

function doesOrderHaveOnlyOneProduct() {
	productCount = $(".hiddenProductId").length;
	return productCount == 1;
}

function removeProduct(link) {
	
	rowNumber = link.attr("rowNumber");
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
	
	// Redefine produdct index after deleting.
	// When there are three products and delete second one(1,2,3)  ,  third product replace second one and its index value will be 2.
	// When there are two products and delete first one (1,2), second one replace first one and its index value will be 1.
	$(".divCount").each(function(index, element) {
		element.innerHTML = "" + (index + 1);
	});
	
	 productDetailCount--;
	 updateElementIds();

    // Update ID for blankLine4
    $('#blankLine4').attr('id', 'blankLine' + (productDetailCount + 1));
}
function updateElementIds() {
     for (var i = 1; i <= productDetailCount; i++) {
        $('#row' + (i + 1)).attr('id', 'row' + i);
        $('#cost' + (i + 1)).attr('id', 'cost' + i);
        $('#quantity' + (i + 1)).attr('id', 'quantity' + i);
        $('#price' + (i + 1)).attr('id', 'price' + i);
        $('#subtotal' + (i + 1)).attr('id', 'subtotal' + i);
        $('#blankLine' + (i + 1)).attr('id', 'blankLine' + i);
        $('.linkRemove[rownumber="' + (i + 1) + '"]').attr('rownumber', i);
    }
}
 

