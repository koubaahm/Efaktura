import React, { useState, useEffect } from 'react'
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { activerAbonnement, createAbonnement, renouvellerAbonnement } from "../../repository/AbonnementsRepository";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";

import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
function AjouterAbonnement() {

    // useEffect(() => {
    //     const cnx = getCnxData();
    //     if (!cnx) {
    //         disableCnx()
    //         setTimeout(() => {
    //             window.location.href = "/";
    //         }, 100);
    //     }
    // }, []);


    const [abonnement, setAbonnement] = useState({ "active": true });

    const { id } = useParams();

    const navigate = useNavigate();

    const handlePaiement = () => {

        if (parseInt(id) !== 0) {
            renouvellerAbonnement(id).then((resp) => {
                localStorage.setItem('abonnementData', JSON.stringify(resp.data));
                setTimeout(() => {
                    navigate("/Connexion");
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
                    const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement du abonnement";
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
        } else {

            createAbonnement(abonnement).then((resp) => {
                localStorage.setItem('abonnementData', JSON.stringify(resp.data));
                setTimeout(() => {
                    navigate("/AjouterSociete");
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
                    const errorMessage = error.response?.data?.message || "Erreur lors de la creation du abonnement";
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
        }
    };


    return (
        <div className="container">
            <ToastContainer />
            <br />
            <div className="row">
                <div className="col-lg-4 mb-lg-0 mb-3">
                    <div className="card p-3">
                        <div className="img-box">
                            <img src="https://www.freepnglogos.com/uploads/visa-logo-download-png-21.png" alt="" width={60} />
                        </div>
                        <div className="number">
                            <label className="fw-bold" htmlFor="">**** **** **** 1060</label>
                        </div>
                        <div className="d-flex align-items-center justify-content-between">
                            <small><span className="fw-bold">Date d'expiration:</span><span>10/16</span></small>
                            <small><span className="fw-bold">Nom:</span><span>***</span></small>
                        </div>
                    </div>
                </div>
                <div className="col-lg-4 mb-lg-0 mb-3">
                    <div className="card p-3">
                        <div className="img-box">
                            <img src="https://www.freepnglogos.com/uploads/mastercard-png/file-mastercard-logo-svg-wikimedia-commons-4.png"
                                alt="" width={60} />
                        </div>
                        <div className="number">
                            <label className="fw-bold">**** **** **** 1060</label>
                        </div>
                        <div className="d-flex align-items-center justify-content-between">
                            <small><span className="fw-bold">Date d'expiration:</span><span>10/16</span></small>
                            <small><span className="fw-bold">Nom:</span><span>***</span></small>
                        </div>
                    </div>
                </div>
                <div className="col-lg-4 mb-lg-0 mb-3">
                    <div className="card p-3">
                        <div className="img-box">
                            <img src="https://www.freepnglogos.com/uploads/discover-png-logo/credit-cards-discover-png-logo-4.png"
                                alt="" width={60} />
                        </div>
                        <div className="number">
                            <label className="fw-bold">**** **** **** 1060</label>
                        </div>
                        <div className="d-flex align-items-center justify-content-between">
                            <small><span className="fw-bold">Date d'expiration:</span><span>10/16</span></small>
                            <small><span className="fw-bold">Nom:</span><span>***</span></small>
                        </div>
                    </div>
                </div>
                <div className="col-12 mt-4">
                    <div className="card p-3">
                        <p className="mb-0 fw-bold h4">Méthodes de payement</p>
                    </div>
                </div>
                <div className="col-12">
                    <div className="card p-3">
                        <div className="card-body border p-0">
                            <p>
                                <span className="p-3 mb-2 bg-light w-100 h-100 d-flex align-items-center justify-content-between">
                                    <span className="fw-bold">Carte de crédit</span>
                                </span>
                            </p>
                            <div className="collapse show p-3 pt-0">
                                <div className="row">
                                    <div className="col-lg-5 mb-lg-0 mb-3">
                                        <p className="h4 mb-0">Résumé</p>
                                        <p className="mb-0"><span className="fw-bold">Produit:</span><span className="c-green">: Renouvellement
                                            de l'abonnement à l'application de facturation en ligne
                                        </span>
                                        </p>
                                        <p className="mb-0">
                                            <span className="fw-bold">Prix:</span>
                                            <span className="c-green">: 100 Dt/ans</span>
                                        </p>
                                        <p className="mb-0">Cet abonnement vous permet de traiter votre activité de facturation pendant 1 an,
                                            non renouvelable automatiquement</p>
                                    </div>
                                    <div className="col-lg-7">
                                        <form action="" className="form">
                                            <div className="row">
                                                <div className="col-12">
                                                    <div className="form__div">
                                                        <input type="text" className="form-control" placeholder=" " required />
                                                        <label htmlFor="" className="form__label">Numéro de carte</label>
                                                    </div>
                                                </div>

                                                <div className="col-6">
                                                    <div className="form__div">
                                                        <input type="text" className="form-control" placeholder=" " required />
                                                        <label htmlFor="" className="form__label">MM / yy</label>
                                                    </div>
                                                </div>

                                                <div className="col-6">
                                                    <div className="form__div">
                                                        <input type="password" className="form-control" placeholder=" " required />
                                                        <label htmlFor="" className="form__label">Code CVV</label>
                                                    </div>
                                                </div>
                                                <div className="col-12">
                                                    <div className="form__div">
                                                        <input type="text" className="form-control" placeholder=" " required />
                                                        <label htmlFor="" className="form__label">Nom sur la Carte</label>
                                                    </div>
                                                </div>
                                                <div className="col-12">
                                                    <button className="btn btn-light w-100">confirmer</button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="col-12">
                    <button className="btn btn-primary" type='button' onClick={handlePaiement} >
                        Effectuer le paiement
                    </button>
                </div>
            </div>
        </div>
    )
}

export default AjouterAbonnement