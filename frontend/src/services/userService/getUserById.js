import { API } from "../../utility/api";

const getUserById = async userId => {
  try {
    const response = await API.get(`/users/${userId}`)
    return response.data
  } catch (e) {
    console.log(e)
  }
}

export default getUserById