<!-- Need to update colleage search ajax, vurrently the code at the bottom of the page is pointing to the docuent search !!!!!-->
<?php
if (isset($colleagues) && sizeof($colleagues) > 0 ): ?>
<h2><label for="colleague_search">Search Collegues:</label>
<input type="text" name="colleague_search" value="" id="colleague_search" onKeyPress="search_colleagues(event)"/>
</h2>
<table cellpadding="0" cellspacing="0" width="100%" class="sortable" id="colleague_list">
    <thead>
      <tr>
	<th>First Name</th>
	<th>Last Name</th>
	<!--<th>Message</th>-->
	<th>Remove</th>
      </tr>
    </thead>
<?php
foreach($colleagues as $colleague):?>
	<tr> 
		<td><?php echo isset($colleague['first_name'] ) ? $colleague['first_name'] : "-";?> </td>
		<td><?php echo isset($colleague['last_name'] ) ? $colleague['last_name'] : "-";?> </td>
		<!--<td><a href="/index.php/user/message/<?php echo $colleague['user_id'] ?>">Message</a> </td>-->
		<td><a href="/index.php/user/remove_colleague/<?php echo $colleague['user_id'] ?>">Remove</a> </td>
	</tr>
<?php
endforeach; ?>
</table>
<?php
 else: ?>
	<h2>You do not have any colleagues yet.</h2>
<?php
endif; ?>

<script type="text/javascript">
function search_colleagues(e)
{
	if (e.keyCode == 13){
		try{xhr = new XMLHttpRequest();}
        catch (e)
        {xhr = new ActiveXObject("Microsoft.XMLHTTP");}
        // handle old browsers
        if (xhr == null)
        {alert("Ajax not supported by your browser!");return;}
        // construct URL
        var url = "/index.php/user/search_docs/" + escape(document.getElementById("colleague_search").value);
        // get list of docs
        xhr.onreadystatechange = function () {
            // only handle loaded requests
            if (xhr.readyState == 4)
            {
                // display response if possible
                if (xhr.status == 200)
                   $("#colleague_list").html(xhr.responseText);
                else
                   alert("Error with Ajax call! " + xhr.status);
           }
        };
        xhr.open("GET", url, true);
        xhr.send(null);
	}
}

</script>
