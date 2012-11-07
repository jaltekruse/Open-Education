<p>
Owner ID: <?php echo $doc['owner_user_id'] ?> <br />
Material Type: <?php echo $doc['material_type'] ?> <br />
Doc Type: <?php echo $doc['doc_type'] ?> <br />
</p>
<form>
<h2>
Share this document &nbsp;&nbsp;&nbsp; <input type="submit" class="submit mid" value="Share!"></input>
</h2>
<select multiple="multiple" class="chosen_dropdown" style="width:350px;" data-placeholder="Choose colleagues">
<?php foreach ( $colleagues as $colleague){ ?>
<option name="colleagues_sharing" value="<?php echo $colleague["user_id"]."\">".$colleague['first_name']." ".$colleague['last_name']?></option>
<?php } ?>
</select>
</form>
<div id="spacer_for_auto_complete" style="min-height:200px"></div>
<!--

add sharing options!! 

link to associated assignemnts, statics about the documents previuos success

or a comment list from other teachers if shared

assign now button?

-->
