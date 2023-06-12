import { API } from "../../utility/api";

const computeTimeNeededToFinishCourse
= async courseId => {
  try {
    const response = await API.get(`/courses/${courseId}/expectedTime`)
    // console.log(response.data)
    return response.data
  } catch (e) {
    console.log(e)
  }
}

export default computeTimeNeededToFinishCourse
