import { API } from "../../../utility/api";

const addEnrolledLessonToUser = async (userId, lessonId, courseId) => {
    try {
        const response = await API.post(`/users/${userId}/lessons/addLesson/${lessonId}`, {param: {courseId}});
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default addEnrolledLessonToUser;
