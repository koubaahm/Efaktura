import React, { useState, useEffect } from "react";
import { saveOperateur } from "../../repository/UsersRepository";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function AjouterOperateur() {

    useEffect(() => {
        const cnx = getCnxData();
        if (!cnx) {
            disableCnx()
            setTimeout(() => {
                window.location.href = "/";
            }, 100);
        }
    }, []);

    const [nom, setNom] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [adresse, setAdresse] = useState("");
    const [telephone, setTelephone] = useState();
    const [passwordError, setPasswordError] = useState(false);
    const [societeId, setSocieteId] = useState(societeIdStored);


    const handleSubmit = (event) => {
        event.preventDefault();
        if (password !== repeatPassword) {
            setPasswordError(true);
            return;
        }
        const user = { nom, email, password, adresse, telephone, societeId };
        console.log('9bal ba3then ', user)
        saveOperateur(user).then((resp) => {
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
            setNom("");
            setEmail("");
            setPassword("");
            setAdresse("");
            setTelephone("");
            setRepeatPassword("");
            setTelephone("");

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
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-lg-4 col-md-6 d-flex flex-column align-items-center justify-content-center">
                        <div className="card mb-3">
                            <div className="card-body">
                                <div className="pt-4 pb-2">
                                    <h5 className="card-title text-center pb-0 fs-4">Ajouter un Opérateur</h5>
                                    <p className="text-center small">Entrez vos informations personnelles pour créer un compte</p>
                                </div>
                                <form className="row g-3 needs-validation" onSubmit={handleSubmit} noValidate>
                                    <div className="col-6">
                                        <label htmlFor="yourName" className="form-label">Nom</label>
                                        <input type="text" name="name" className="form-control" id="yourName" required value={nom}
                                            onChange={(e) => setNom(e.target.value)} />
                                    </div>
                                    <div className="col-6">
                                        <label htmlFor="yourEmail" className="form-label"> Email </label>
                                        <input type="email" name="email" className="form-control" id="yourEmail" required value={email}
                                            onChange={(e) => setEmail(e.target.value)} />
                                    </div>
                                    <div className="col-12">
                                        <label htmlFor="yourPassword" className="form-label">Mot de passe</label>
                                        <input type="password" name="password" className="form-control" id="yourPassword" required value={password}
                                            onChange={(e) => setPassword(e.target.value)} />
                                    </div>
                                    <div className="col-12">
                                        <label htmlFor="repyourPassword" className="form-label">Répéter le mot de passe</label>
                                        <input type="password" name="reppassword" className="form-control" id="repyourPassword" required value={repeatPassword}
                                            onChange={(e) => setRepeatPassword(e.target.value)} />
                                        {passwordError && (
                                            <div className="text-danger small">
                                                Les mots de passe ne correspondent pas.
                                            </div>
                                        )}
                                    </div>
                                    <div className="col-12">
                                        <label htmlFor="yourAdresse" className="form-label">Adresse</label>
                                        <input type="text" name="adresse" className="form-control" id="adresse" required value={adresse}
                                            onChange={(e) => setAdresse(e.target.value)} />
                                    </div>
                                    <div className="col-12">
                                        <label htmlFor="yourTelephne" className="form-label">Téléphone</label>
                                        <input type="text" name="telephone" className="form-control" id="telephone" required value={telephone}
                                            onChange={(e) => setTelephone(e.target.value)} />
                                    </div>
                                    <div className="col-12">
                                        <button className="btn btn-primary w-100" type="submit">Créer un compte</button>
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

export default AjouterOperateur