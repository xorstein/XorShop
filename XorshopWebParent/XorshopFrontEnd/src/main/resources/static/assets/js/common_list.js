function clearFilter() {
	window.location = moduleURL;
}
function SearchIconSubmit() {
	 if ($('#keyword').val() !== null && $('#keyword').val().trim() !== '') {
            // If it's not empty or null, submit the form
            $('#searchForm').submit();
        }
}

function handleDetailLinkClick(linkClass, modalId,title) {
	$(linkClass).on("click", function(e) {
		e.preventDefault();
		linkDetailURL = $(this).attr("href");
		$("#OrderModalTitle").text(title);
		$(modalId).modal("show").find(".modal-body").load(linkDetailURL);
	});		
}

function handleDefaultDetailLinkClick() {
	handleDetailLinkClick(".link-detail", "#detailModal");	
} 
function handleDefaultDetailLinkClick(title) {
	handleDetailLinkClick(".link-detail", "#detailModal",title);	
} 
