import { API } from "../../utility/api";

const getUsersSolvedProblems = async (userId) => {
    try {
        const response = await API.get(`/users/${userId}/resultsProblems`);
        console.log("problems ", response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default getUsersSolvedProblems;
