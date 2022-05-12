class Course
{
public $courseTerm = NULL;
public $courseMajor = NULL;
public $courseTitle = NULL;
public $courseCRN = NULL;
public $courseArea = NULL;
public $courseSection = NULL;
public $courseClass = NULL;
public $courseTime = NULL;
public $courseDay = NULL;
public $courseLocation = NULL;
public $courseInstructor = NULL;
public $courseUniversity = NULL;
public $courseCredit = NULL;
public $courseAttribute = NULL;

public function __construct($courseTerm="",$courseMajor="",$courseTitle="",$courseCRN="",$courseArea="",$courseSection="",$courseClass="",$courseTime="",$courseDay="
",$courseLocation="",$courseInstructor="",$courseUniversity="",$courseCredit="",$courseAttribute="")
{
$this->courseTerm = $courseTerm;
$this->courseMajor = $courseMajor;
$this->courseTitle = $courseTitle;
$this->courseCRN = $courseCRN;
$this->courseArea = $courseArea;
$this->courseSection = $courseSection;
$this->courseClass = $courseClass;
$this->courseTime = $courseTime;
$this->courseDay = $courseDay;
$this->courseLocation = $courseLocation;
$this->courseInstructor = $courseInstructor;
$this->courseUniversity = $courseUniversity;
$this->courseCredit = $courseCredit;
$this->courseAttribute = $courseAttribute;
}
}
?>