<?php
$subject = array(
	'name'	=> 'subject',
	'id'	=> 'subject',
	'value' => set_value('subject'),
	'maxlength'	=> 50,
	'size'	=> 20,
);
$subject_label = 'Subject:';
$class_hour = array(
	'name'	=> 'class_hour',
	'id'	=> 'class_hour',
	'value' => set_value('class_hour'),
	'maxlength'	=> 50,
	'size'	=> 20,
);
$class_hour_label = "Class Hour:";
?>
<?php echo form_open($this->uri->uri_string()); ?>
<table>
	<tr>
		<td><?php echo form_label($subject_label, $subject['id']); ?></td>
		<td><?php echo form_input($subject); ?></td>
		<td style="color: red;"><?php echo form_error($subject['name']); ?><?php echo isset($errors[$subject['name']])?$errors[$subject['name']]:''; ?>
		</td>
	</tr>
	<tr>
		<td><?php echo form_label($class_hour_label, $class_hour['id']); ?></td>
		<td><?php echo form_input($class_hour); ?></td>
		<td style="color: red;"><?php echo form_error($class_hour['name']); ?><?php echo isset($errors[$class_hour['name']]) ? $errors[$class_hour['name']]:''; ?>
		</td>
	</tr>
</table>

<input type="submit" name="submit" class="submit mid" value="Create Class" />
<?php echo form_close(); ?>
