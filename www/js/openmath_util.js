
// not sure how this method will handle redirects, error mesages from server etc. Need to define a better API first.
// Would like to move to wards more client side processing
function prompt doc_delete(name, doc_id){
	if (confirm("Are you sure you want to delete " + name + "?")){
		$.post("/index.php/user/delete_doc/" + doc_id);
		alert("Document successfully deleted.");
	}
}
