<?php include "../inc/dbinfo.inc"; ?>
  
<?php
header("Content-Type: text/html; charset = UTF-8");
$conn = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
mysqli_select_db($conn, DB_DATABASE);

$userID = $_GET["userID"];

$result = mysqli_query($conn, "SELECT COURSE.courseTerm, COURSE.courseTitle, COURSE.courseCRN, COURSE.courseSection, COURSE.courseTime, COURSE.courseDay, COURSE.cour
seCredit, COURSE.courseAttribute FROM SCHEDULE,COURSE WHERE SCHEDULE.courseCRN IN (SELECT SCHEDULE.courseCRN FROM SCHEDULE WHERE SCHEDULE.userID = '$userID') AND SCHEDULE.co
urseCRN = COURSE.courseCRN GROUP BY SCHEDULE.courseCRN");

$response = array();
while($row = mysqli_fetch_array($result)) {
array_push($response, array("courseTerm" => $row[0], "courseTitle" => $row[1], "courseCRN" => $row[2], "courseSection" => $row[3], "courseTime" => $row[4], "
courseDay" => $row[5],"courseCredit" => $row[6], "courseAttribute" => $row[7]));
}

echo json_encode(array("response" => $response), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);
?>