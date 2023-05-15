import { API } from "../../utility/api";

const getAllCourses = async() => {
    try {
        const response = await API.get('/courses')
        console.log(response.data)
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
}

export default getAllCourses;