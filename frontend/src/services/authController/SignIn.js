import { API } from "../../utility/api";

const signIn = async (usernameOrEmail, password) => {
    try {
        const response = await API.post('auth/login', {
            usernameOrEmail,
            password
        })
        console.log(response)
        localStorage.setItem("token", response.data);
        if (response.status === 200) {
            localStorage.setItem("isLoggedIn", "true")
        } else {
            localStorage.setItem("isLoggedIn", "false")
        }
        return response;
    } catch (e) {
        throw new Error(e);
    }
}

export default signIn;