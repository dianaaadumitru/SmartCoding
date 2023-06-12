import { API } from "../../utility/api";

const getCourseLessonByNoLesson = async (courseId, noLesson) => {
    try {
        const response = await API.get(`/courses/${courseId}/lessons/noLesson/${noLesson}`)
        return response.data
      } catch (e) {
        console.log("error: ", e)
      }
}

export default getCourseLessonByNoLesson;



