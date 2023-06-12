import { API } from "../../utility/api";

const getRecommendedCourses = async courseId => {
  try {
    const response = await API.get(`/courses/recommendedCourses/${courseId}`)
    return response.data
  } catch (e) {
    console.log(e)
  }
}

export default getRecommendedCourses