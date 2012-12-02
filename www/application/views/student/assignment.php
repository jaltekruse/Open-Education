<?php
$userfile = array(
	'name'	=> 'userfile',
	'id'	=> 'userfile',
	'value' => set_value('userfile'),
	'maxlength'	=> 50,
	'minlength' => 1,
	'size'	=> 20,
);
$userfile_label = "File:"; ?>
<div>
<table cellpadding="0" cellspacing="0" width="100%" class="sortable">
    <thead>
      <tr>
	<th>Assigned</th>
	<th>Due</th>
	<th>Last Submit</th>
	<th>Points</th>
	<th>Score</th>
	<!--<th>Submitted</th>-->
      </tr>
    </thead>
    <tbody>
    <td> <?php echo isset($assignment['assign_date'] ) ?  $assignment['assign_date'] : "-"; ?></td>
		<td> <?php echo isset($assignment['due_date'] ) ? $assignment['due_date'] : "-"; ?></td>
		<td> <?php echo isset($assignment['submit_time'] ) ? 'factoring_worksheet.mdoc - ' . $assignment['submit_time'] : "Not Submitted"; ?></td>
		<td> <?php echo isset($assignment['total_points'] ) ? $assignment['total_points'] : "-"; ?></td>
		<td> <?php echo isset($assignment['student_score'] ) ? $assignment['student_score'] : "-"; ?></td>
		<!--<td> <?php echo (isset($assignment['submit_time'] ) && $assignment['submit_time'] != null ) ? 'yes' : "-"; ?></td>-->
    </tbody>
</table>
<h3>
Teacher Notes: <?php echo $assignment['notes'] ?>
</h3>
 <br />
<h2 style="float:left">
<?php 
$download_assignment_url = "/index.php?/user/student_assignment/" . $assignment['assignment_id'] . "/1";
if (isset($assignment['submit_time'])){ 
// if the assignment has been submitted ?>
	<div style="float:left">
		<h2> Continue Working </h2>
		<?php if ( substr($assignment['assignment_name'], strlen($assignment['assignment_name']) - 5) == ".mdoc"){ 
		// assignment is of type .mdoc ?>
			<input type="submit" name="submit" class="submit mid" value="Open Now" onclick="window.location=''">
			<br /><br />
		<?php } ?>
		<input type="submit" name="submit" class="submit mid" value="Download" 
			onclick="window.location='<?php echo $download_assignment_url ?>'" />
	</div>
	<div style="float:left;margin-left:60px">
		<h2> Original Assignment </h2>
		<?php if ( substr($assignment['assignment_name'], strlen($assignment['assignment_name']) - 5) == ".mdoc"){
		// assignment is of type .mdoc ?>
			<input type="submit" class="submit mid" value="Open Now" onclick="window.location=''">
			<br /><br />
		<?php } ?>
		<input type="submit" name="submit" class="submit mid" value="Download" onclick="window.location=''">
	</div>
	<?php
	}
else {
// the assignment has not been submitted yet ?>
	<div style="float:left;margin-left:60px">
	<h2> Start Working </h2>
	<?php if ( substr($assignment['assignment_name'], strlen($assignment['assignment_name']) - 5) == ".mdoc"){
	// assignment is of type .mdoc ?>
		<input type="submit" name="submit" class="submit mid" value="Open Now">
		<br /><br />
	<?php } ?>
	<input type="submit" class="submit mid" value="Download"
			onclick="window.location='<?php echo $download_assignment_url ?>'">
	</div>
<?php }?>
<div style="float:left;margin-left:60px">
<h2> Submit </h2>
<?php echo form_open_multipart('user/student_assignment/'.$assignment['assignment_id']);?>
<?php echo form_upload($userfile); ?> <br />
<input style="float:right" type="submit" name="submit" class="submit small" value="Submit!" />
<?php echo form_close(); ?>
</div>
</div>
<!--
add list of questions asked by students here?

or a comment list?
-->
