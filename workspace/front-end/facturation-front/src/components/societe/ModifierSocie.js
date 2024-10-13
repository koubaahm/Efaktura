import React, { useState, useEffect } from "react";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Link, } from "react-router-dom";
import { userIdStored } from "../sharedDataLocalStorage/UserStorage";
import { useNavigate } from "react-router-dom";
import { abonnementIdStored } from "../sharedDataLocalStorage/AbonnementStorage";
import { getSocieteById, saveSociete, updateSociete } from "../../repository/societeRepository";
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { getReload, saveReload } from "../sharedDataLocalStorage/ReloadStorage";
import { getSocieteData, saveSocieteData, societeIdStored, societeNom } from "../sharedDataLocalStorage/societeStorage";


function ModifierSocie() {

    useEffect(() => {
        const cnx = getCnxData();
        if (!cnx) {
            disableCnx()
            setTimeout(() => {
                window.location.href = "/";
            }, 100);
        }
    }, []);

    const [nom, setNom] = useState(societeNom);
    const [email, setEmail] = useState("");
    const [adresse, setAdresse] = useState("");
    const [telephone, setTelephone] = useState();
    const [matriculeFiscal, setMatriculeFiscal] = useState();
    const [abonnementId, setAbonnementId] = useState(abonnementIdStored);
    const [adminUserId, setAdminUserId] = useState(userIdStored);
    const [id, setId] = useState(societeIdStored);
    const navigate = useNavigate();

    useEffect(() => {
        const reloadFlag = getReload();
        if (!reloadFlag) {
            localStorage.setItem("reload", "disable");
            saveReload();
            window.location.reload(false);
        }
    }, []);

    useEffect(() => {
        handleGetsocieteById()
    }, []);

    const handleGetsocieteById = async () => {
        try {
            const resp = await getSocieteById(id);
            setNom(resp.data.nom);
            setEmail(resp.data.email);
            setAdresse(resp.data.adresse);
            setTelephone(resp.data.telephone);
            setMatriculeFiscal(resp.data.matriculeFiscal)

        } catch (error) {
            console.error("Error retrieving users:", error);
        }
    };



    const handleSubmit = (event) => {
        event.preventDefault();
        const user = { id, nom, email, adresse, telephone, matriculeFiscal, abonnementId, adminUserId };
        updateSociete(user).then((resp) => {
            saveSocieteData(resp.data)
            localStorage.setItem('societeData', JSON.stringify(resp.data));
            setTimeout(() => {
                navigate("/Société");
            }, 700);
            toast.success(JSON.stringify(resp.data), {
                position: "top-center",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        })
            .catch((error) => {
                const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement d'utilisateur";
                toast.error(errorMessage, {
                    position: "top-center",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                });
            });
    };
    return (
        <div className="container">
            <ToastContainer />
            <br />
            {/* <section className="section register min-vh-100 d-flex flex-column align-items-center justify-content-center py-4"> */}
            <div className="">
                <div className="row justify-content-center">
                    {/* <div className="col-7">
                        <img src={process.env.PUBLIC_URL + '/images/societe.png'} alt="Image Description" height={600} />
                    </div> */}
                    <div className="col-md-5 d-flex flex-column align-items-center justify-content-center">
                        <div className="card mb-3">
                            <div className="card-body">
                                <div className="pt-4 pb-2">
                                    <h5 className="card-title text-center pb-0 fs-4">Modifier votre société</h5>
                                    <p className="text-center small">Saisissez les données de votre société</p>
                                </div>
                                <form className="row g-3 needs-validation" onSubmit={handleSubmit} >
                                    <div className="col-6">
                                        <label htmlFor="yourName" className="form-label">Nom du société</label>
                                        <input type="text" name="name" className="form-control" id="yourName" required value={nom}
                                            onChange={(e) => setNom(e.target.value)} />
                                    </div>
                                    <div className="col-6">
                                        <label htmlFor="yourEmail" className="form-label"> Email </label>
                                        <input type="email" name="email" className="form-control" id="yourEmail" required value={email}
                                            onChange={(e) => setEmail(e.target.value)} />
                                    </div>
                                    <div className="col-12">
                                        <label htmlFor="adresse" className="form-label">Adresse</label>
                                        <input type="text" name="adresse" className="form-control" id="adresse" required value={adresse}
                                            onChange={(e) => setAdresse(e.target.value)} />
                                    </div>
                                    <div className="col-12">
                                        <label htmlFor="yourTelephne" className="form-label">Téléphone</label>
                                        <input type="text" name="telephone" className="form-control" id="yourTelephne" required value={telephone}
                                            onChange={(e) => setTelephone(e.target.value)} />
                                    </div>
                                    <div className="col-12">
                                        <label htmlFor="matriculeFiscal" className="form-label">Matricule Fiscal</label>
                                        <input type="text" name="matriculeFiscal" className="form-control" id="matriculeFiscal" required value={matriculeFiscal}
                                            onChange={(e) => setMatriculeFiscal(e.target.value)} />
                                    </div>
                                    <div className="col-12">
                                        <button className="btn btn-primary w-100" type="submit">Modifier</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            {/* </section> */}
        </div>
    );
}

export default ModifierSocie