import { API } from "../../../utility/api";

const checkIfCourseIsCompleted = async (userId, courseId, length) => {
    try {
        const response = await API.get(`/users/${userId}/courses/completedCourse/${courseId}?length=${length}`);
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default checkIfCourseIsCompleted;

