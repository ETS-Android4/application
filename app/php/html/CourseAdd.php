<?php include "../inc/dbinfo.inc"; ?>
  
<?php

$userID = "";
$courseCRN = "";

$conn = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
if(!$conn) {
error_log("Connection Error:". mysqli_connect_errno());
}

mysqli_select_db($conn, DB_DATABASE);

$userID = $_POST["userID"];
$courseCRN = $_POST["courseCRN"];

$statement = mysqli_prepare($conn, "INSERT INTO SCHEDULE VALUES(?,?)");
mysqli_stmt_bind_param($statement, "ss", $userID, $courseCRN);
mysqli_stmt_execute($statement);

$response = array();
$response["success"] = true;

echo json_encode($response);
?>