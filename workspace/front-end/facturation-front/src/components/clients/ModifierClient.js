import React, { useEffect, useState } from "react";
import { getClientById, updateClient, getClientBySociete } from "../../repository/ClientsRepository";
import { useParams } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify';
import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTimesCircle } from '@fortawesome/free-solid-svg-icons';

import 'react-toastify/dist/ReactToastify.css';
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function ModifierClient() {

  useEffect(() => {
    const cnx = getCnxData();
    if (!cnx) {
      disableCnx()
      setTimeout(() => {
        window.location.href = "/";
      }, 100);
    }
  }, []);

  const { id } = useParams();
  const [nom, setNom] = useState("");
  const [telephone, setTelephone] = useState(0);
  const [adresse, setAdresse] = useState("");
  const [email, setEmail] = useState("");
  const [cin_MatriculeFiscal, setCin_MatriculeFiscal] = useState("");
  const [societeId, setSocieteId] = useState(societeIdStored);

  useEffect(() => {
    handleGetClientById(id);
  }, [id]);

  const handleGetClientById = async (id) => {
    try {
      const resp = await getClientById(id);
      setNom(resp.data.nom);
      setTelephone(resp.data.telephone);
      setAdresse(resp.data.adresse);
      setEmail(resp.data.email);
      setCin_MatriculeFiscal(resp.data.cin_MatriculeFiscal);
    } catch (error) {
      console.error("Error retrieving Clients:", error);
    }
  };

  const navigate = useNavigate();

  const handleUpdateClient = (event) => {
    event.preventDefault();
    const Client = { id, nom, telephone, adresse, email, cin_MatriculeFiscal, societeId };
    updateClient(Client).then((resp) => {
      toast.success("Mise à jour terminée avec succès", {
        position: "top-center",
        autoClose: 300,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        icon: <FontAwesomeIcon icon={faCheckCircle} style={{ color: 'green' }} />,
      });

      setTimeout(() => {
        navigate("/Clients");
      }, 700);

    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "Erreur lors de l'update du client";
      toast.error(errorMessage, {
        position: "top-center",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        icon: <FontAwesomeIcon icon={faTimesCircle} style={{ color: 'red' }} />,
      });
    });
  };



  return (
    <div className="p-3">
      <ToastContainer />
      <div className="card">
        <div className="card-header">
          <h2>Modifier le Client</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleUpdateClient} method="post">
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
              <label htmlFor="telephone" className="form-label">
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
                required
              ></input>
            </div>
            <button className="btn btn-primary">Modifier</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default ModifierClient;
