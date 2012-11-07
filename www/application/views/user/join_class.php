<?php
$class_id = array(
	'name'	=> 'class_id',
	'id'	=> 'class_id',
	'value' => set_value('class_id'),
	'maxlength'	=> 50,
	'minlength' => 1,
	'size'	=> 20,
);
$class_id_label = "Class ID:";
$password = array(
	'name'	=> 'password',
	'id'	=> 'password',
	'value' => set_value('password'),
	'maxlength'	=> 50,
	'minlength' => 5,
	'size'	=> 20,
);
$password_label = 'Password:';
?> 
<h2>Class Enrollment</h2>
<p> To join a class, enter the class ID and password set by your teacher below.</p>
<?php echo form_open($this->uri->uri_string()); ?>
<table>
	<tr>
		<td><?php echo form_label($class_id_label, $class_id['id']); ?></td>
		<td><?php echo form_input($class_id); ?></td>
		<td style="color: red;"><p><?php echo form_error($class_id['name']); ?><?php echo isset($errors[$class_id['name']])?$errors[$class_id['name']]:''; ?> </p>
		</td>
	</tr>
	<tr>
		<td><?php echo form_label($password_label, $password['id']); ?></td>
		<td><?php echo form_password($password); ?></td>
		<td style="color: red;"><?php echo form_error($password['name']); ?><?php echo isset($errors[$password['name']])?$errors[$subject['name']]:''; ?>
		</td>
	</tr>
</table>

<input type="submit" name="submit" class="submit" value="Enroll" />
<?php echo form_close(); ?>
