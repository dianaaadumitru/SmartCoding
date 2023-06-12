import { API } from "../../../utility/api";

const isUserEnrolledToCourse = async (userId, courseId) => {
    try {
        const response = await API.get(`/users/${userId}/courses/${courseId}/isEnrolled`);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default isUserEnrolledToCourse;