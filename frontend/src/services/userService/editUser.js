import { API } from "../../utility/api";

const editUser = async (userId, firstName, lastName, username, email) => {
    try {
        const response = await API.put(`/users/${userId}`, {firstName, lastName, username, email});
        console.log(response.data);
        return response.data;
    } catch (e) {
        // throw new Error(e);
        console.log(e);
    }
};

export default editUser;
