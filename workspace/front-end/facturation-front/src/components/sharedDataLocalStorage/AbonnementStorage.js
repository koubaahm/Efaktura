export const getAbonnementData = () => {
    const abonnementData = localStorage.getItem("abonnementData");
    return abonnementData ? JSON.parse(abonnementData) : null;
    // return abonnementData ? abonnementData : null;
};

export const saveAbonnementData = (abonnement) => {
    localStorage.setItem("abonnementData", JSON.stringify(abonnement));

};

export const abonnementIdStored = getAbonnementData()?.id || null;

export const abonnementActive = getAbonnementData()?.active || null;

export const abonnementNonSociete = getAbonnementData()?.nonSociete || null;

export const abonnementSocieteId = getAbonnementData()?.societeId || null;


export const updateAbonnementData = (updatedAbonnementData) => {
    const abonnementData = getAbonnementData();
    if (abonnementData) {
        const newAbonnementData = { ...abonnementData, ...updatedAbonnementData };
        localStorage.setItem("abonnementData", JSON.stringify(newAbonnementData));
    }
};
export const deleteAbonnementData = () => {
    localStorage.removeItem("abonnementData");
};