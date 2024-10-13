import apiFact from "./axiosInstanceFacture";
// import axios from "axios";
// export const api = axios.create({
//   baseURL: "http://localhost:8888/FACTURATION-SERVICE",
//   withCredentials: false,
//   headers: {
//     'Access-Control-Allow-Origin': '*',
//     'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
//     'Access-Control-Allow-Headers': '*',
//     'Content-Type': 'application/json'


//   }
// });


export const getClients = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get("/clients");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};


export const getClientById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/clients/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};
export const getClientBySociete = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/clients/bySociete/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const saveClient = (client) => {
  return apiFact.post("/clients", client);
};
export const updateClient = (client) => {
  console.log(client.id);
  return apiFact.put(`/clients/${client.id}`, client);
};
export const deleteClient = (client) => {
  console.log(client.id);
  return apiFact.delete(`/clients/${client.id}`);
};