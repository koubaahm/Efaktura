import apiFact from "./axiosInstanceFacture";

// axios.defaults.headers.common['Authorization'] = 'Bearer your-token';
// axios.defaults.headers.common['Content-Type'] = 'application/json';

export const getProducts = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get("/produits");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getProductById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/produits/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getProductBySociete = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiFact.get(`/produits/bySociete/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const saveProduct = (produit) => {
  return apiFact.post("/produits", produit);
};

export const updateProduct = (produit) => {
  console.log(produit.id);
  return apiFact.put(`/produits/${produit.id}`, produit);
};

export const deleteProduct = (produit) => {
  console.log(produit.id);
  return apiFact.delete(`/produits/${produit.id}`);
};

