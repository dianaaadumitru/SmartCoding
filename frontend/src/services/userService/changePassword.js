import { API } from "../../utility/api";

const changePassword = async (userId, oldPassword, newPassword) => {
    try {
        const response = await API.put(`/users/${userId}/changePassword`, {oldPassword, newPassword});
        console.log(response);
        return response;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default changePassword;