import { API } from "../../utility/api";

const Logout = async() => {
    try {
        const response = await API.post('/auth/logout')
        console.log(response);
        return response;
    } catch (e) {
        console.log(e);
    }
}

export default Logout;