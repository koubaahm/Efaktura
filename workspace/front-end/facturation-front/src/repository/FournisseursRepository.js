import apiFact from "./axiosInstanceFacture";


export const getFournisseurs = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get("/fournisseurs");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getFournisseurById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/fournisseurs/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getFournisseurBySociete = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/fournisseurs/bySociete/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const saveFournisseur = (fournisseur) => {
  return apiFact.post("/fournisseurs", fournisseur);
};
export const updateFournisseur = (fournisseur) => {
  return apiFact.put(`/fournisseurs/${fournisseur.id}`, fournisseur);
};
export const deleteFournisseur = (fournisseur) => {
  return apiFact.delete(`/fournisseurs/${fournisseur.id}`);
};
