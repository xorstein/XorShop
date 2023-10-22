/**
 * 
 */
$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		//console.log(moduleURL);
		window.location = moduleURL;
	});

	$("#fileImage").change(function() {
		if (!checkFileSize(this)) {
			return;
		}

		showImageThumbnail(this);

	});
});

function checkFileSize(fileInput) {
	fileSize = fileInput.files[0].size;

	if (fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + " bytes!");
		fileInput.reportValidity();

		return false;
	} else {
		fileInput.setCustomValidity("");

		return true;
	}	
}


function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};

	reader.readAsDataURL(file);
} 

function ShowModalDialog(title, message) {
	$("#modalTitle").text(title);
    $("#modalBody").text(message);
    let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-block-slideleft'));
    modal.show();
}

function showErrorModal(message) {
	showModalDialog("Erreur", message);
}

function showWarningModal(message) {
	ShowModalDialog("Attention", message);
}