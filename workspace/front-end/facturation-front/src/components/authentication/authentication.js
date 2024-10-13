import axios from "axios";

export const api = axios.create({
    baseURL: "http://localhost:8888/AUTHENTIFICATION-SERVICE",
    withCredentials: false,
    headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
        'Access-Control-Allow-Headers': '*',
        // 'Content-Type': 'application/json'
        'Content-Type': 'application/x-www-form-urlencoded'
    }
});

export default function loginUser(email, password) {
    const id = 123
    const user = { email, password, id };
    return new Promise(async (resolve, reject) => {
        try {
            console.log("user repo ", user)
            const response = await api.post("/login", user);
            resolve(response);
        } catch (error) {
            reject(error);
        }
    });
};
