var iconNames = {
	'PICKED': 'fa-people-carry',
	'SHIPPING': 'fa-shipping-fast',
	'DELIVERED': 'fa-box-open',
	'RETURNED': 'fa-undo'
};



function addEventHandlerForYesButton(modal) {
	$("#yesButton").click(function(e) {
		e.preventDefault();
        sendRequestToUpdateOrderStatus($(this));
		modal.hide();
	});
}

function sendRequestToUpdateOrderStatus(button) {
	requestURL = button.attr("href");

	$.ajax({
		type: 'POST',
		url: requestURL,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		$("#modalBody").text("Order updated successfully");
		updateStatusIconColor(response.orderId, response.status);

		console.log(response);
	}).fail(function(err) {
		showMessageModal("Error updating order status");
	})
}

function updateStatusIconColor(orderId, status) {
	link = $("#link" + status + orderId);
	link.replaceWith("<i class='fas " + iconNames[status] + " fa-2x text-success'></i>");
}

function showUpdateConfirmModal(link, orderId, status) {
	$("#yesButton").attr("href", link);
	$('#modal-block-slideleft-title').text("Mettre à jour le suivi de la commande ORD"+orderId);
	var modalText = "Etes-vous sûr de vouloir mettre à jour le statut de l'ID de commande n° <strong>#" + orderId + "</strong>  sur <strong>" + getStatusInFrench(status) + "</strong>?";
	$("#modalBody").html(modalText);
	let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-block-slideleft'));
	modal.show();
    addEventHandlerForYesButton(modal);
    
}


$(document).ready(function() {
	$(".linkUpdateStatus").on("click", function(event) {
		event.preventDefault();
		var link = $(this).attr("href");
		var orderId = $(this).attr("orderId");
		var status = $(this).attr("status");

		showUpdateConfirmModal(link, orderId, status);
	});
});
function getStatusInFrench(status) {
    switch (status) {
        case "PICKED":
            return "Récupéré"; 
        case "SHIPPING":
            return "En cours de livraison"; 
        case "DELIVERED":
            return "Livré"; 
        case "RETURNED":
            return "Retourné"; 
        default:
            return status; 
    }
}
