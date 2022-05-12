<?php include "../inc/dbinfo.inc"; ?>
  
<?php
$courseMajor = "";

$conn = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
if(!$conn) {
error_log("Connection Error:". mysqli_connect_errno());
error_log("Connection Error:". mysqli_connect_error());
}

mysqli_select_db($conn, DB_DATABASE);

$courseUniversity = $_GET["courseUniversity"];
$courseTerm = $_GET["courseTerm"];

$univ_field1 = "";
$univ_field2 = "";
if($courseUniversity == "Undergraduate"){
$univ_field1 = "GraduateSemester,UndergraduateSemester";
$univ_field2 = "UndergraduateSemester";
}

else if($courseUniversity == "Graduate"){
$univ_field1 = "GraduateSemester,UndergraduateSemester";
$univ_field2 = "GraduateSemester";
}

$result = mysqli_query($conn, "SELECT DISTINCT courseMajor FROM COURSE WHERE courseUniversity IN ('$univ_field1', '$univ_field2') AND courseTerm = '$courseTerm' ORDE
R BY courseMajor");
$response = array();

while($row = mysqli_fetch_array($result)) {
array_push($response, array("courseMajor" => $row[0]));
}

echo json_encode(array("response" => $response), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);
?>