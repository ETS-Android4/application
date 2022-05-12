<?php include "../inc/dbinfo.inc"; ?>
  
<?php
$courseTerm = "";
$courseCRN = "";

$conn = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
if(!$conn) {
error_log("Connection Error:". musqli_connect_errno());
error_log("Connection Error". mysqli_connect_error());

}

mysqli_select_db($conn, DB_DATABASE);


$courseTerm = $_GET["courseTerm"];
$courseCRN = $_GET["courseCRN"];

$result = mysqli_query($conn, "SELECT * FROM SEAT WHERE courseTerm='$courseTerm' AND courseCRN='$courseCRN'");

$response = array();

while($row = mysqli_fetch_array($result)) {
$response["seatCapacity"] = $row[2];
$response["seatActual"] = $row[3];
$response["seatRemaining"] = $row[4];
$response["waitlistCapacity"] = $row[5];
$response["waitlistActual"] = $row[6];
$response["waitlistRemaining"] = $row[7];
}

echo json_encode($response, JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>