<?php
$uploaddir = $_SERVER['DOCUMENT_ROOT'].'/adminus/uploads/';

$uploadfile = $uploaddir . basename($_FILES['userfile']['name']);

if (move_uploaded_file($_FILES['userfile']['tmp_name'], $uploadfile)) {
	echo "Done!";
} else {
// WARNING! DO NOT USE "FALSE" STRING AS A RESPONSE!
// Otherwise onSubmit event will not be fired
	echo "Error";
}
?>