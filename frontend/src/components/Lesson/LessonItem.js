const { default: React } = require("react");
const { useState, useEffect } = require("react");
const { AiOutlineLock } = require("react-icons/ai");
const { default: checkIfAUserLessonIsCompleted } = require("services/userService/user-lesson/checkIfAUserLessonIsComplete");

const LessonItem = ({ lesson, userId, courseId, isEnrolled, index, lessons, handleClickLesson }) => {
    const [isLessonCompleted, setIsLessonCompleted] = useState(false);
    
    useEffect(() => {
      const fetchLessonCompletionStatus = async () => {
        try {
          const lessonCompleted = await checkIfAUserLessonIsCompleted(userId, lesson.id, courseId);
          setIsLessonCompleted(Boolean(lessonCompleted));
        } catch (error) {
          console.error('Error:', error);
          
        }
      };
  
      fetchLessonCompletionStatus();
    }, [lesson.id]);
  
    return (
      <div
        className={`additional-content ${
          !isEnrolled || (isEnrolled && (!isLessonCompleted && lesson.noLesson !== 1)) ? 'disabled' : ''
        }`}
        onClick={
          !isEnrolled || (isEnrolled && (!isLessonCompleted && lesson.noLesson !== 1)) ? null : () =>
            handleClickLesson(lesson.id)
        }
      >
        <div className="lesson-item">
          <div className="lesson-addons">
          {(isEnrolled && isLessonCompleted) ? (
              <input type="checkbox" readOnly checked className="lesson-checkbox" />
            ) : (
                <input type="checkbox" readOnly className="lesson-checkbox" />
            )}
            
            {!isEnrolled || (isEnrolled && (!isLessonCompleted && lesson.noLesson !== 1)) ? (
              <>
              {console.log("isEnrolled: ", {isEnrolled}, " isCompleted: ", {isLessonCompleted}, " no lesson: ", lesson.noLesson)}
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