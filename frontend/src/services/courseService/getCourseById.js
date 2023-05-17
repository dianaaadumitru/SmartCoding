import { API } from "../../utility/api";

const getCourseById = async courseId => {
  try {
    const response = await API.get(`/courses/${courseId}`)
    console.log(response.data)
    return response.data
  } catch (e) {
    console.log(e)
  }
}

export default getCourseById