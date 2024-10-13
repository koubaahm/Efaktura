import apiAuth from "./axiosInstanceAuthentication";



export const getUsers = () => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiAuth.get("/users");
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getUserById = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiAuth.get(`/users/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};

export const getUserbySociete = (id) => {
  return new Promise(async (resolve, reject) => {
    try {
      const response = await apiAuth.get(`/users/bySociete/${id}`);
      resolve(response);
    } catch (error) {
      reject(error);
    }
  });
};
export const saveUser = (user) => {
  return apiAuth.post("/users", user);
};
export const saveAdmin = (user) => {
  return apiAuth.post("/users/saveAdmin", user);
};
export const saveOperateur = (user) => {
  console.log("user save repo ", user)
  return apiAuth.post("/users/saveOperateur", user);
};
export const updateUser = (user) => {
  console.log(user.id);
  return apiAuth.put(`/users/${user.id}`, user);
};
export const deleteUser = (user) => {
  console.log(user.id);
  return apiAuth.delete(`/users/${user.id}`);
};