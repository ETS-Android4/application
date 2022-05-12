<?php include "../inc/dbinfo.inc"; ?>
<?php

/**
         *  Access database and retrieve announcement content, announcement title, and announcement date
         *  Return errorcode on failure 
         *  Data is encoded in json
        */
$success = true;
$content = "";
$title = "";
$date = "";
$errorCode = 0;

$conn = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
if(!$conn) {
$success = false;
$errorCode = mysqli_errno($conn);
}

mysqli_select_db($conn, DB_DATABASE);

$stmt = mysqli_query($conn, "SELECT * FROM ANNOUNCEMENT");

if(mysqli_stmt_errno($stmt)) {
$success = false;
$errorCode = $errorCode? $errorCode:mysqli_stmt_errno($stmt);
}


$announcement = array();
while($row = mysqli_fetch_array($stmt)) {
$content = $row[0];
$title = $row[1];
$date = $row[2];
array_push($announcement, array("announcementContent" => $content, "announcementTitle" => $title, "announcementDate" => $date));
}


$response = array();
$response["success"] = $success;
$response["errorCode"] = $errorCode;
$response["announcement"] = $announcement;

echo json_encode($response);
mysqli_close($conn);
?>