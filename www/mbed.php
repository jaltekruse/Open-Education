<html>
<body>
<h1> mbed data upload</h1>
<p>
<a href="/mbed_recent_uploads.php">Check recent uploads</a><br>
<?php
// do not change this array!! the positions of these elements are used throughout the program!
$tables = array(	array('animals'	,				'animal_id', 			'animal_id', 			'animal_code'),
					array('activities',			'activity_id', 			'activity_id', 			'activity_code'),
					array('vigilant_behaviors','behavior_id', 				'behavior_id', 			'behavior_code'),
					array('habitats',				'habitat_id', 			'habitat_habitat_id', 	'habitat_code'),
					array('relative_heights',		'relative_height_id', 	'relative_height_id', 	'relative_height_code'),
					array('actual_heights',		'actual_height_id', 	'actual_height_id', 	'actual_height_code'),
					array('distances_from_neighbor','distance_from_neighbor_id',	'distance_from_neighbor_id', 'distance_from_neighbor_code'),
					array('groups',				'group_id', 			'group_group_id', 		'group_name') );

$db = new mysqli('localhost', 'mbed_importer', 'dsjfh3#$/436$%@#$5kdfj1234%&8634%Jer4r5^', 'mbed');

if (mysqli_connect_errno()){
	echo "connection error";
	exit;
}

$authenticated = false;
$password_wrong = true;
if ($_SERVER['REQUEST_METHOD'] === 'POST'){
	$input = $_POST['input'];
	$group_name = $_POST['group'];
	$just_validate = $_POST['just_validate'];
	$data_source = $_POST['data_source'];
	if ($_POST['password'] == 'bulernum'){
		$authenticated = true;
		$password_wrong = false;
	}
	$key = get_foreign_key($db, $tables[7][0], $tables[7][3], $group_name, $tables[7][1]);
	if ($key == null)
		echo "group name does not match an entry in the database.\n";
	$group_code = $key;
}
else{
	$group_code = "";
	$group_name = "";
	$input = "";
	$data_source = "";
	$just_validate = false;
}

$table = "scan_observations";

$columns = "date_time, gps, carrying_infant,";
for ($i = 0; $i < count($tables); $i++)
	$columns = $columns . $tables[$i][2] . ', ';
$columns = $columns . "notes, upload_date, data_source";

$insert_prefix = "insert into " . $table . "(". $columns. ") values ";
//$insert; // this is no longer used as each row is being inserted seperately

$entry_parts;$successful_add_counter = 0; $error_counter = 0; $last_date = null; $last_time = null; $last_gps = null;
$current_date;$current_time;$current_gps; $validation_queries;$current_duplicate_check;

$lines_in_error = "";
$current_insert; $error_with_column; $observation_id;
// building this string allows for the data to always be displayed with the results in error
// as the previous record is is referring to might have been correct and thus
// not display in the set of entries in error
$current_date_time_gps;$error_char = "*";

if ($authenticated){
	$lines = explode("\r\n", $input);
	foreach($lines as $line){
		if ($line == "") continue;
		$error_with_column = null;
		$current_duplicate_check = "";
		$entry_parts = explode("\t", $line);
		foreach($entry_parts as &$part){
			// the part of the entry was previously marked as having an error with an asterisk character,
			// assume they fixed and remove the aserisk so it does not interfere below
			if (substr($part, strlen($part) - 1) == $error_char){
				$part = substr($part, 0, strlen($part) - 1);
			}
		}
		$current_insert = " ( '";
		// check if there is a current time, date and gps entry, if not replace it with the previous one
	
		$current_date = $entry_parts[0] == "" ? $last_date : $entry_parts[0];
		if ( is_null($current_date)) {// if no date has been entered yet
			$lines_in_error = $lines_in_error . $line . "\n";
			continue;
		}
		$last_date = $current_date;

		$current_time = $entry_parts[1] == "" ? $last_time : $entry_parts[1];
		if (is_null($current_time)) {// if no time has been entered yet
			$lines_in_error = $lines_in_error . $line . "\n";
			continue;
		}
		$last_time = $current_time;
	
		$current_gps = $entry_parts[2] == "" ? $last_gps : $entry_parts[2];
		if ( is_null($current_gps)) {// if no time has been entered yet
			$lines_in_error = $lines_in_error . $line . "\n";
			continue;
		}
		$last_gps = esc($db, $current_gps);
	
		// extract the date and format it for database insertion
		$date_parts = explode("/", $current_date);
		// check that the month is valid
		if ( count($date_parts) != 3 || ! checkdate(get_month($date_parts[1]), $date_parts[0], $date_parts[2]) ){
			$error_with_column = 0;// there was an error with the first column (adding one later)
			$current_date_time_gps = $current_date . $error_char . "\t";
		}
		else
			$current_date_time_gps = $current_date . "\t";
	
		$current_date = $date_parts[2] . '-' . get_month($date_parts[1]) . '-' . $date_parts[0] . ' ';
		$time_parts = explode(":", $current_time);
		if ( count($time_parts) < 2 || ! is_numeric($time_parts[0]) || ! is_numeric($time_parts[1]) || intval($time_parts[0]) < 0 || intval($time_parts[0]) > 23 
				|| intval($time_parts[1]) < 0 || intval($time_parts[1]) > 59){ // change this to do validation on the time
			$error_with_column = 1;// there was an error with the second column(adding one later)
			$current_date_time_gps = $current_date_time_gps . $current_time . $error_char . "\t";
		}
		else{
			$current_date_time_gps = $current_date_time_gps . $current_time . "\t";
		}
		$current_date = esc($db, $current_date) . ' ' . esc($db, $current_time) . ":00'"; // add the time with seconds 
		$current_insert = $current_insert . $current_date . ", '" . $current_gps . "',";
		$current_date_time_gps = $current_date_time_gps . $current_gps . "\t";
	
		// check to see if an infant was being carried, indicated by a + at the end of the animal ID
		if (substr($entry_parts[3], strlen($entry_parts[3]) - 1) == '+'){
			$current_insert = $current_insert . "'1', ";
			// take the + off of the end of the string
			$entry_parts[3] = substr($entry_parts[3], 0, strlen($entry_parts[3]) - 1);
		}else{
			$current_insert = $current_insert . "'0', ";
		}
	
		if ( ! is_null($error_with_column)){
			handle_error(&$lines_in_error, &$current_date_time_gps, &$entry_parts, $error_with_column, &$error_counter);
			continue;
		}
		// add all of the data from user input of the table
		// the - 1 is to exclude the groups at the end of the list, this is pulled out of the input above, not the complete result set
		for ($i = 0; $i < count($tables) - 1 /* read this ^ */; $i++){
			if ( trim($entry_parts[$i + 3]) == "")
			{// empty values are allowed
				$current_insert = $current_insert . "null,";
			}
			else{
				$key = get_foreign_key($db, $tables[$i][0], $tables[$i][3], $entry_parts[$i + 3], $tables[$i][1]);
				if ( is_null($key) && ! trim($entry_parts[$i + 3]) == ""){
					$error_with_column = $i + 3;
					break;
				}
				$current_insert = $current_insert . "'" . mysqli_real_escape_string($db, $key) ."',";
			}
		}
		if ( ! is_null($error_with_column)){
			handle_error(&$lines_in_error, &$current_date_time_gps, &$entry_parts, $error_with_column, &$error_counter);
			continue;
		}
		// need to check all of the adjacent animals before inserting the parent record, otherwise it will be hard to link up the neighboring records 
		// if I allow the observations to be inserted right away
		for ($i = 10; $i < count($entry_parts) && $i < 14; $i++){
			// SV - code for 'without neighbors'
			if ($entry_parts[$i] == "" || $entry_parts[$i] == "SV") continue;
			if (substr($entry_parts[$i], strlen($entry_parts[$i]) - 1) == '+'){
				// take the + off of the end of the string to check the animal code
				$key = get_foreign_key($db, $tables[0][0], $tables[0][3],
						substr($entry_parts[$i], 0, strlen($entry_parts[$i]) - 1), $tables[0][1]);
			}else{// no infant is being carried
				$key = get_foreign_key($db, $tables[0][0], $tables[0][3], $entry_parts[$i], $tables[0][1]);
			}
			if (is_null($key)){
				$error_with_column = $i;
				break;
			}
		}
		if ( ! is_null($error_with_column)){
			handle_error(&$lines_in_error, &$current_date_time_gps, &$entry_parts, $error_with_column, &$error_counter);
			continue;
		}

		// take off the last comma as it will break the query
		//$current_insert = substr($current_insert, 0, strlen($current_insert) - 1);
		$current_insert = $current_insert . "'" . $group_code . "','" . (isset($entry_parts[14]) ? $entry_parts[14] : "") . "' , NOW(), '" . $data_source . "')";
		//insert a single row
		//echo $insert_prefix . $current_insert . "<br>";

		if ($just_validate){
			continue;
		}
	
		$result = $db->query($insert_prefix . $current_insert);
	
		if ($result){// successfully inserted an observation record
			//echo 'inserted record ' . $db->insert_id . "<br>";
			$observation_id = $db->insert_id;
			// insert a record for adjacent animals
			for ($i = 10; $i < count($entry_parts) && $i < 14; $i++){
				if ($entry_parts[$i] == "" || $entry_parts[$i] == "SV") continue;
			
				if (substr($entry_parts[$i], strlen($entry_parts[$i]) - 1) == '+'){
					$carrying_infant = '1';
					//echo "NEIGHBOR WITH INFANT <br>";
					// take the + off of the end of the string
					$entry_parts[$i] = substr($entry_parts[$i], 0, strlen($entry_parts[$i]) - 1);
				}else{
					$carrying_infant = '0';
				}
				$key = $key = get_foreign_key($db, $tables[0][0], $tables[0][3], $entry_parts[$i], $tables[0][1]);
				$query = "insert into neighbors(observation_id, animal_id, carrying_infant) values ('" .
						"" . esc($db, $observation_id) . "', '" . esc($db, $key) . "', '" . esc($db, $carrying_infant) . "')";
				$result = $db->query($query);
				if ($result); // successfully inserted an neighbor record 
				else{
					// this is an unexpected error as the observation was just added and I checked the animal_id above for the neighbors
					// should probably still return a message that one of the observations might not have all of the neighbors it should
				}
			}
			$successful_add_counter++;
		}
		else{
			// as I checked all of the parts of the input, this should not throw an error, but databases can have unexpected problems
			handle_error(&$lines_in_error, &$current_date_time_gps, &$entry_parts, 1000, &$error_counter);
			continue;
		}
	}
}

function esc($db, $key){
	return mysqli_real_escape_string($db, $key);
}

function get_month($month_name){
	switch(strtolower($month_name)){
		case 'jan': return 1;
		case 'feb': return 2;
		case 'mar': return 3;
		case 'apr': return 4;
		case 'may': return 5;
		case 'jun': return 6;
		case 'jul': return 7;
		case 'aug': return 8;
		case 'sep': return 9;
		case 'oct': return 10;
		case 'nov': return 11;
		case 'dec': return 12;
		default: 	 return null; 
	}
}

function handle_error($lines_in_error, $current_date_time_gps, $entry_parts, $error_column, $error_counter){
	$error_counter++;
	$error_row = $current_date_time_gps;
	for($i = 3; $i < count($entry_parts); $i++){
		$error_row = $error_row . $entry_parts[$i];
		if ($i == $error_column)
			$error_row = $error_row . "*";
		
		$error_row = $error_row . "\t";
	}
	if ($error_column == 1000){
		echo "DATABASE ERROR, an unexpected problem prevented a record from being submitted: " . $db->error . "<br>";
		echo "Record contents:" .  htmlentities($error_row) . "<br>";
		echo "It is included in the box below, but we were unable to identify why it was in error, so it will be without an asterisk.<br><br>";
	}
	$lines_in_error = $lines_in_error . $error_row . "\n";
}

function get_foreign_key($db, $table, $code_column, $code_value, $id_column){
	//$validation_queries++;
	$query = "select " . $id_column . " from " . $table . " where " . $code_column . " = '" . esc( $db, $code_value ) . "'";
	$result = $db->query($query);
	//echo $query . "<br>";
	if ( $result ){
		$row = $result->fetch_assoc();
		//echo $result->num_rows . " " .$id_column . ": " . $row[$id_column] . "<br>";
		return $row[$id_column];
	}else{
		echo $db->error;
		return null;
	}	
}

function get_code_from_key($db, $table, $code_column, $id_value, $id_column){
	$query = "select " . $code_column . " from " . $table . " where " . $id_column . " = '" . $id_value . "'";
	$result = $db->query($query);
	//echo $query . "<br>";
	if ( $result ){
		$row = $result->fetch_assoc();
		//echo $result->num_rows . " " .$id_column . ": " . $row[$id_column] . "<br>";
		return $row[$id_column];
	}else{
		echo $db->error;
		return null;
	}	
}
?>
</p>
<form action="" method="post">
<?php if (trim($lines_in_error) != ""){ ?>
<p> <?php echo $successful_add_counter ?> records sucessfully submitted. </p>
<p> The following <?php echo $error_counter; ?> record(s) had errors, the column in error is marked with an asterisk, you can amend the records and resubmit them <p/>
<?php } else if ($_SERVER['REQUEST_METHOD'] === 'POST' && $successful_add_counter > 0){?>
<p> All records successfully submitted.
<?php } if ($password_wrong && $_SERVER['REQUEST_METHOD'] == 'POST') {?>
<p style="color:FF0000"> The password was incorrect no records were checked for accuracy or submitted. </p>
<?php } if ($password_wrong) {?>
<textarea name="input" cols="150" rows="30"><?php echo htmlentities($_POST['input']); ?></textarea><br>
<?php } else {?>
<textarea name="input" cols="150" rows="30"><?php echo htmlentities($lines_in_error); ?></textarea><br>
<?php } ?>
Group:<br>
<?php
$result = $db->query("select distinct group_name from groups");
for ($i = 0; $i < $result->num_rows; $i++){
	$row = $result->fetch_assoc();
	echo "<input type=\"radio\" name=\"group\" ";
	if(htmlentities($group_name) == htmlentities($row['group_name'])) echo "checked=\"checked\" ";
	echo "value=\"" . htmlentities($row['group_name'])  . "\" id=\"radio" . 
		$i . "\"><label for=\"radio" . $i . "\">" . htmlentities($row['group_name']) . "</label></input> &nbsp;&nbsp;";
}
$db->close();
?>
<br>
Password:<input type="password" name="password" <?php echo "value=\"\"" ?>></input><br>
Data Source (spreadsheet filename):<input type="text" name="data_source" <?php echo "value = \"$data_source\" " ?>></input><br>
<!--Columns (seperate with commas, put date column at end):<br>
Table:<input type="text" name="table" /><br>
<input type="text" name="columns" /><br> -->
<input type="checkbox" name="just_validate" /> Don't Submit (just check the data for errors)<br>
<input type="submit" value="submit" /><br>
</body>
</html>
