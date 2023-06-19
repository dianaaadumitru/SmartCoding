import { API } from "../../utility/api";

const runCode = async (code, valuesType, valuesToCheckCode, resultsToCheckCode, returnType, noParameters) => {
    console.log("Sending request with: code ", code, " valuesType ", valuesType, " valuesToCheckCode ", valuesToCheckCode, " resultsToCheckCode ", resultsToCheckCode, " returnType ", returnType, " noParameters ", noParameters)
    try {
        const response = await API.post('/runCode/run/results', { code, valuesType, valuesToCheckCode, resultsToCheckCode, returnType, noParameters });
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default runCode;
