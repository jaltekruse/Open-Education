<?php
if (sizeof($docs) == 0){ ?>
	<h2>You have not added any documents yet.
	&nbsp;&nbsp; <a href="/index.php/user/upload_doc">Upload Doc</a> &nbsp; &nbsp; <a href="/OpenNotebook.jnlp">Create Doc</a>
	</h2>
<?php
} else{ ?>
<form action="" method="post">
		<h2><label for="doc_search">Search Documents:</label>
		<input type="text" name="doc_search" value="" id="doc_search" onKeyPress="search_docs(event)" maxlength="60" minlength="0" size="30"  />
</form>
		&nbsp;&nbsp; <a href="/index.php/user/upload_doc">Upload Doc</a> &nbsp; &nbsp; <a href="/OpenNotebook.jnlp">Create Doc</a>
		</h2>
<table cellpadding="0" cellspacing="0" width="100%" class="sortable" id="doc_list">
    <thead>
      <tr>
	<th>Doc Name</th>
	<th>Size(Mb)</th>
	<th>Doc Type</th>
	<th>Material Type</th>
	<th>Upload Date</th>
	<th colspan="3">Actions</th>
      </tr>
    </thead>
    <tbody>
<?php foreach($docs as $doc):?>
	<tr>
		<td>
			<a href="/index.php/user/doc_details/<?php echo $doc['doc_id']?>">
			 <?php echo isset( $doc['docname'] ) ? $doc['docname'] : "-";?>
			</a><br />
			<span style="font-size:12px">Tags: <?php foreach($doc['tags'] as $tag) echo $tag.', '; ?></span>
		</td>
		<td> <?php echo isset($doc['size'] ) ?  number_format(($doc['size'] / 1024), 2) : "-"; ?></td>
		<td> <?php echo isset($doc['doc_type'] ) ? $doc['doc_type'] : "-"; ?></td>
		<td> <?php echo isset($doc['material_type'] ) ? $doc['material_type'] : "-"; ?></td>
		<td> <?php echo isset($doc['upload_date'] ) ? $doc['upload_date'] : "-"; ?></td>
		<td> <a href="/index.php/user/documents/<?php echo $doc['doc_id']?>">Download</a></td>
		<td> <a href="/index.php/user/delete_doc/<?php echo $doc['doc_id']?>">Delete</a></td>
		<td> <a href="/index.php/user/edit_doc/<?php echo $doc['doc_id']?>">Edit</a></td>
	</tr>
<?php endforeach ?>
<tbody>
</table>
<?php echo $pagination_links; ?>
<?php } ?>

<script type="text/javascript">
function search_docs(e)
{
	if (e.keyCode == 13){
		$.get("/index.php/user/search_docs/" + escape(document.getElementById("doc_search").value), function(data){
			$("#doc_list").html(data);
		});
	}
}

</script>
