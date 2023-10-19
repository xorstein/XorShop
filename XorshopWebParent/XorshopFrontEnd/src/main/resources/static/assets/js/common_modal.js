function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	 $("#modalDialog").modal('show');
}
function showModalDialog1(title) {
	$("#modalTitleConfirme").text(title);
	 $("#confirmModal").modal('show');
}

function showErrorModal(message) {
	showModalDialog("Erreur", message);
}

function showWarningModal(message) {
	showModalDialog("Attention", message);
}