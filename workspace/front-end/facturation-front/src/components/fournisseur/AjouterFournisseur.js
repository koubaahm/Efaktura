import React, { useState, useEffect } from "react";
import { saveFournisseur } from "../../repository/FournisseursRepository";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function AjouterFournisseur() {

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
  const [telephone, setTelephone] = useState(0);
  const [adresse, setAdresse] = useState("");
  const [societeId, setSocieteId] = useState(societeIdStored);

  const handleSubmit = (event) => {
    event.preventDefault();
    const fournissuer = { nom, telephone, adresse, societeId };
    saveFournisseur(fournissuer).then((resp) => {
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
      setTelephone(0);
      setAdresse("")
    })
      .catch((error) => {
        const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement du fournissuer";
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
    <div className="p-3">
      <ToastContainer />
      <div className="card">
        <div className="card-header">
          <h2>Ajouter un Fournisseur</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit} method="post">
            <div className="mb-3">
              <label htmlFor="nom" className="form-label">
                Nom
              </label>
              <input
                id="nom"
                type="text"
                className="form-control"
                value={nom}
                onChange={(e) => setNom(e.target.value)}
                required
              ></input>
            </div>
            <div className="mb-3">
              <label htmlFor="adresse" className="form-label">
                Adresse
              </label>
              <input
                id="adresse"
                type="text"
                className="form-control"
                value={adresse}
                onChange={(e) => setAdresse(e.target.value)}
              ></input>
            </div>
            <div className="mb-3">
              <label htmlFor="telephone" className="form-label">
                Telephone
              </label>
              <input
                id="telephone"
                type="number"
                className="form-control"
                value={telephone}
                onChange={(e) => setTelephone(e.target.value)}
                required
              ></input>
            </div>
            <button className="btn btn-primary">Ajouter</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default AjouterFournisseur;
