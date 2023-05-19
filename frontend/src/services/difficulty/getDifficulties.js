import { API } from "../../utility/api";

const getDifficulties = async() => {
    try {
        const response = await API.get('/difficulties')
        console.log(response.data)
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
}

export default getDifficulties;