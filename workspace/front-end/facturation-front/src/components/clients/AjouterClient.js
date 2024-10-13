import React, { useState, useEffect } from "react";
import { saveClient } from "../../repository/ClientsRepository";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";
function AjouterClient() {

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
  const [email, setEmail] = useState("");
  const [cin_MatriculeFiscal, setCin_MatriculeFiscal] = useState("");
  const [societeId, setSocieteId] = useState(societeIdStored);

  const handleSubmit = (event) => {
    event.preventDefault();
    const client = { nom, telephone, adresse, email, cin_MatriculeFiscal, societeId };
    saveClient(client).then((resp) => {
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
      setCin_MatriculeFiscal("")
      setEmail("")
    })
      .catch((error) => {
        const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement du client";
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
          <h2>Ajouter un Client</h2>
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
            <div className="mb-3">
              <label htmlFor="email" className="form-label">
                Email
              </label>
              <input
                id="email"
                type="text"
                className="form-control"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              ></input>
            </div>
            <div className="mb-3">
              <label htmlFor="cin_MatriculeFiscal" className="form-label">
                Cin / MatriculeFiscal
              </label>
              <input
                id="cin_MatriculeFiscal"
                type="text"
                className="form-control"
                value={cin_MatriculeFiscal}
                onChange={(e) => setCin_MatriculeFiscal(e.target.value)}

              ></input>
            </div>
            <button className="btn btn-primary">Ajouter</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default AjouterClient;
