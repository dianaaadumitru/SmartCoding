import { API } from "../../utility/api";

const getLessonById = async lessonId => {
  try {
    const response = await API.get(`/lessons/${lessonId}`)
    console.log(response.data)
    return response.data
  } catch (e) {
    console.log(e)
  }
}

export default getLessonById