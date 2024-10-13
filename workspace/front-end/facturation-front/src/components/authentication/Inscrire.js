import React, { useState, useContext } from "react";
import { saveAdmin } from "../../repository/UsersRepository";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { activeCnx } from "../sharedDataLocalStorage/cnxStateStorage";

// import { UserContext } from "../context/SharedContext";


function Inscrire() {
    const [nom, setNom] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [adresse, setAdresse] = useState("");
    const [telephone, setTelephone] = useState();
    const [passwordError, setPasswordError] = useState(false);
    const navigate = useNavigate();



    const handleSubmit = (event) => {
        event.preventDefault();
        if (password !== repeatPassword) {
            setPasswordError(true);
            return;
        }
        const user = { nom, email, password, adresse, telephone };
        saveAdmin(user).then((resp) => {
            activeCnx();
            localStorage.setItem('userData', JSON.stringify(resp.data));
            setTimeout(() => {
                navigate(`/AjouterAbonnement/${0}`);
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
            {/* <section className="section register min-vh-100 d-flex flex-column align-items-center justify-content-center py-4"> */}
            <div className="">
                <div className="row justify-content-center">
                    <div className="col-7">
                        <img src={process.env.PUBLIC_URL + '/images/inscri2.png'} alt="Image Description" height={600} />
                    </div>
                    <div className="col-md-5 d-flex flex-column align-items-center justify-content-center">
                        <div className="card mb-3">
                            <div className="card-body">
                                <div className="pt-4 pb-2">
                                    <h5 className="card-title text-center pb-0 fs-4">Créer un compte</h5>
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
                                <br />
                                <div className="d-flex justify-content-center align-items-center">
                                    Vous avez un compte?
                                    <Link to="/Connexion">
                                        <a> Connexion</a>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            {/* </section> */}
        </div>
    );
}

export default Inscrire