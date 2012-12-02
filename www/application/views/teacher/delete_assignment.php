Are you sure you want to remove <?php echo $assignment_name ?>? <br />
<b>WARNING</b> - This will delete all student submissions and grading data from our records.
<table>
<tr>
<td>
<form method="post" action="/index.php?/user/delete_assignment/<?php echo $assignment_id ?>">
	<input type="hidden" name="delete_assignment" value="1">
	<input type="submit" name="submit" class="submit long" value="Delete Assignment">
</form>
</td>
<td>
<form method="post" action="/index.php?/user/current_assignments">
	<input type="submit" name="submit" class="submit mid" value="Cancel">
</form>
</td>
</tr>
</table>
