import apiAuth from "./axiosInstanceAuthentication";


export const getAbonnements = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiAuth.get("/abonnements");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getAbonnementById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiAuth.get(`/abonnements/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};
export const getAbonnementBySociete = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiAuth.get(`/abonnements/bySociete/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const createAbonnement = (abonnement) => {
  return apiAuth.post("/abonnements", abonnement);
};

export const suspendreAbonnement = (id) => {
  return apiAuth.post(`/abonnements/suspendre/${id}`);
};

//2 jours
export const activerAbonnement = (id) => {
  return apiAuth.post(`/abonnements/activation/${id}`);
};

// 1 ans
export const renouvellerAbonnement = (id) => {
  return apiAuth.post(`/abonnements/renouvellement/${id}`);
};
export const updateAbonnement = (abonnement) => {
  console.log(abonnement.id);
  return apiAuth.put(`/abonnements/${abonnement.id}`, abonnement);
};

export const deleteAbonnement = (abonnement) => {
  console.log(abonnement.id);
  return apiAuth.delete(`/abonnements/${abonnement.id}`);
};