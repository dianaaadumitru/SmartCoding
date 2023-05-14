import { API } from "../../utility/api";

const signIn = async(usernameOrEmail, password) => {
    try {
        console.log("from signin method: ", usernameOrEmail, password);
        const response = await API.post('auth/login', {
            usernameOrEmail,
            password
        })
        console.log(response.data);
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
}

export default signIn;