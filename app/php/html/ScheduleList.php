<?php include "../inc/dbinfo.inc"; ?>
  
<?php
$userID = "";

$conn = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
if(!$conn) {
error_log("Connection Error:". mysqli_connect_errno());
}

mysqli_select_db($conn, DB_DATABASE);

$userID = $_GET["userID"];

$result = mysqli_query($conn, "SELECT COURSE.courseTerm, COURSE.courseTitle, COURSE.courseTime, COURSE.courseDay, COURSE.courseLocation, COURSE.courseInstructor, COU
RSE.courseCRN,COURSE.courseCredit FROM COURSE,SCHEDULE,USER WHERE USER.userID = '$userID' AND USER.userID = SCHEDULE.userID AND SCHEDULE.courseCRN = COURSE.courseCRN");

$response = array();

while($row = mysqli_fetch_array($result)) {
array_push($response, array("courseTerm" => $row[0], "courseTitle" => $row[1], "courseTime" => $row[2], "courseDay" => $row[3], "courseLocation" => $row[4],
"courseInstructor" => $row[5], "courseCRN" => $row[6], "courseCredit" => $row[7]));
}

echo json_encode(array("response" => $response));
mysqli_close($conn);
?>