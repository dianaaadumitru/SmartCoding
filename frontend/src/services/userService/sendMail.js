import { API } from "../../utility/api";

const sendMail = async (recipient, msgBody, subject) => {
    try {
        const response = await API.post(`/emails/sendMail`, {recipient, msgBody, subject});
        console.log(response);
        return response;
    } catch (e) {
        console.log(e);
    }
};

export default sendMail;
