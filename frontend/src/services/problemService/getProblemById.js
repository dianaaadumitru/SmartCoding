import { API } from "../../utility/api";

const getProblemById = async problemId => {
  try {
    const response = await API.get(`/problems/${problemId}`)
    console.log(response.data)
    return response.data
  } catch (e) {
    console.log(e)
  }
}

export default getProblemById;