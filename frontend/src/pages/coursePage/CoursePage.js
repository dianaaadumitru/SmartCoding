import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import './CoursePage.css';
import getCourseById from "services/courseService/getCourseById";
import { AiFillSignal, AiOutlineClockCircle } from "react-icons/ai";
import getAllLessonsOfACourse from "services/courseService/getAllLessonsOfACourse";
import NavBar from "pages/navBar/NavBar";

function CoursePage() {
    const navigate = useNavigate();
    const { courseId } = useParams();
    const [course, setCourse] = useState({
        id: courseId,
        name: '',
        description: '',
        difficulty: '',
        courseType: ''
    });
    const [lessons, setLessons] = useState([]);

    const getLessons = async () => {
        const result = await getAllLessonsOfACourse(courseId);
        setLessons(result)
    }

    const getCourse = async () => {
        const result = await getCourseById(courseId)
        setCourse(result)
    }

    useEffect(() => {
        getCourse()
        getLessons()
    }, [])

    const handleClick = async () => navigate('/signin')

    return (
        <div className="page-section-course">
            <NavBar />

            <div className="course-container">
                <div className="course-rectangle">
                    <div className="rectangle-header">
                        <h2 className="header-text">Course</h2>
                        <h2 className="course-name">{course.name}</h2>
                        <p className="course-description">{course.description}</p>
                        <div className="line"></div>

                        <div className="bottom-section">
                            <div className="left-content">
                                <AiFillSignal className="info-icon" />
                                <div>
                                    <p className="bottom-text">Skill level</p>
                                    <p className="bottom-text second-line">{course.difficulty}</p>
                                </div>
                            </div>
                            <button className="bottom-button" onClick={handleClick}>Start</button>
                            <div className="right-content">
                                <AiOutlineClockCircle className="info-icon" />
                                <div>
                                    <p className="bottom-text">Time to complete</p>
                                    <p className="bottom-text second-line">approx. 2 hours</p>
                                </div>
                            </div>
                        </div>

                        <div className="additional-rectangle">
                            <div className="additional-header">Syllabus</div>
                            <div className="additional-content">
                                <p className="additional-description">{lessons.length} lessons</p>
                            </div>
                            <div className="additional-line"></div>

                            {lessons.map((lesson, index) => (
                                <React.Fragment key={lesson.id}>
                                    <div className="additional-content" onClick={handleClick}>
                                        <div className="lesson-item">
                                            <div className="lesson-number">{index + 1}</div>
                                            <div className="lesson-info">
                                                <p className="lesson-name">{lesson.name}</p>
                                                <p className={`lesson-description ${index === lessons.length - 1 ? 'last-element' : ''}`}>{lesson.description}</p>
                                            </div>
                                        </div>
                                    </div>
                                    {index !== lessons.length - 1 && <div className="additional-line"></div>}

                                </React.Fragment>
                            ))}


                        </div>

                        <button className="bottom-button last-padding" onClick={handleClick}>Start</button>


                    </div>
                </div>
            </div>
        </div>
    );
}

export default CoursePage;

//     return (
//       <div className={'container'}>
//         <div className={'desktop1'}>
//           <span className={'text'}>
//             <span>Explore Developer Sign in</span>
//           </span>
//           <img
//             src="https://aheioqhobo.cloudimg.io/v7/_playground-bucket-v2.teleporthq.io_/fef9e243-ab12-47c0-b47e-4195344ce004/a0a12228-7466-4baf-b341-68d451fd67b9?org_if_sml=15134"
//             alt="Rectangle1128"
//             className={'rectangle1'}
//           />
//           <img
//             src="https://aheioqhobo.cloudimg.io/v7/_playground-bucket-v2.teleporthq.io_/fef9e243-ab12-47c0-b47e-4195344ce004/bfcc7ee7-33e9-46c9-a82f-808cd998b0e6?org_if_sml=12781"
//             alt="Rectangle3129"
//             className={'rectangle3'}
//           />
//           <span className={'text02'}>
//             <span>START</span>
//           </span>
//           <img
//             src="https://aheioqhobo.cloudimg.io/v7/_playground-bucket-v2.teleporthq.io_/fef9e243-ab12-47c0-b47e-4195344ce004/18c87007-6fc4-4afa-bd58-c342f211334e?org_if_sml=12401"
//             alt="Rectangle2131"
//             className={'rectangle2'}
//           />
//           <span className={'text04'}>
//             <span>Course</span>
//           </span>
//           <span className={'text06'}>
//             <span>COURSE TITLE</span>
//           </span>
//           <span className={'text08'}>
//             <span>
//               course description blablblablabalabalablalala lblaahal ksugf sh
//               fsukf s shkv s hdi shl iayhs fcogfuty
//               <span
//                 dangerouslySetInnerHTML={{
//                   __html: ' ',
//                 }}
//               />
//             </span>
//           </span>
//           <span className={'text10'}>
//             <span>
//               <span>skill level</span>
//               <br></br>
//               <span>BEGINNER</span>
//             </span>
//           </span>
//           <span className={'text15'}>
//             <span>
//               <span>Time to complete</span>
//               <br></br>
//               <span>approx 7h</span>
//             </span>
//           </span>
//           <img
//             src="https://aheioqhobo.cloudimg.io/v7/_playground-bucket-v2.teleporthq.io_/fef9e243-ab12-47c0-b47e-4195344ce004/7c84e241-d3a1-4cdd-84f1-02f42f5d14b6?org_if_sml=11919"
//             alt="Rectangle4137"
//             className={'rectangle4'}
//           />
//           <img
//             src="/playground_assets/line2138-8ht9.svg"
//             alt="Line2138"
//             className={'line2'}
//           />
//           <img
//             src="/playground_assets/line3139-wm1m.svg"
//             alt="Line3139"
//             className={'line3'}
//           />
//           <img
//             src="/playground_assets/line4140-w426.svg"
//             alt="Line4140"
//             className={'line4'}
//           />
//           <span className={'text20'}>
//             <span>
//               <span>Syllabus</span>
//               <br></br>
//               <span>7 lessons</span>
//             </span>
//           </span>
//           <img
//             src="https://aheioqhobo.cloudimg.io/v7/_playground-bucket-v2.teleporthq.io_/fef9e243-ab12-47c0-b47e-4195344ce004/517ae8a6-c58f-4b10-8f5e-3cbddb40a23f?org_if_sml=11002"
//             alt="Ellipse1142"
//             className={'ellipse1'}
//           />
//           <span className={'text25'}>1</span>
//           <span className={'text26'}>
//             <span>Lesson title</span>
//           </span>
//           <span className={'text28'}>
//             <span>lesson description</span>
//           </span>
//           <img
//             src="https://aheioqhobo.cloudimg.io/v7/_playground-bucket-v2.teleporthq.io_/fef9e243-ab12-47c0-b47e-4195344ce004/8812dd2e-37a9-4673-b1b2-b401ce17ed0c?org_if_sml=11002"
//             alt="Ellipse2146"
//             className={'ellipse2'}
//           />
//           <span className={'text30'}>1</span>
//           <span className={'text31'}>
//             <span>Lesson title</span>
//           </span>
//           <span className={'text33'}>
//             <span>lesson description</span>
//           </span>
//         </div>
//       </div>
//     )
//   }
  
//   export default CoursePage;