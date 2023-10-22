$(document).ready(function() {
    $(".link-deleted, .link-edited, .fas.fa-circle.link-enabled").on("click", function(event) {
        event.preventDefault();
        var link = $(this).attr("href");
        var productid = $(this).attr("entityId");
        var productName = $(this).attr("entitytName");
        if (productName.includes("-")) {

                productName = productName.replace(/-/g, ' ');
        }
        ShowActionModal(link, productid, productName);
    });
});

function ShowActionModal(link,productid,productName){
	 
          // Get the current URL
          deleteddString = "delete";
          editeddString = "edit";
          enabledString = "enabled";
          title="";
          action="";
          if (link.indexOf(deleteddString) !== -1) {
			  title="Suppression";
			  action="Supprimer";
		  } else if (link.indexOf(editeddString) !== -1) {
			  title="Modification de la fiche "+EntityName2;
		      action="Modifier";
 
          } 
          else if (link.indexOf(enabledString) !== -1) {
			  title="Modification du bloquage de "+EntityName;
			  if(link.indexOf("false") !== -1){
				  action="bloquer";
			  }
			  else  if(link.indexOf("true") !== -1){
				  action="d\u00e9sactiver le bloquage pour ";
			  }
		      
 
          } 
          
		  $("#yesButton").attr("href", link);
          $('#modal-block-slideleft-title').text(title);         
          var modalText = "Voulez-vous vraiment "+action+" "+EntityName+" <strong>" + productName + "</strong>  num\u00e9ro <strong>" + productid+"</strong>";
          $("#modalBody").html(modalText);
          let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-block-slideleft'));
          modal.show();
}


function clearFilter() {
	window.location = moduleURL;	
}


function handleDetailLinkClick(cssClass, modalId) {
	//alert(cssClass);
	   $(cssClass).on("click", function(e) {
		e.preventDefault();
		linkDetailURL = $(this).attr("href");
		$(modalId).modal("show").find(".modal-content").load(linkDetailURL);
		 //let modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('modal-block-tabs'));
         //modal.show();
	});		
}

function handleDefaultDetailLinkClick() {
	handleDetailLinkClick(".link-detail", "#detailModal");	
}