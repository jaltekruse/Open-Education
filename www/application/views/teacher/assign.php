<form method="post" action="/index.php/user/assign">
<div style="float:left;width:70%">
		<?php if (sizeof($docs) > 0){ ?>
			<label for="doc_search">Search Documents:</label>
			<input type="text" name="doc_search" value="" id="doc_search" maxlength="60" minlength="0" size="30"  />
			
		<?php
		} ?>
	<!-- list of documents -->
		<?php if (sizeof($docs) == 0){ ?>
			<h2>You have not added any assignments yet.
		<?php
		} else{ echo set_value('assign_date').'hello there'?>
		<p>
		<table cellpadding="0" cellspacing="0" width="100%" class="sortable">
			<thead>
			  <tr>
			<!-- no content for this column header, it is a column for the checkboxes -->
			<th></th>
			<th>Doc Name</th>
			<th>Material Type</th>
			<th>Upload Date</th>
			  </tr>
			</thead>
			<tbody>
		<?php foreach($docs as $doc):?>
			<tr>
				<td><input type="radio" name="doc_id" value="<?php echo $doc['doc_id'] ?>"
				<?php if (set_value('doc_id') == $doc['doc_id']) { ?> selected="selected" <?php } ?>></td>
				<td><a href="/index.php/user/view_doc/<?php echo $doc['doc_id']?>">
					 <?php echo isset( $doc['docname'] ) ? $doc['docname'] : "N/A";?>
					</a></br>
					<p style="font-size:12px">Tags: algebra, stuff, another tag, I'm not going to be very creative with this</p>
				</td>
				<td> <?php echo isset($doc['material_type'] ) ? $doc['material_type'] : "N/A"; ?></td>
				<td> <?php echo isset($doc['upload_date'] ) ? $doc['upload_date'] : "N/A"; ?></td>
			</tr>
		<?php endforeach ?>
		<tbody>
		</table>
		</p>
		<?php } ?>
	</div>
	<!-- might add list of classes, for now just going to use session stored class id -->
	<!-- assignment info -->
	<div style="float:left;width=20%">
	 <h2>Class: </br> <?php echo $class_name ?> </h2>
		<p>
		Return to the <a href="/index.php/user/classes">class list</a> to work with a different class.
		</p>
		<p>
		Date Format: (mm/dd/yy)
		</p>
		<label>Assign date:</label></br>
		<input type="text" class="text date_picker" name="assign date"/></br></br>
		<label>Due date:</label></br>

		<input type="text" class="text date_picker" name="due_date"/></br></br>
		<input type="submit" name="submit" class="submit mid" value="Assign" />
	</div>
</form>
