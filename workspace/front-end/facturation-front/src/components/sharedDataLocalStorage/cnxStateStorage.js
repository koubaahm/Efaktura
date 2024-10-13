export const activeCnx = () => {
    localStorage.setItem("activeCnx", "active");
};

export const getCnxData = () => {
    const cnxData = localStorage.getItem("activeCnx");
    return cnxData ? cnxData : null;
};

export const disableCnx = () => {
    localStorage.removeItem("factureToken");
    localStorage.removeItem("activeCnx");
    localStorage.removeItem("abonnementData");
    localStorage.removeItem("societeData");
    localStorage.removeItem("userData");
    // localStorage.removeItem("reload");
};