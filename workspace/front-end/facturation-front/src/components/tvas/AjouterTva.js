import React, { useState, useEffect } from "react";
import { saveTva } from "../../repository/TvasRepository";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function AjouterTva() {

  useEffect(() => {
    const cnx = getCnxData();
    if (!cnx) {
      disableCnx()
      setTimeout(() => {
        window.location.href = "/";
      }, 100);
    }
  }, []);


  const [convertLabel, setConvertLabel] = useState(0);
  const [societeId, setSocieteId] = useState(societeIdStored);

  const handleSubmit = (event) => {
    event.preventDefault();
    const label = convertLabel + "%";
    const valeur = parseInt(convertLabel) / 10;
    const tva = { label, valeur, societeId };
    console.log(tva)
    saveTva(tva).then((resp) => {
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
      setConvertLabel(0);
    })
      .catch((error) => {
        const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement du tva";
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
      <div className="card col-3">
        <div className="card-header">
          <h2>Ajouter un Tva</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit} method="post">
            <div className="mb-3">
              <label htmlFor="label" className="form-label">
                valeur Tva %
              </label>
              <input
                id="label"
                type="number"
                className="form-control"
                value={convertLabel}
                onChange={(e) => setConvertLabel(e.target.value)}
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

export default AjouterTva;
