<html>
<body>
<h1> A PAGE!</h1>
<p>
<?php
if ($_SERVER['REQUEST_METHOD'] == 'GET'){?>
</p>
<form action="" method="post">
<textarea name="input" cols="100" rows="30"></textarea></br>
Table:<input type="text" name="table" /></br>
Columns (seperate with commas, put date column at end):</br>
<input type="text" name="columns" /></br>
<input type="submit" value="submit" />
</body>
</html>
<?php 
exit;
}
$input = $_POST['input'];

$db = new mysqli('localhost', 'root', 'election death double empire', 'mbed');

if (mysqli_connect_errno()){
	echo "connection error";
	exit;
}

$insert = "insert into " . $_POST['table']. "(". $_POST['columns']. ") values ";

$lines = explode("\r\n", $input);
$entry_parts;
$counter = 0;
$sex;
foreach($lines as $line){
	$entry_parts = explode("\t", $line);
	if ($entry_parts[2] == "F" || $entry_parts[2] == "f")
		$sex = "0";
	else if ($entry_parts[2] == "M" || $entry_parts[2] == "m")
		$sex = "1";
	else
		$sex = "NULL";
		
	$insert = $insert . " ('" . mysqli_real_escape_string($db, $entry_parts[0]) ."','". mysqli_real_escape_string($db, $entry_parts[1]) ."',". mysqli_real_escape_string($db, $sex ) . " ),";
}

// take off the last , as it will break the query
$insert = substr($insert, 0, strlen($insert) - 1);
//$insert = "select * from activities";
echo $insert . "</br></br>";
$result = $db->query($insert);

if ($result){
	echo "success " . $db->affected_rows . " row(s) affected.";
	for ($i = 0; $i < $result->num_rows; $i++){
		$row = $result->fetch_assoc();
		echo $row['activity'] . " " . htmlentities($row['p_activity']) . "</br>";
	}
}
else{
	echo "error - " . $db->error;
}

$db->close();
?>
</br>
<a href="/mbed.php">go back</a>
</p>
</body>
</html>
