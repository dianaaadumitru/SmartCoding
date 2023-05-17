import { API } from "../../utility/api";

const getTopCourses = async() => {
    try {
        const response = await API.get('/users/topCourses')
        console.log(response.data)
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
}

export default getTopCourses;