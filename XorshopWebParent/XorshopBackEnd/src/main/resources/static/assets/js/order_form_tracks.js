var trackRecordCount;

$(document).ready(function() {

	trackRecordCount = $(".hiddenTrackId").length;
	$("#trackList").on("click", ".linkRemoveTrack", function(e) {
		e.preventDefault();
		link = $(this);
		rowNumber = $(this).attr('rowNumber');
		var modalText = "Voulez-vous vraiment supprimer le suivi de la commande </strong>  num\u00e9ro <strong>" + rowNumber + "</strong>";
		$('#modalTitleProduct').text("Suppression du suivi de la commande");
		$('#modalBodyProduct').html(modalText);

		// Unbind previously attached click event handlers
		$("#yesButton-modal-block-popin").off("click");

		// Add the click event handler to the "yesButton-modal-block-popin" button
		$("#yesButton-modal-block-popin").on("click", function() {
			deleteTrack(link);
			updateTrackCountNumbers();
			$("#modal-block-popin").modal('hide');
		});

		// Show the modal
		$("#modal-block-popin").modal('show');
	});

	$("#linkAddTrack").on("click", function(e) {
		e.preventDefault();
		addNewTrackRecord();
		
	});

	$("#trackList").on("change", ".dropDownStatus", function(e) {
		dropDownList = $(this);
		rowNumber = dropDownList.attr("rowNumber");
		selectedOption = $("option:selected", dropDownList);

		defaultNote = selectedOption.attr("defaultDescription");
		$("#trackNote" + rowNumber).text(defaultNote);
	});
});

function deleteTrack(link) {
	rowNumber = link.attr('rowNumber')
	$("#rowTrack" + rowNumber).remove();
	$("#emptyLine" + rowNumber).remove();
	UpdateRowNumbersForRemainingRows();
	$("#modal-block-popin").hide();


}

function updateTrackCountNumbers() {
	$(".divCountTrack").each(function(index, element) {
		element.innerHTML = "" + (index + 1);
	});
}

function addNewTrackRecord() {
	htmlCode = generateTrackCode();
	$("#trackList").append(htmlCode);
}
function UpdateRowNumbersForRemainingRows() {
	// Update row numbers for the remaining rows
	$(".row.border.rounded.p-1").each(function(index, element) {
		var currentRowNumber = index + 1;
		$(element).attr('id', 'rowTrack' + currentRowNumber);
		$(element).find('.linkRemoveTrack').attr('rowNumber', currentRowNumber);
		updateInputFieldsRowNumber(element, currentRowNumber);
	});

	// Update the trackRecordCount variable if needed (optional)
	trackRecordCount = $(".row.border.rounded.p-1").length;
}

function generateTrackCode() {
	trackRecordCount = $(".hiddenTrackId").length;
	nextCount = $(".row.border.rounded.p-1").length + 1;
	trackRecordCount++;
	//alert("nextCount "+nextCount);
	rowId = "rowTrack" + nextCount;
	emptyLineId = "emptyLine" + nextCount;
	trackNoteId = "trackNote" + nextCount;
	currentDateTime = formatCurrentDateTime();

	htmlCode = `
			<div class="row border rounded p-1" id="${rowId}">
				<input type="hidden" name="trackId" value="0" class="hiddenTrackId" />
				<div class="col-2">
				<div class="btn-group mt-2" role="group" aria-label="">
				<button type="button" class="btn btn-dark">
					<div class="divCountTrack">${nextCount}</div>
				</button>
				<button type="button" class="btn btn-danger">
						<a class="fas fa-trash icon-dark text-light linkRemoveTrack" href="" rowNumber="${nextCount}"></a>
					</button>
				</div>
				
							
				</div>				
				
				<div class="col-10 p-5">
				  <div class="form-group row">
				    <label class="col-form-label">Time:</label>
				    <div class="col">
						<input type="datetime-local" name="trackDate" value="${currentDateTime}" class="form-control" required
							style="max-width: 300px"/>						
				    </div>
				  </div>					
				<div class="form-group row">  
				<label class="col-form-label">Status:</label>
				<div class="col">
					<select name="trackStatus" class="form-control dropDownStatus" required style="max-width: 150px" rowNumber="${nextCount}">
			`;

	htmlCode += $("#trackStatusOptions").clone().html();

	htmlCode += `
				      </select>						
				    </div>
				  </div>
				  <div class="form-group row">
				    <label class="col-form-label">Notes:</label>
				    <div class="col">
						<textarea rows="2" cols="10" class="form-control" name="trackNotes" id="${trackNoteId}" style="max-width: 300px" required></textarea>
				    </div>
				  </div>
				  
				</div>				
			</div>	
			<div id="${emptyLineId}" class="row">&nbsp;</div>
	`;

	return htmlCode;
}

function formatCurrentDateTime() {
	date = new Date();
	year = date.getFullYear();
	month = date.getMonth() + 1;
	day = date.getDate();
	hour = date.getHours();
	minute = date.getMinutes();
	second = date.getSeconds();

	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;

	if (hour < 10) hour = "0" + hour;
	if (minute < 10) minute = "0" + minute;
	if (second < 10) second = "0" + second;

	return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;

} 

function updateInputFieldsRowNumber(element, currentRowNumber) {
    // Update the ID of the row element
    $(element).attr('id', 'rowTrack' + currentRowNumber);

    // Update the 'rowNumber' attribute for the relevant elements
    $(element).find('.dropDownStatus').attr('rownumber', currentRowNumber);
    $(element).find('textarea[name="trackNotes"]').attr('id', 'trackNote' + currentRowNumber);

    // Update the ID of the corresponding empty line
    $(element).next('.row').attr('id', 'emptyLine' + currentRowNumber);

    // If you have more dynamic elements, update their attributes here
}
