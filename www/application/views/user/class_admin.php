<?php
$password = array(
	'name'	=> 'password',
	'id'	=> 'password',
	'value' => set_value('password'),
	'maxlength'	=> 50,
	'minlength' => 8,
	'size'	=> 20,
);
$password_label = 'Password:';
$password2 = array(
	'name'	=> 'password2',
	'id'	=> 'password2',
	'value' => set_value('password2'),
	'maxlength'	=> 50,
	'minlength' => 8,
	'size'	=> 20,
);
$password2_label = "Confirm Password:";
?> 
<h2>Class Enrollment</h2>
<h3>Class ID = <?php echo $class_id ?>
<?php
if ( isset($enrollment_open) && $enrollment_open){
	echo ' - Currently Open'; ?>
	</h3>
	<p> Enrollment is currently open, students can join your class using the class ID listed above and the password you set. If you have forgotten the password, simply close enrollment and open it again with a new password. </p>
	<form method="post" action="/index.php/user/class_admin/<?php echo $class_id ?>">
	<input type="submit" name="submit" class="submit long" value="Close Enrollment">
	<input type="hidden" name="close_enrollment" value="1">
	</form>
<?php } else{
	echo ' - Currently Closed'; ?>
	</h2>
	<p> To open enrollment, you must set a password. Students will need the class ID listed above and the password you enter below to join your class. When you have all of your students have joined your class, enrollment can be closed. Enrollment can be opened at any time to allow new additions to the class.</p>
	<?php echo form_open($this->uri->uri_string()); ?>
	<table>
		<tr>
			<td><?php echo form_label($password_label, $password['id']); ?></td>
			<td><?php echo form_password($password); ?></td>
			<td style="color: red;"><?php echo form_error($password['name']); ?><?php echo isset($errors[$password['name']])?$errors[$subject['name']]:''; ?>
			</td>
		</tr>
		<tr>
			<td><?php echo form_label($password2_label, $password2['id']); ?></td>
			<td><?php echo form_password($password2); ?></td>
			<td style="color: red;"><?php echo form_error($password2['name']); ?><?php echo isset($errors[$password2['name']])?$errors[$password2['name']]:''; ?>
			</td>
		</tr>
	</table>

	<input type="submit" name="submit" class="submit long" value="Open Enrollment" />
	<?php echo form_close(); ?>
<?php }?>
</h3>
