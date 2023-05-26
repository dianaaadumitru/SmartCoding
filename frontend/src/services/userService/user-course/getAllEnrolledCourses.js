import { API } from "../../../utility/api";

const geAllEnrolledCourses = async (userId) => {
    try {
        const response = await API.get(`/users/${userId}/courses`);
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default geAllEnrolledCourses;
