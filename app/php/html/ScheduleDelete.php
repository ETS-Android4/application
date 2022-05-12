<?php include "../inc/dbinfo.inc"; ?>
  
<?php
$conn = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
mysqli_select_db($conn, DB_DATABASE);

$userID = $_POST["userID"];
$courseCRN = $_POST["courseCRN"];

$statement = mysqli_prepare($conn, "DELETE FROM SCHEDULE WHERE userID = '$userID' AND courseCRN = '$courseCRN'");
mysqli_stmt_bind_param($statement, "ss", $userID, $courseCRN);
mysqli_stmt_execute($statement);

$response = array();
$response["success"] = true;

echo json_encode($response);
mysqli_close($conn);

?>