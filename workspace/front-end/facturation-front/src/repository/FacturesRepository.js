import apiFact from "./axiosInstanceFacture";


export const getFactures = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get("/factures");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getFactureById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/factures/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getFactureBySociete = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/factures/bySociete/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getNumeroNouvelleFacture = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/factures/lastNumeroFacture/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const saveFacture = (facture) => {
  return apiFact.post("/factures", facture);
};
export const updateFacture = (facture) => {
  return apiFact.put(`/factures/${facture.id}`, facture);
};
export const deleteFacture = (facture) => {
  return apiFact.delete(`/factures/${facture.id}`);
};
