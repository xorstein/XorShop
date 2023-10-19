$(".buttonCancel").on("click", function() {
	window.location = moduleURL;
});
function showImageThumbnail(fileInput, thumbnailElementId) {
  console.log("showImageThumbnail called with", fileInput, thumbnailElementId);
  
  var file = fileInput.files[0];
  console.log("Selected file:", file);

  var reader = new FileReader();

  reader.onload = function() {
    console.log("Reader result:", reader.result);
    $("#" + thumbnailElementId).attr("src", reader.result); // Use reader.result here
  };

  reader.readAsDataURL(file);
}

$("#fileImage").change(function() {
  showImageThumbnail(this, "thumbnail");
});

$("#logo_dashboard").change(function() {
  showImageThumbnail(this, "dashboard_thumbnail");
});

$("#logo_footer").change(function() {
  showImageThumbnail(this, "footer_thumbnail");
});

$("#logo_login_site").change(function() {
  showImageThumbnail(this, "login_site_thumbnail");
});

$("#logo_login_dashboard").change(function() {
  showImageThumbnail(this, "login_dashboard_thumbnail");
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