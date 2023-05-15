import { API } from "../../utility/api";

const signIn = async(usernameOrEmail, password) => {
    try {
        const response = await API.post('auth/login', {
            usernameOrEmail,
            password
        })
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
}

export default signIn;