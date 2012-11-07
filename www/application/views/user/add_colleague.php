<?php
$email = array(
	'name'	=> 'email',
	'id'	=> 'email',
	'value' => set_value('email'),
	'maxlength'	=> 50,
	'size'	=> 20,
);
$email_label = "New Colleague E-mail:";
?>
<p> Enter the e-mail address of a colleague below to connect with on Open-Math. </p>
<?php echo form_open($this->uri->uri_string()); ?>
<table>
	<tr>
		<td><?php echo form_label($email_label, $email['id']); ?></td>
		<td><?php echo form_input($email); ?></td>
		<td style="color: red;"><?php echo form_error($email['name']); ?><?php echo isset($errors[$email['name']])?$errors[$email['name']]:''; ?>
		</td>
	</tr>
</table>

<input type="submit" name="submit" class="submit mid" value="Add Colleague" />
<?php echo form_close(); ?>
