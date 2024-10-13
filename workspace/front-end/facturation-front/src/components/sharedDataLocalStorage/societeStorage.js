export const getSocieteData = () => {
    const societeData = localStorage.getItem("societeData");
    // return societeData ? societeData : null;
    return societeData ? JSON.parse(societeData) : null;
};


export const saveSocieteData = (societe) => {
    localStorage.setItem("societeData", JSON.stringify(societe));
};

export const societeIdStored = getSocieteData()?.id || null;

export const societeNom = getSocieteData()?.nom || null;

export const societeAbonnementId = getSocieteData()?.abonnementId || null;

export const societeAdminUserId = getSocieteData()?.adminUserId || null;

export const updateSocieteData = (updatedSocieteData) => {
    const societeData = getSocieteData();
    if (societeData) {
        const newSocieteData = { ...societeData, ...updatedSocieteData };
        localStorage.setItem("societeData", JSON.stringify(newSocieteData));
    }
};

export const deleteSocieteData = () => {
    localStorage.removeItem("societeData");
};