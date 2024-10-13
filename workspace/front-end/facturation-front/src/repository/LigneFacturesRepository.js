import apiFact from "./axiosInstanceFacture";

export const getLigneFactures = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get("/ligneFactures");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getLigneFactureById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/ligneFactures/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getLigneFactureIdFacture = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/ligneFactures/byIdFacture/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const saveListLigneFacture = (ligneFacture) => {
  console.log("ligneFacture ", ligneFacture)
  return apiFact.post("/ligneFactures/saveList", ligneFacture);
};
export const saveLigneFacture = (ligneFacture) => {
  return apiFact.post("/ligneFactures", ligneFacture);
};
export const updateLigneFacture = (ligneFacture) => {

  return apiFact.put(`/ligneFactures/${ligneFacture.id}`, ligneFacture);
};
export const updateListLigneFacture = (ligneFacture) => {

  return apiFact.put(`/ligneFactures/updateList`, ligneFacture);
};
export const deleteLigneFacture = (ligneFacture) => {

  return apiFact.delete(`/ligneFactures/${ligneFacture.id}`);
};