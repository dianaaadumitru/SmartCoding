import { API } from "../../utility/api";

const runCode = async (code, valuesType, valuesToCheckCode, resultsToCheckCode) => {
    try {
        const response = await API.post('/runCode/run/results', { code, valuesType, valuesToCheckCode, resultsToCheckCode });
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default runCode;
