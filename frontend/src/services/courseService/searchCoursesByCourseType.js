import { API } from "../../utility/api";

const searchCoursesByCourseType = async (courseType) => {
  try {
    const response = await API.get(`/courses/difficulty?${courseType}`);
    console.log(response.data);
    return response.data;
  } catch (e) {
    console.log(e);
  }
};

export default searchCoursesByCourseType;