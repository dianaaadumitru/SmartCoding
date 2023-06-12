import getCourseLessonByNoLesson from "services/courseService/getCourseLessonByNoLesson";

const { default: React } = require("react");
const { useState, useEffect } = require("react");
const { AiOutlineLock } = require("react-icons/ai");
const { default: checkIfAUserLessonIsCompleted } = require("services/userService/user-lesson/checkIfAUserLessonIsComplete");

const LessonItem = ({ lesson, userId, courseId, isEnrolled, index, lessons, handleClickLesson }) => {
  const [isLessonCompleted, setIsLessonCompleted] = useState(false);
  const [isPreviousLessonCompleted, setIsPreviousLessonCompleted] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchLessonCompletionStatus = async () => {
      try {
        const lessonCompleted = await checkIfAUserLessonIsCompleted(userId, lesson.id, courseId);
        setIsLessonCompleted(Boolean(lessonCompleted));
      } catch (error) {
        console.error('Error:', error);
      }
    };

    const fetchPreviousLessonCompletionStatus = async () => {
      try {
        if (lesson.noLesson !== 1) {
          const result = await getCourseLessonByNoLesson(courseId, lesson.noLesson - 1);
          const lessonCompleted = await checkIfAUserLessonIsCompleted(userId, result.id, courseId);
          setIsPreviousLessonCompleted(Boolean(lessonCompleted));
        } else {
          setIsPreviousLessonCompleted(true);

        }
      } catch (error) {
        console.error('Error:', error);
      }
    };

    const fetchLessonStatus = async () => {
      try {
        await fetchLessonCompletionStatus();
        await fetchPreviousLessonCompletionStatus();
        setIsLoading(false);
      } catch (error) {
        console.error('Error:', error);
      }
    };

    fetchLessonStatus();
  }, [lesson.id, courseId, userId]);

  if (isLoading) {
    // Show a loading indicator or placeholder while fetching the lesson status
    return <div>Loading...</div>;
  }


  return (
    <div
      className={`additional-content ${(!isEnrolled  || (isEnrolled && !isLessonCompleted && !isPreviousLessonCompleted)) ? 'disabled' : ''
        }`}
      onClick={
        (!isEnrolled  || (isEnrolled && !isLessonCompleted && !isPreviousLessonCompleted)) ? null : () =>
          handleClickLesson(lesson.id)
      }
    >
      <div className="lesson-item">
        <div className="lesson-addons">
          {(isEnrolled && isLessonCompleted) ? (
            <input type="checkbox" readOnly checked className="lesson-checkbox" />
          ) : (
            <input type="checkbox" readOnly={true} checked={false} className="lesson-checkbox" />
          )}

          {(!isEnrolled  || (isEnrolled && !isLessonCompleted && !isPreviousLessonCompleted)) ? (
            <>
              {console.log("first condition: ", !isEnrolled)}
              {/* {console.log("second condition: ", (isEnrolled &&  lesson.noLesson !== 1))} */}
              {console.log("third condition: ", (isEnrolled && !isLessonCompleted && !isPreviousLessonCompleted))}
              {console.log("final result: " + (!isEnrolled || (isEnrolled && (!isLessonCompleted || lesson.noLesson !== 1)) || (isEnrolled && !isLessonCompleted && !isPreviousLessonCompleted)))}
              {console.log("isEnrolled:", isEnrolled, "isCompleted:", isLessonCompleted, "previous completed:", isPreviousLessonCompleted, " lesson number ", lesson.noLesson)}
              <AiOutlineLock className="lesson-icon" />
            </>
          ) : null}



        </div>
        <div className="lesson-number">{lesson.noLesson}</div>
        <div className="lesson-info">
          <p className="lesson-name">{lesson.name}</p>
          <p className={`lesson-description ${index === lessons.length - 1 ? 'last-element' : ''}`}>
            {lesson.description}
          </p>
        </div>
      </div>
    </div>
  );
};


export default LessonItem;