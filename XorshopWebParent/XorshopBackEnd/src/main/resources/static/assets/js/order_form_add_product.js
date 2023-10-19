var productDetailCount;

$(document).ready(function() {

	productDetailCount = $(".hiddenProductId").length;


	$("#linkAddProduct").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		url = link.attr("href");

		$("#modalIframe").attr("src", "");
		$("#addProductModal").on("shown.bs.modal", function() {
			$('body').css("margin-right", "0px!important");
			$(this).find("#modalIframe").attr("src", url);
		});
		$("#addProductModal").modal('show');

	});
});

function addProduct(productId, productName) {
	getShippingCost(productId);
}

function getShippingCost(productId) {
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();

	state = $("#state").val();
	if (state.length == 0) {
		state = $("#city").val();
	}

	requestUrl = contextPath + "get_shipping_cost";
	params = { productId: productId, countryId: countryId, state: state };

	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: params
	}).done(function(shippingCost) {
		getProductInfo(productId, shippingCost);
	}).fail(function(err) {
		showWarningModal(err.responseJSON.message);
		shippingCost = 0.0;
		getProductInfo(productId, shippingCost);
	}).always(function() {
		$("#addProductModal").modal("hide");
	});
}

function getProductInfo(productId, shippingCost) {
	requestURL = contextPath + "products/get/" + productId;
	$.get(requestURL, function(productJson) {
		console.log(productJson);
		productName = productJson.name;
		mainImagePath = contextPath.substring(0, contextPath.length - 1) + productJson.imagePath;
		productCost = $.number(productJson.cost, 2);
		productPrice = $.number(productJson.price, 2);

		htmlCode = generateProductCode(productId, productName, mainImagePath, productCost, productPrice, shippingCost);
		$("#productList").append(htmlCode);

		updateOrderAmounts();

	}).fail(function(err) {
		showWarningModal(err.responseJSON.message);
	});
}

function generateProductCode(productId, productName, mainImagePath, productCost, productPrice, shippingCost) {
	nextCount = productDetailCount + 1;
	productDetailCount++;
	rowId = "row" + nextCount;
	quantityId = "quantity" + nextCount;
	priceId = "price" + nextCount;
	subtotalId = "subtotal" + nextCount;
	blankLineId = "blankLine" + nextCount;
	htmlCode = `
		<div class="border rounded p-1" id="${rowId}">
		    <input type="hidden" name="detailId" value="0" />
			<input type="hidden" name="productId" value="${productId}" class="hiddenProductId" />
			<div class="row mt-2 justify-content-center align-items-center">
			<div class="col-1 text-center btn-group" role="group" aria-label="">
				<button type="button" class="btn btn-dark">
							<div class="divCount">${nextCount}</div>
							</button>
							<button type="button" class="btn btn-danger"><a class="fas fa-trash icon-dark text-light linkRemove" href=""
									 rowNumber="${nextCount}"></a></button>
					
									
				</div>
				<div class="col-12 col-sm-4 text-center">
							<img src="${mainImagePath}" class="img-fluid"  />
						</div>
			
			</div>
			
			<div class="row m-2 text-center">
				<b>${productName}</b>
			</div>
			
			<div class="row m-2 text-center">
			<table>
				<tr>
					<td>Valeur d'achat:</td>
					<td>
						<input type="text" required class="form-control m-1 cost-input"
						    name="productDetailCost"
							rowNumber="${nextCount}" 
							value="${productCost}" style="max-width: 140px"/>
					</td>
				</tr>
				<tr>
					<td>Quantité:</td>
					<td>
						<input type="number" step="1" min="1" max="5" class="form-control m-1 quantity-input"
						    name="quantity"
							id="${quantityId}"
							rowNumber="${nextCount}" 
							value="1" style="max-width: 140px"/>
					</td>
				</tr>	
				<tr>
					<td>Prix d'unité:</td>
					<td>
						<input type="text" required class="form-control m-1 price-input"
						    name="productPrice"
							id="${priceId}"
							rowNumber="${nextCount}" 
							value="${productPrice}" style="max-width: 140px"/>
					</td>
				</tr>
				<tr>
					<td>Total article(s):</td>
					<td>
						<input type="text" readonly="readonly" class="form-control m-1 subtotal-output"
						    name="productSubtotal"
							id="${subtotalId}" 
							value="${productPrice}" style="max-width: 140px"/>
					</td>
				</tr>				
				<tr>
					<td>Frais de livraison:</td>
					<td>
						<input type="text" required class="form-control m-1 ship-input"
						    name="productShipCost"  
							value="${shippingCost}" style="max-width: 140px"/>
					</td>
				</tr>											
			</table>
			</div>
			
		</div>
		<div id="${blankLineId}"class="row">&nbsp;</div>	
	`;

	return htmlCode;
}

function isProductAlreadyAdded(productId) {
	productExists = false;

	$(".hiddenProductId").each(function(e) {
		aProductId = $(this).val();

		if (aProductId == productId) {
			productExists = true;
			return;
		}
	});

	return productExists;
} 