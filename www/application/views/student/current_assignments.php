<?php
$userfile = array(
	'name'	=> 'userfile',
	'id'	=> 'userfile',
	'value' => set_value('userfile'),
	'maxlength'	=> 50,
	'minlength' => 1,
	'size'	=> 20,
);
$userfile_label = "File:";
if (sizeof($assignments) == 0){ ?>
	<h2>Your teacher has added any assignments yet.</h2>
<?php
} else{ ?>
<!--<div style="float:left;width:25%">
<h2>Working Online</h2>
<ul>
	<li>To complete your assignment click "Edit Assignment"
	<li>This will only work with assignments designed for the Open Notebook application, they should end with .mdoc
	<li>If the document does not open automatically, you will have to follow the working offline instructions</li>
	<li>You can submit your work right from the application</li>
	<li>At any time you can also save your current work to your computer</li>
</ul>
<h2>Working Offline</h2>
<ul>
	<li>Use the download link to save your assignment for working offline</li>
	<li>When you are done working, select your finished assignment file and hit the submit button</li>
</ul>
<h2>Important Note</h2>
<ul>
	<li><b>We only store your most recent submission, backups of your previous work will have to be stored on your own computer if you want to refer to them later</b></li>
</ul>
</div>-->
<!--<div style="float:left;width:65%;margin-left:3%;">-->
<table cellpadding="0" cellspacing="0" width="100%" class="sortable">
    <thead>
      <tr>
	<th>Assignment Name</th>
	<th>Assigned</th>
	<th>Due</th>
	<th>Last Submit</th>
	<th>Points</th>
	<th>Score</th>
      </tr>
    </thead>
    <tbody>
<?php foreach($assignments as $assignment):?>
	<tr>
		<td><a href="/index.php/user/assignment/<?php echo $assignment['assignment_id']?>">
			 <?php echo isset( $assignment['assignment_name'] ) ? $assignment['assignment_name'] : "-";?>
			</a>
		<!--View Assignment
		<br />
		<a href="/index.php/user/student_current_assignments/<?php echo $assignment['assignment_id']?>">Download</a>&nbsp;&nbsp;
		<a href="/index.php/user/student_current_assignments/<?php echo $assignment['assignment_id']?>" title="View document originally assigned by your teacher">View Now</a> &nbsp;&nbsp;
		<br /><br />
		Start or Continue Working
		<br />
		<a href="/index.php/user/student_current_assignments/<?php echo $assignment['assignment_id']?>" title="Launch Open Notebook to work on your assignment">Work Now</a> &nbsp;&nbsp;
		<a href="/index.php/user/student_current_assignments/<?php echo $assignment['assignment_id']?>">Download Assignment</a>
		<?php echo form_open_multipart('user/student_current_assignments/'.$assignment['assignment_id']);?>
		<br /><br /> 
		<?php echo form_upload($userfile); ?> <br />
		<input style="float:right" type="submit" name="submit" class="submit small" value="Submit!" />
		<?php echo form_close(); ?>-->
		</td>
		<td> <?php echo isset($assignment['assign_date'] ) ?  $assignment['assign_date'] : "-"; ?></td>
		<td> <?php echo isset($assignment['due_date'] ) ? $assignment['due_date'] : "-"; ?></td>
		<td> <?php echo isset($assignment['submit_time'] ) ? /* $assignment['submit_doc_name'] . ' - ' .*/ $assignment['submit_time'] : "Not Submitted"; ?></td>
		<td> <?php echo isset($assignment['total_points'] ) ? $assignment['total_points'] : "-"; ?></td>
		<td> <?php echo isset($assignment['student_score'] ) ? $assignment['student_score'] : "-"; ?></td>
	</tr>
<?php endforeach?>
<tbody>
</table>
<!--</div>-->
<?php } ?>
