import { API } from "../../utility/api";

const signUp = async (firstName, lastName, username, email, password) => {
    try {
        console.log("from signin method: ", firstName, lastName, username, email, password);
        const response = await API.post('auth/signup', {
            firstName,
            lastName,
            username,
            email,
            password
        })
        console.log(response);
        return response;
    } catch (e) {
        console.log("error", e.response.data)
        throw new Error(e.response.data);
    }
}

export default signUp;