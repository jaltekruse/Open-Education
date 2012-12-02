Are you sure you want to remove <?php echo $docname ?>?
<table>
<tr>
<td>
<form method="post" action="/index.php?/user/delete_doc/<?php echo $doc_id ?>">
	<input type="hidden" name="delete_doc" value="1">
	<input type="submit" name="submit" class="submit long" value="Yes, Delete Document">
</form>
</td>
<td>
<form method="post" action="/index.php?/user/documents">
	<input type="submit" name="submit" class="submit mid" value="Cancel">
</form>
</td>
</tr>
</table>
