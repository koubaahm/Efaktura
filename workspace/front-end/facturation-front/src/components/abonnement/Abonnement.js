import { faEdit, faPlus, faTrash, faCheckCircle, faBan } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import $ from 'jquery';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'datatables.net-dt/js/dataTables.dataTables';
import { deleteAbonnement, getAbonnements, getAbonnementBySociete, suspendreAbonnement, activerAbonnement } from "../../repository/AbonnementsRepository";
import { toast, ToastContainer } from 'react-toastify';

import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { getReload, saveReload } from "../sharedDataLocalStorage/ReloadStorage";
function Abonnement() {

  useEffect(() => {
    const cnx = getCnxData();
    if (!cnx) {
      disableCnx()
      setTimeout(() => {
        window.location.href = "/";
      }, 100);
    } else {
      const reloadFlag = getReload();
      if (!reloadFlag) {
        saveReload();
        window.location.reload(false);
      }
    }
  }, []);


  const tableRef = useRef(null);

  const [abonnements, setAbonnements] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    handleGetAbonnements();
  }, []);
  useEffect(() => {
    console.log(abonnements);
  }, [abonnements]);

  useEffect(() => {
    let dataTableInstance = null;
    setTimeout(() => {
      if ($.fn.DataTable.isDataTable(tableRef.current)) {
        $(tableRef.current).DataTable().destroy();
      }
      dataTableInstance = $(tableRef.current).DataTable({
        responsive: true
      });
    }, 1000);
    return () => {
      if (dataTableInstance !== null) {
        dataTableInstance.destroy();
      }
    };

  }, [abonnements]);

  const handleGetAbonnements = async () => {
    try {
      // const resp = await getAbonnementBySociete(1);
      const resp = await getAbonnements();
      setAbonnements(resp.data);
    } catch (error) {
      console.error("Error retrieving abonnements:", error);
    }
  };

  const handleactiverAbonnement = (abonnementId) => {
    const confirmed = window.confirm("Voulez-vous vraiment activer cet abonnement?");
    if (confirmed) {
      activerAbonnement(abonnementId)
        .then((resp) => {
          handleGetAbonnements();
          toast.success(JSON.stringify("Abonnement activer avec succès"), {
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
          const errorMessage = error.response?.data?.message || "Erreur lors de l'activation de l'abonnement";
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
  const handleSuspendreAbonnement = (abonnementId) => {
    const confirmed = window.confirm("Voulez-vous vraiment suspendre cet abonnement?");
    if (confirmed) {
      suspendreAbonnement(abonnementId)
        .then((resp) => {
          handleGetAbonnements();
          toast.success(JSON.stringify("Abonnement suspendu avec succès"), {
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
          const errorMessage = error.response?.data?.message || "Erreur lors de la suspension de l'abonnement";
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

  const handleDeleteProducts = (abonnement) => {
    const confirmed = window.confirm("Voulez-vous vraiment supprimer ce abonnement?");
    if (confirmed) {
      deleteAbonnement(abonnement)
        .then((res) => {
          let newAbonnement = abonnements.filter((p) => p !== abonnement);
          setAbonnements(newAbonnement);
          window.location.reload();
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };
  // const id = 1;
  return (
    <div className="p-3">
      <ToastContainer />
      <div className="card">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h2>List des Abonnements</h2>
          {/* <div className="ml-auto">
            <button
              onClick={() => navigate(`/ModifierOperateur/${id}`)}
              className="btn btn-outline-primary"
            >
              <FontAwesomeIcon icon={faEdit} />
              <span style={{ marginLeft: '0.5rem' }}>Modifier Compte</span>
            </button>
          </div> */}
        </div>
        <div className="card-body">
          <div className="row">
            <div className="col12">
              <br />
              <table id="table" ref={tableRef} className="display">
                <thead>
                  <tr>
                    <th>Date Debut</th>
                    <th>Date Fin</th>
                    <th>Nom du société</th>
                    <th>Statut</th>
                    <th>Activer</th>
                    <th>Suspendre</th>
                    <th>Delete</th>
                  </tr>
                </thead>
                <tbody>
                  {abonnements.map((abonnement) => (
                    <tr key={abonnement.id} id={abonnement.id}>
                      <td>{new Date(abonnement.dateDebut).toLocaleDateString()}</td>
                      <td>{new Date(abonnement.dateFin).toLocaleDateString()}</td>
                      <td>{abonnement.nonSociete}</td>
                      <td>{abonnement.active ? "Active" : "Suspendu"}</td>
                      <td>
                        <button
                          onClick={() => handleactiverAbonnement(abonnement.id)}
                          className="btn btn-outline-success"
                        >
                          <FontAwesomeIcon icon={faCheckCircle}></FontAwesomeIcon>
                        </button>
                      </td>
                      <td>
                        <button
                          onClick={() => handleSuspendreAbonnement(abonnement.id)}
                          className="btn btn-outline-danger"
                        >
                          <FontAwesomeIcon icon={faBan}></FontAwesomeIcon>
                        </button>
                      </td>
                      <td>
                        <button
                          onClick={() => handleDeleteProducts(abonnement)}
                          className="btn btn-outline-danger"
                        >
                          <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Abonnement;
