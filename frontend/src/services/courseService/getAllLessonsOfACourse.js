import { API } from "../../utility/api";

const getAllLessonsOfACourse = async (courseId) => {
  console.log("course id: ", courseId)
    try {
        const response = await API.get(`/courses/${courseId}/lessons`)
        console.log(response.data)
        return response.data
      } catch (e) {
        console.log("error: ", e)
      }
}

export default getAllLessonsOfACourse;