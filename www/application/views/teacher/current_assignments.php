<?php
if (sizeof($assignments) == 0){ ?>
	<h2>You have not added any assignments yet.
<?php
} else{ ?>
<p>
<table cellpadding="0" cellspacing="0" width="100%" class="sortable">
    <thead>
      <tr>
	<th>Assignment Name</th>
	<th>Assign Date</th>
	<th>Due Date</th>
	<th>Total Points</th>
	<th colspan="2">Actions</td>
      </tr>
    </thead>
    <tbody>
<?php foreach($assignments as $assignment):?>
	<tr>
		<td><a href="/index.php/user/assignment/<?php echo $assignment['assignment_id']?>">
			 <?php echo isset( $assignment['assignment_name'] ) ? $assignment['assignment_name'] : "N/A";?>
			</a>
		</td>
		<td> <?php echo isset($assignment['assign_date'] ) ?  $assignment['assign_date'] : "N/A"; ?></td>
		<td> <?php echo isset($assignment['due_date'] ) ? $assignment['due_date'] : "N/A"; ?></td>
		<td> <?php echo isset($assignment['total_points'] ) ? $assignment['total_points'] : "N/A"; ?></td>
		<td>
		<a href="/index.php/user/assignment_doc/<?php echo $assignment['assignment_id']?>">View</a>&nbsp;
		<a href="/index.php/user/assignment_doc/<?php echo $assignment['assignment_id']?>">Download</a>&nbsp;
		<a href="/index.php/user/delete_assignment/<?php echo $assignment['assignment_id']?>">Delete</a>&nbsp;
		</td>
	</tr>
<?php endforeach ?>
<tbody>
</table>
</p>
<?php } ?>