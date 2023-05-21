import { API } from "../../utility/api";

const addAnswerAndProblemPercentageToStudent = async (userId, problemId, answer, score) => {
    try {
        const response = await API.put(`/users/${userId}/resultsProblem/${problemId}`, { answer, score});
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default addAnswerAndProblemPercentageToStudent;



