import React, { useEffect, useState } from "react";
import { getTvaById, updateTva } from "../../repository/TvasRepository";
import { useParams } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify';
import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import 'react-toastify/dist/ReactToastify.css';
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";


function ModifierTva() {


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
  const [convertLabel, setConvertLabel] = useState(0);
  const [societeId, setSocieteId] = useState(societeIdStored);
  const navigate = useNavigate();

  useEffect(() => {
    handleGetTvaById(id);
  }, [id]);

  const handleGetTvaById = async (id) => {
    try {
      const resp = await getTvaById(id);
      setConvertLabel(resp.data.valeur * 10);

    } catch (error) {
      console.error("Error retrieving tvas:", error);
    }
  };



  const handleUpdateTva = (event) => {
    event.preventDefault();
    const label = convertLabel + "%";
    const valeur = parseInt(convertLabel) / 10;
    const tva = { id, label, valeur, societeId };
    updateTva(tva).then((resp) => {
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
        navigate("/Tva");
      }, 700);

    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "Erreur lors de l'update du produit";
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
      <div className="card col-3">
        <div className="card-header">
          <h2>Modifier le Tva</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleUpdateTva} method="post">
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
            <button className="btn btn-primary">Modifier</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default ModifierTva;
