<?php include "../inc/dbinfo.inc"; ?>
<?php include "Course.php"; ?>

<?php use data\Course;?>

<?php
$course = new Course();
$courseMajor = "";
$courseTitle = "";
$courseCRN = "";
$courseArea = "";
$courseSection = "";
$courseClass = "";
$courseTime = "";
$courseDay = "";
$courseLocation = "";
$courseInstructor = "";
$courseUniversity = "";
$courseCredit = "";
$courseAttribute = "";


$conn = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
if(!$conn) {
error_log("Connection Error:". mysqli_connect_errno());
error_log("Connection Error:". mysqli_connect_error());
}

mysqli_select_db($conn, DB_DATABASE);

$courseUniversity = $_GET["courseUniversity"];
$courseTerm = $_GET["courseTerm"];
$courseMajor = $_GET["courseMajor"];
$courseArea = $_GET["courseArea"];

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

$result = mysqli_query($conn, "SELECT * FROM COURSE WHERE courseUniversity IN ('$univ_field1', '$univ_field2') AND courseTerm = '$courseTerm' AND courseMajor = '$cou
rseMajor' AND courseArea = '$courseArea'");
$response = array();

while($row = mysqli_fetch_array($result)) {
array_push($response, array("courseTerm" => $row[0], "courseMajor" => $row[1], "courseTitle" => $row[2], "courseCRN" => $row[3], "courseArea" => $row[4], "co
urseSection" => $row[5], "courseClass" => $row[6], "courseTime" => $row[7], "courseDay" => $row[8], "courseLocation" => $row[9], "courseInstructor" => $row[10], "courseUnive
rsity" => $row[11], "courseCredit" => $row[12], "courseAttribute" => $row[13]));
}


foreach ($response as &$row) {
$courseTerm = $row["courseTerm"];
$courseCRN = $row["courseCRN"];

$result = mysqli_query($conn, "SELECT * FROM SEAT WHERE courseTerm = '$courseTerm' AND courseCRN = '$courseCRN'");
$seatRow = mysqli_fetch_array($result);

$row["seatCapacity"] = $seatRow[2];
$row["seatActual"] = $seatRow[3];
$row["seatRemaining"] = $seatRow[4];
$row["waitlistCapacity"] = $seatRow[5];
$row["waitlistActual"] = $seatRow[6];
$row["waitlistRemaining"] = $seatRow[7];

}


echo json_encode(array("response" => $response), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>