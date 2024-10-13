export const saveReload = () => {
    localStorage.setItem("reload", "disable");
};

export const deleteReload = () => {
    localStorage.removeItem("reload");
};

export const getReload = () => {
    const reload = localStorage.getItem("reload");
    return reload ? reload : null;
};