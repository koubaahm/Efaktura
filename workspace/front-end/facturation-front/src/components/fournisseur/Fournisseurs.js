import { faEdit, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState, useRef, useContext } from "react";
import { useNavigate } from "react-router-dom";
import $ from 'jquery';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'datatables.net-dt/js/dataTables.dataTables';
import { deleteFournisseur, getFournisseurs, getFournisseurBySociete } from "../../repository/FournisseursRepository";
import { UserContext } from "../context/SharedContext";
import { getUserData, userRolesStored, userIdStored } from "../sharedDataLocalStorage/UserStorage";
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";


function Fournisseurs() {

  useEffect(() => {
    const cnx = getCnxData();
    if (!cnx) {
      disableCnx()
      setTimeout(() => {
        window.location.href = "/";
      }, 100);
    }
  }, []);


  const tableRef = useRef(null);

  const [fournisseurs, setFournisseurs] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    handleGetFournisseurs();
  }, []);

  useEffect(() => {
    let dataTableInstance = null;
    setTimeout(() => {
      if ($.fn.DataTable.isDataTable(tableRef.current)) {
        $(tableRef.current).DataTable().destroy();
      }
      dataTableInstance = $(tableRef.current).DataTable({
        responsive: true
      });
    }, 700);
    return () => {
      if (dataTableInstance !== null) {
        dataTableInstance.destroy();
      }
    };
  }, [fournisseurs]);

  const handleGetFournisseurs = async () => {
    try {
      // const resp = await getFournisseurs();
      const resp = await getFournisseurBySociete(societeIdStored);
      setFournisseurs(resp.data);
    } catch (error) {
      console.error("Error retrieving fournisseurs:", error);
    }
  };

  const handleDeleteProducts = (fournisseur) => {
    const confirmed = window.confirm("Voulez-vous vraiment supprimer ce fournisseur?");
    if (confirmed) {
      deleteFournisseur(fournisseur)
        .then((res) => {
          let newFournisseur = fournisseurs.filter((p) => p !== fournisseur);
          setFournisseurs(newFournisseur);
          window.location.reload();
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  return (
    <div className="p-3">
      <div className="card">
        <div className="card-header">
          <h2>List des Fournisseurs</h2>
        </div>
        <div className="card-body">
          <div className="row">
            <div className="col-12">
              <button
                onClick={() => navigate(`/AjouterFournisseur`)}
                className="btn btn-outline-success"
              >
                <FontAwesomeIcon icon={faPlus}></FontAwesomeIcon>
                <span style={{ marginLeft: '0.5rem' }}>Ajouter un Fournisseur</span>
              </button>
              <br />
              <br />
              <table id="table" ref={tableRef} className="display">
                <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Adresse</th>
                    <th>Telephone</th>
                    <th>Supprimer</th>
                    <th>Modifier</th>
                  </tr>
                </thead>
                <tbody>
                  {fournisseurs.map((fournisseur) => (
                    <tr key={fournisseur.id} id={fournisseur.id}>
                      <td>{fournisseur.nom}</td>
                      <td>{fournisseur.adresse}</td>
                      <td>{fournisseur.telephone}</td>
                      <td>
                        <button
                          onClick={() => handleDeleteProducts(fournisseur)}
                          className="btn btn-outline-danger"
                        >
                          <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                        </button>
                      </td>
                      <td>
                        <button
                          onClick={() => navigate(`/ModifierFournisseur/${fournisseur.id}`)}
                          className="btn btn-outline-success"
                        >
                          <FontAwesomeIcon icon={faEdit}></FontAwesomeIcon>
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

export default Fournisseurs;
