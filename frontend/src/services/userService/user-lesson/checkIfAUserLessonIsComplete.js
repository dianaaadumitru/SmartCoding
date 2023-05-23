import { API } from "../../../utility/api";

const checkIfAUserLessonIsCompleted = async (userId, lessonId, courseId) => {
    try {
        const response = await API.get(`/users/${userId}/lessons/completedLessons/${lessonId}?courseId=${courseId}`);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default checkIfAUserLessonIsCompleted;