<html>
<body>
<?php
$num_minutes = 10;
if ($_SERVER['REQUEST_METHOD'] === 'POST'){
	if (is_numeric($_POST['minutes']))
		$num_minutes = $_POST['minutes'];
}
?>
<h1> data recently uploaded <?php $current_time = new DateTime(); echo $current_time->sub(new DateInterval("PT" . $num_minutes . "M"))->format('H:i') . " - " . date('H:i'); ?></h1>

<a href="/mbed.php">Upload more data</a><br>

<form action="" method="post">
Number of Minutes: <input type="text" name="minutes" <?php echo "value = \"$num_minutes\" " ?>></input><br>
<input type="submit" value="submit" /><br>
</form>

<p>Below is a list of all scan data observations uploaded in the last <?php echo $num_minutes ?> minutes. These records will not include any of the data that was found to be in error, these records must be ammended before uploading them again.</p>
<table border="1" style="width:100%;border-collapse:collapse;font-size:12px" cellpadding="3" cellspacing="3">
<?php
$tables = array(	array('animals'	,				'animal_id', 			'animal_id', 			'animal_code'),
					array('activities',			'activity_id', 			'activity_id', 			'activity_code'),
					array('vigilant_behaviors',	'behavior_id',			'behavior_id', 			'behavior_code'),
					array('habitats',				'habitat_id', 			'habitat_habitat_id', 	'habitat_code'),
					array('relative_heights',		'relative_height_id', 	'relative_height_id', 	'relative_height_code'),
					array('actual_heights',		'actual_height_id', 	'actual_height_id', 	'actual_height_code'),
					array('distances_from_neighbor','distance_from_neighbor_id',	'distance_from_neighbor_id', 'distance_from_neighbor_code'),
					array('groups',				'group_id', 			'group_group_id', 		'group_name') );
					
$db = new mysqli('localhost', 'mbed_importer', 'dsjfh3#$/436$%@#$5kdfj1234%&8634%Jer4r5^', 'mbed');

$result = $db->query("show columns from scan_observations");

echo "<thead><tr>";
for ($i = 0; $i < $result->num_rows; $i++){
	$row = $result->fetch_assoc();
	echo "<th>" . str_replace("_", "<br>", $row['Field']) . "</th>";
}
echo "<th>neighbors</th>";
echo "</tr></thead>";

$result = $db->query(	"select scan_observations.*, GROUP_CONCAT(animals.animal_code SEPARATOR ', ') " .
						"as neighboring from scan_observations left outer join neighbors on " .
						"scan_observations.observation_id = neighbors.observation_id " .
						"left outer join animals on animals.animal_id = neighbors.animal_id " .
						"where NOW() - upload_date < " . (10 * $num_minutes) . " group by scan_observations.observation_id");

/*
select ID,Name,
    stuff((select ',' + CAST(t2.SomeColumn as varchar(10))
     from @t t2 where t1.id = t2.id and t1.name = t2.name
     for xml path('')),1,1,'') SomeColumn
from @t t1
group by id,Name
*/

$table_names = array();
$neighbor_result;
if ( $result ){
	for ($i = 0; $i < $result->num_rows; $i++){
		echo "<tr ";
		if ($i % 2 == 0) echo "style=\"background-color:#DEDEDE\">";
		else echo ">";
		$row = $result->fetch_assoc();
		$counter = 0;	
		foreach ($row as $column){
			if ( ($counter > 4 && $counter < 12) ){
				$pos = array_search($column, $table_names);
				$key = get_code_from_key($db, $tables[$counter - 4][0], $tables[$counter - 4][3], $column, $tables[$counter - 4][1]);
				echo "<td style=\"margin:2px\">" . $key . "</td>";
			}
			else if ( $counter == 3){
				$pos = array_search($column, $table_names);
				$key = get_code_from_key($db, $tables[$counter - 3][0], $tables[$counter - 3][3], $column, $tables[$counter - 3][1]);
				echo "<td style=\"margin:2px\">" . $key . "</td>";
			}

			else if ($counter == 4){
				if ($column == 1){
					echo "<td style=\"margin:2px\">" . "yes" . "</td>";
				}
				else{
					echo "<td style=\"margin:2px\"></td>";
				}
			}
			else{
				echo "<td style=\"margin:2px\">" . $column . "</td>";
			}
			$counter++;
		}
		
		
		/*
		$neighbor_result = $db->query("select animal_code from animals, neighbors where animals.animal_id = neighbors.animal_id and neighbors.observation_id = '" . $row[0] . "'");
		for ($i = 0; $i < $neighbor_result->num_rows; $i++){
			$row = $neighbor_result->fetch_assoc();
			echo "<td>" . $row['animal_code'] . "</td>";
		}
		*/
		echo "</tr>";
	}
}
else{
	echo $db->error;
}

function get_code_from_key($db, $table, $code_column, $id_value, $id_column){
	$query = "select " . $code_column . " from " . $table . " where " . $id_column . " = '" . $id_value . "'";
	$result = $db->query($query);
	//echo $query . "<br>";
	if ( $result ){
		$row = $result->fetch_assoc();
		//echo $result->num_rows . " " .$id_column . ": " . $row[$id_column] . "<br>";
		return $row[$code_column];
	}else{
		echo $db->error;
		return null;
	}	
}
?>
</table>
</body>
</html>
