<?php
if (isset($assignments) && sizeof($assignments) > 0 ): ?>
Assign Date: <?php echo $assignment['assign_date'] ?> <br />
Due Date: <?php echo $assignment['due_date'] ?> <br />
Points: <?php echo $assignment['total_points'] ?> <br />
Teacher Notes: <?php echo $assignment['notes'] ?> <br />
<table cellpadding="0" cellspacing="0" width="100%" class="sortable">
    <thead>
      <tr>
	<td>Last Name</td>
	<td>First Name</td>
	<td>Submitted</td>
	<td colspan="2">Actions</td>
      </tr>
    </thead>
<?php
foreach($assignments as $assignment):?>
	<tr>
		<td><?php echo isset($assignment['first_name'] ) ? $assignment['first_name'] : "-";?> </td>
		<td><?php echo isset($assignment['last_name'] ) ? $assignment['last_name'] : "-";?> </td>
		<?php if (isset($assignment['student_doc'] ) && $assignment['student_doc'] != NULL ){ ?>
			<td>yes</td>
			<td><a href="/index.php/user/view_submission/<?php echo $assignment['assignments_assignment_id'].'/'.$assignment['users_user_id']?>">View</a>
			<td><a href="/index.php/user/teacher_assignment/<?php echo $assignment['assignments_assignment_id'].'/'.$assignment['users_user_id']?>">Download</a>
		<?php } else {?>
			<td>-</td>
			<td>-</td>
			<td>-</td>
		<?php } ?>
	</tr>
<?php
endforeach; ?>
</table>

<?php
else: ?>
No students have been assigned this assignment, to open enrollment for students to join your class click on the link at the top labeled Class Admin.
<?php endif; ?>

