import { API } from "../../utility/api";

const getAllProblems = async() => {
    try {
        const response = await API.get('/problems')
        console.log(response.data)
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
}

export default getAllProblems;