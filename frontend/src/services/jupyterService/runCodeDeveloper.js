import { API } from "../../utility/api";

const runCodeDeveloper = async (code) => {
    try {
        const response = await API.post('/runCode/run/resultsDeveloper', { code });
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default runCodeDeveloper;