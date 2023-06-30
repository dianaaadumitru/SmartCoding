import { API } from "../../utility/api";

const forgotPassword = async (email) => {
    try {
        const response = await API.put(`/users/forgotPassword?email=${email}`);
        console.log(response);
        return response;
    } catch (e) {
        console.log(e);
    }
};

export default forgotPassword;