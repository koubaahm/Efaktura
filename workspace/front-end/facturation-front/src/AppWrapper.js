import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { disableCnx, getCnxData } from './components/sharedDataLocalStorage/cnxStateStorage';
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";



function AppWrapper() {
    return (
        <BrowserRouter>
            <App />
        </BrowserRouter>
    );
}

function App() {
    // Use the useNavigate hook here
    const navigate = useNavigate();

    useEffect(() => {
        const cnx = getCnxData();
        if (!cnx) {
            navigate('/');
        }
    }, [navigate]);

    // Rest of your component code
}

export default AppWrapper;