import { API } from "../../utility/api";

const getProblemScoreForAProblemSoledByUser = async (userId, problemId) => {
    try {
        const response = await API.get(`/users/${userId}/resultsProblem/${problemId}`);
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default getProblemScoreForAProblemSoledByUser;
