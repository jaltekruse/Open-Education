<?php if (sizeof($docs) == 0){
		if (isset($searched)) {?>
			<h2>Search returned no results.</h2>
		<?php
		}else{ ?>
			<h2>You have not added any assignments yet. &nbsp;&nbsp;<a href="/index.php/user/assign">Create New Assignment</a></h2>
	<?php
		}
	}else{ ?>
<thead>
      <tr>
	<th>Doc Name</th>
	<th>Size(Mb)</th>
	<th>Doc Type</th>
	<th>Material Type</th>
	<th>Upload Date</th>
	<th colspan="2">Actions</td>
      </tr>
    </thead>
    <tbody>
<?php foreach($docs as $doc):?>
	<tr>
		<td><a href="/index.php/user/doc_details/<?php echo $doc['doc_id']?>">
			 <?php echo isset( $doc['docname'] ) ? $doc['docname'] : "N/A";?>
			</a></br>
			<p style="font-size:12px">Tags: <?php foreach($doc['tags'] as $tag) echo $tag.', '; ?></p>
		</td>
		<td> <?php echo isset($doc['size'] ) ?  number_format(($doc['size'] / 1024), 2) : "N/A"; ?></td>
		<td> <?php echo isset($doc['doc_type'] ) ? $doc['doc_type'] : "N/A"; ?></td>
		<td> <?php echo isset($doc['material_type'] ) ? $doc['material_type'] : "N/A"; ?></td>
		<td> <?php echo isset($doc['upload_date'] ) ? $doc['upload_date'] : "N/A"; ?></td>
		<td> <a href="/index.php/user/documents/<?php echo $doc['doc_id']?>">Download</a></td>
		<td> <a href="/index.php/user/delete_doc/<?php echo $doc['doc_id']?>">Delete</a></td>
	</tr>
<?php endforeach ?>
<tbody>
<?php } ?>
