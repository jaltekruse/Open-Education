<h2>Create new Account or Log In</h2>

<p>
<?php echo validation_errors(); ?>

<?php echo form_open('user/create') ?>
	<p>
	<label for="email">E-mail:</label>
	<input type="input" name="email" class="text small" value="<?php echo set_value('email'); ?>" > </input><br/>
	<p />
	<p>
	<input type="submit" class="submit long" name="submit" value="Create/Login"/>
	<p />
</form>
<p /><br />
