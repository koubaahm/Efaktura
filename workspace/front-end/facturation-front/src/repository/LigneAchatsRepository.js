import apiFact from "./axiosInstanceFacture";

export const getLigneAchats = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get("/ligneAchats");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getLigneAchatById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/ligneAchats/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getAllByProduit = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/ligneAchats/getAllByProduit/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getLignesAchatsByProduit = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/ligneAchats/byProduit/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const saveLigneAchat = (ligneAchat) => {
  return apiFact.post("/ligneAchats", ligneAchat);
};

export const saveListLigneAchat = (ligneAchat) => {
  return apiFact.post("/ligneAchats/saveList", ligneAchat);
};

export const updateLigneAchat = (ligneAchat) => {
  return apiFact.put(`/ligneAchats/${ligneAchat.id}`, ligneAchat);
};

export const updateListLigneAchat = (ligneAchat) => {
  return apiFact.put(`/ligneAchats/updateList`, ligneAchat);
};

export const deleteLigneAchat = (ligneAchat) => {
  return apiFact.delete(`/ligneAchats/${ligneAchat.id}`);
};