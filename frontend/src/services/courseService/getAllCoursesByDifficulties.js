import { API } from "../../utility/api";

const getAllCoursesByDifficulties = async (difficulties) => {
  try {
    const difficultiesString = difficulties.join(','); // Convert the array to a comma-separated string
    const response = await API.get('/courses/difficulty', { params: { difficulties: difficultiesString } });
    console.log(response.data);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};

export default getAllCoursesByDifficulties;
