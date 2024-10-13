import axios from "axios";

export const apiFact = axios.create({
  baseURL: "http://localhost:8888/FACTURATION-SERVICE",
  withCredentials: false,
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
    'Access-Control-Allow-Headers': '*',
    'Content-Type': 'application/json'

  }
});

export const getTvas = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get("/tvas");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getTvaById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/tvas/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};
export const getTvaBySociete = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/tvas/bySociete/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const saveTva = (tva) => {
  return apiFact.post("/tvas", tva);
};
export const updateTva = (tva) => {
  return apiFact.put(`/tvas/${tva.id}`, tva);
};
export const deleteTva = (tva) => {
  return apiFact.delete(`/tvas/${tva.id}`);
};