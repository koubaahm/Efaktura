import apiAuth from "./axiosInstanceAuthentication";

export const getSocietes = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiAuth.get("/societes");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getSocieteById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiAuth.get(`/societes/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const saveSociete = (user) => {
  return apiAuth.post("/societes", user);
};

export const updateSociete = (user) => {
  console.log(user.id);
  return apiAuth.put(`/societes/${user.id}`, user);
};
export const deleteSociete = (user) => {
  console.log(user.id);
  return apiAuth.delete(`/societes/${user.id}`);
};