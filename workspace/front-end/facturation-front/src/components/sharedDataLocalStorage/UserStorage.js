export const getUserData = () => {
    const userData = localStorage.getItem("userData");
    return userData ? JSON.parse(userData) : null;
};

export const saveUserData = (user) => {
    localStorage.setItem("userData", JSON.stringify(user));

};

// export const getUserId = () => {
//     const userData = getUserData();
//     return userData ? userData.id : null;
// };

// export const getUserRoles = () => {
//     const userData = getUserData();
//     return userData ? userData.rolesNom : null;
// };

export const userIdStored = getUserData()?.id || null;

export const userRolesStored = getUserData()?.rolesNom || null;

export const updateUserData = (updatedUserData) => {
    const userData = getUserData();
    if (userData) {
        const newUserData = { ...userData, ...updatedUserData };
        localStorage.setItem("userData", JSON.stringify(newUserData));
    }
};

export const deleteUserData = () => {
    localStorage.removeItem("userData");
};