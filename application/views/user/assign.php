<form method="post" action="/index.php?/user/assign">
<div style="float:left; width:60%">
		<?php if (sizeof($docs) > 0){ ?>
			<label>Search Docs:</label>
			<input type="text" name="doc_search" value="" id="doc_search" onKeyPress="return search_docs(event)" maxlength="60" minlength="0" size="30"  />
			<br />
			<?php echo form_error('doc_id'); ?>
		<?php
		} ?>
		<h2>Assignment Properties</h2>

	<!-- list of documents, with radio buttons to select a single one in the list 
			be sure to make any changes here also refelcted in the seperate file for ajax requests
			and find a way to unify them soon-->
		<?php if (sizeof($docs) == 0){ ?>
			<h2>You have not added any documents yet.</h2>
		<?php
		} else{ ?>
		<?php echo form_error('doc_id'); ?>
		<table cellpadding="0" cellspacing="0" width="100%" class="sortable" id="doc_list">
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
				<?php if (set_value('doc_id') == $doc['doc_id']) { ?> checked="checked" <?php } ?>></td>
				<td><a href="/index.php?/user/assignment/<?php echo $doc['doc_id']?>">
					 <?php echo isset( $doc['docname'] ) ? $doc['docname'] : "N/A";?>
					</a></br>
					<p style="font-size:12px">Tags: <?php foreach($doc['tags'] as $tag) echo $tag.', '; ?> </p>
				</td>
				<td> <?php echo isset($doc['material_type'] ) ? $doc['material_type'] : "N/A"; ?></td>
				<td> <?php echo isset($doc['upload_date'] ) ? $doc['upload_date'] : "N/A"; ?></td>
			</tr>
		<?php endforeach ?>
		</tbody>
		</table>
<?php } ?>
</div>
	<!-- might add list of classes, for now just going to use session stored class id -->
	<!-- assignment info -->
<div style="float:left;width=20%; margin-left:2%">
	<h2>Current Class: </br> <?php echo $class_name ?> </h2>
		<p>Return to the <a href="/index.php?/user/classes">class list</a> to work with a different class.</p>
		<p>Date Format: (mm/dd/yy)</p>
		<?php echo form_error('assign_date'); ?>
		<label>Assign date:</label></br>
		<input type="text" name="assign_date" size="20" id="assign_date" value="<?php echo set_value('assign_date') ?>" /></br></br>
		<?php echo form_error('due_date'); ?>
		<label>Due date:</label></br>
		<input type="text" name="due_date" size="20" id="due_date" value="<?php echo set_value('due_date') ?>"/></br></br>
		<?php echo form_error('total_points'); ?>
		<label>Total Points:</label></br>
		<input type="text" name="total_points" size="20" id="total_points" value="<?php echo set_value('total_points') ?>"/></br></br>
		<?php echo form_error('notes'); ?>
		<label>Comments/Suggestions for Students:</label></br>
		<textarea rows="5" cols="30" name="notes" id="notes"><?php echo set_value('notes') ?></textarea></br></br>
		<input type="submit" name="submit" class="submit mid" value="Assign" />
</div>
</form>

<script type="text/javascript">
function search_docs(e)
{
	if (e.keyCode == 13){
		try{xhr = new XMLHttpRequest();}
        catch (e)
        {xhr = new ActiveXObject("Microsoft.XMLHTTP");}
        // handle old browsers
        if (xhr == null)
        {alert("Ajax not supported by your browser!");return;}
        // construct URL
        // construct URL
        var url = "/index.php?/user/search_assign_docs/" + escape(document.getElementById("doc_search").value);
        // get list of docs
        xhr.onreadystatechange = function () {
            // only handle loaded requests
            if (xhr.readyState == 4)
            {
                // display response if possible
                if (xhr.status == 200)
                   $("#doc_list").html(xhr.responseText);
                else
                    alert("Error with Ajax call! " + xhr.status);
           }
        };
        xhr.open("GET", url, true);
        xhr.send(null);
        return false;
	}
}

</script>
