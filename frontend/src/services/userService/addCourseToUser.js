import { API } from "../../utility/api";

const addCourseToUser = async (userId, courseId) => {
    try {
        const response = await API.put(`/users/${userId}/courses/${courseId}`);
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default addCourseToUser;
