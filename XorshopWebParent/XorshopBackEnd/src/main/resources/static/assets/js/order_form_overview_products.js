var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;
//$.number_format.defaults.decimal = '.';
//$.number_format.defaults.thousands = ',';
$(document).ready(function() {

	fieldProductCost = $("#productCost");
	fieldSubtotal = $("#subtotal");
	fieldShippingCost = $("#shippingCost");
	fieldTax = $("#tax");
	fieldTotal = $("#total");

	formatOrderAmounts();
	formatProductAmounts();

	$("#productList").on("change", ".quantity-input", function(e) {
		updateSubtotalWhenQuantityChanged($(this));
		updateOrderAmounts();
	});

	$("#productList").on("change", ".price-input", function(e) {
		updateSubtotalWhenPriceChanged($(this));
		updateOrderAmounts();
	});	

	$("#productList").on("change", ".cost-input", function(e) {
		updateOrderAmounts();
	});

	$("#productList").on("change", ".ship-input", function(e) {
		updateOrderAmounts();
	});			
});

function updateOrderAmounts() {
	totalCost = 0.0;

	$(".cost-input").each(function(e) {
	    costInputField = $(this);
		rowNumber = costInputField.attr("rowNumber");
		quantityInput = $(".quantity-input[rowNumber=" + rowNumber + "]");
		quantityValue = quantityInput.val();
		productCost = getNumberValueRemovedThousandSeparator(costInputField); 
		//alert(productCost);
		totalCost += productCost * parseInt(quantityValue); 
	});
	

	setAndFormatNumberForField("productCost", totalCost);

	orderSubtotal = 0.0;

	$(".subtotal-output").each(function(e) {
		productSubtotal = getNumberValueRemovedThousandSeparator($(this));
		orderSubtotal += productSubtotal;
	});

	setAndFormatNumberForField("subtotal", orderSubtotal);

	shippingCost = 0.0;

	$(".ship-input").each(function(e) {
		productShip = getNumberValueRemovedThousandSeparator($(this));
		shippingCost += productShip;
	});

	setAndFormatNumberForField("shippingCost", shippingCost);

	tax = getNumberValueRemovedThousandSeparator(fieldTax);
	orderTotal = orderSubtotal + tax + shippingCost;
	setAndFormatNumberForField("total", orderTotal);
}

function setAndFormatNumberForField(fieldId, fieldValue) {
	formattedValue = $.number(fieldValue, 2);
	$("#" + fieldId).val(formattedValue);
}

function getNumberValueRemovedThousandSeparator(fieldRef) {
	  // Remove spaces
    fieldValue = fieldRef.val().replace(/\s+/g, "");
    // Replace commas with periods
    fieldValue = fieldValue.replace(/,/g, ".");
	return parseFloat(fieldValue);
} 

function updateSubtotalWhenPriceChanged(input) {
	priceValue = getNumberValueRemovedThousandSeparator(input);
	rowNumber = input.attr("rowNumber");

	quantityField = $("#quantity" + rowNumber);
	quantityValue = quantityField.val();
	newSubtotal = parseFloat(quantityValue) * priceValue;

	setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);	
}

function updateSubtotalWhenQuantityChanged(input) {
	quantityValue = input.val();
	rowNumber = input.attr("rowNumber");
	priceValue = getNumberValueRemovedThousandSeparator($("#price" + rowNumber));
	//alert("hamza"+$("#price" + rowNumber));
	newSubtotal = parseFloat(quantityValue) * priceValue;

	setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
}

function formatProductAmounts() {
	$(".cost-input").each(function(e) {
		formatNumberForField($(this));
	});

	$(".price-input").each(function(e) {
		formatNumberForField($(this));
	});	

	$(".subtotal-output").each(function(e) {
		formatNumberForField($(this));
	});	

	$(".ship-input").each(function(e) {
		formatNumberForField($(this));
	});	
}

function formatOrderAmounts() {
	formatNumberForField(fieldProductCost);
	formatNumberForField(fieldSubtotal);
	formatNumberForField(fieldShippingCost);
	formatNumberForField(fieldTax);
	formatNumberForField(fieldTotal);	
}

function formatNumberForField(fieldRef) {
	fieldRef.val($.number(fieldRef.val(), 2));
}

function processFormBeforeSubmit() {
	setCountryName();

	removeThousandSeparatorForField(fieldProductCost);
	removeThousandSeparatorForField(fieldSubtotal);
	removeThousandSeparatorForField(fieldShippingCost);
	removeThousandSeparatorForField(fieldTax);
	removeThousandSeparatorForField(fieldTotal);

	$(".cost-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});

	$(".price-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});

	$(".subtotal-output").each(function(e) {
		removeThousandSeparatorForField($(this));
	});			

	$(".ship-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});		


	return true;
}

function removeThousandSeparatorForField(fieldRef) {
	 const currentValue = fieldRef.val();
    const cleanedValue = currentValue.replace(/\s+/g, '').replace(',', '.');
    fieldRef.val(cleanedValue);
	
}

function setCountryName() {
	selectedCountry = $("#country option:selected");
	countryName = selectedCountry.text();
	$("#countryName").val(countryName);
}
/*$(document).ready(function() {
    // Assuming your form has an ID, replace 'your-form-id' with your actual form ID.
    $('#your-form-id').on('submit', function() {
        // Iterate through the input fields with the specified class names.
        $(this).find('input.cost-input, input.price-input, input.subtotal-output, input.ship-input').each(function() {
            // Replace commas with periods (for decimal separator).
            var originalValue = $(this).val();
            var formattedValue = originalValue.replace(/,/g, '.');
            // Update the input field with the formatted value.
            $(this).val(formattedValue);
        });
    });
});*/
  