import React, { useEffect } from 'react'
import { useNavigate } from "react-router-dom";
import { disableCnx } from '../sharedDataLocalStorage/cnxStateStorage';
import { deleteReload, getReload } from '../sharedDataLocalStorage/ReloadStorage';

function Logout() {

    const navigate = useNavigate();

    useEffect(() => {
        disableCnx();
        const reloadFlag = getReload();
        if (reloadFlag) {
            deleteReload();
            setTimeout(() => {
                window.location.reload(false);
            }, 100);
        }
        else {
            setTimeout(() => {
                navigate("/");
            }, 100);

        }
    }, []);

}

export default Logout