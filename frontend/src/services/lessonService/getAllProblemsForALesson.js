import { API } from "../../utility/api";

const getAllProblemsOfALesson = async (lessonId) => {
    try {
        const response = await API.get(`/lessons/${lessonId}/problems`)
        console.log(response.data)
        return response.data
      } catch (e) {
        console.log("error: ", e)
      }
}

export default getAllProblemsOfALesson;