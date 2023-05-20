import { API } from "../../utility/api";

const getAllProblemsByDifficulties = async (difficulties) => {
  try {
    const difficultiesString = difficulties.join(',');
    const response = await API.get('/problems/difficulty', { params: { difficulties: difficultiesString } });
    console.log(response.data);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};

export default getAllProblemsByDifficulties;
