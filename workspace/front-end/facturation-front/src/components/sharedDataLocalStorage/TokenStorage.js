export const saveToken = (token) => {
    localStorage.setItem("factureToken", token);
};

export const getToken = () => {
    const token = localStorage.getItem("factureToken");
    return token ? token : null;
};

export const deleteToken = () => {
    localStorage.removeItem("factureToken");
};