
<?php
if (sizeof($classes) == 0){ ?>
	<h2>You are not in any classes yet, create or join one below.</h2>
<?php
}else{ ?>
<table cellpadding="0" cellspacing="0" width="100%" class="sortable">
    <thead>
      <tr>
	<th>Subject</th>
	<th>Hour/Section</th>
	<th>Role</th>
	<th>Class ID</th>
	<th>Creation Date</th>
	<th>Enrollment</th>
      </tr>
    </thead>
	<?php
	foreach($classes as $class):?>
		<tr>
			<td>
				<a href="/index.php?/user/view_class/<?php echo $class['class_id']?>">
				<?php echo isset($class['subject'] ) ? $class['subject'] : "N/A";?> 
				</a>
			</td>
			<td> <?php echo isset($class['class_hour'] ) ? $class['class_hour'] : "N/A";?> </td>
			<td> <?php echo isset($class['role'] ) ? $class['role'] : "N/A";?></td>
			<td><?php echo isset($class['class_id'] ) ? $class['class_id'] : "N/A";?> </td>
			<td><?php echo isset($class['create_date'] ) ? $class['create_date'] : "N/A";?> </td>
			<td><?php if ( ! isset($class['enrollment'] )) echo 'closed'; else echo 'open'; ?> </td>
		</tr>
	<?php
	endforeach; ?>
	</table>
<?php
} ?>
<h2>
<a href="/index.php?/user/create_class">Create Class</a> &nbsp; &nbsp;
<a href="/index.php?/user/join_class">Join Class</a>
</h2>

