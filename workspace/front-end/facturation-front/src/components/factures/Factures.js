import { faEdit, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState, useRef } from "react";
import { deleteFacture, getFactures, getFactureBySociete } from "../../repository/FacturesRepository";
import { getClients, getClientBySociete } from "../../repository/ClientsRepository";
import { useNavigate } from "react-router-dom";
import $ from 'jquery';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'datatables.net-dt/js/dataTables.dataTables';
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function Factures() {

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

  const [factures, setFactures] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    handleGetFactures();
  }, []);

  const handleGetFactures = async () => {
    try {
      // const [clientsResp, facturesResp] = await Promise.all([getClients(), getFactures()]);
      const [clientsResp, facturesResp] = await Promise.all([getClientBySociete(societeIdStored), getFactureBySociete(societeIdStored)]);

      setFactures(facturesResp.data);

      const updatedFactures = facturesResp.data.map((facture) => {
        const correspondingClient = clientsResp.data.find((client) => client.id === facture.clientId);
        return {
          ...facture,
          client: correspondingClient
        };
      });

      setFactures(updatedFactures);

      setTimeout(() => {
        if ($.fn.DataTable.isDataTable(tableRef.current)) {
          $(tableRef.current).DataTable().destroy();
        }
        $(tableRef.current).DataTable({
          responsive: true
        });
      }, 1000);

    } catch (error) {
      console.error("Error retrieving products:", error);
    }
  };

  const handleDeleteFacture = (facture) => {
    const confirmed = window.confirm("Voulez-vous vraiment supprimer ce facture?");
    if (confirmed) {
      deleteFacture(facture)
        .then((res) => {
          let newFactures = factures.filter((p) => p !== facture);
          setFactures(newFactures);
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
          <h2>List des Factures</h2>
        </div>
        <div className="card-body">
          {/* <div className="row"> */}
          {/* <div className="col-12"> */}
          <button
            onClick={() => navigate(`/AjouterFacture`)}
            className="btn btn-outline-success"
          >
            <FontAwesomeIcon icon={faPlus}></FontAwesomeIcon>
            <span style={{ marginLeft: '0.5rem' }}>Ajouter une Facture</span>
          </button>
          <br />
          <br />
          <table id="table" ref={tableRef} className="display">
            <thead>
              <tr>
                <th>Numéro de facture</th>
                <th>client</th>
                <th>Date</th>
                <th>Remises</th>
                <th>Mode de paiement</th>
                <th>Statut</th>
                <th>Commentaires</th>
                <th>Total TTC</th>
                <th>Supprimer</th>
                <th>Modifier</th>
              </tr>
            </thead>
            <tbody>
              {factures.map((facture) => (

                <tr key={facture.id} id={facture.id}>
                  <td>{facture.numeroFacture}</td>
                  <td>{facture.client.nom}</td>
                  <td>{facture.date}</td>
                  <td>{facture.totalRemises.toFixed(3)}</td>
                  <td>{facture.modePaiement}</td>
                  <td>{facture.statut}</td>
                  <td>{facture.commentaires}</td>
                  <td>{facture.totalTTC.toFixed(3)}</td>
                  <td>
                    <button
                      onClick={() => handleDeleteFacture(facture)}
                      className="btn btn-outline-danger"
                    >
                      <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                    </button>
                  </td>
                  <td>
                    <button
                      onClick={() => navigate(`/ModifierFacture/${facture.id}`)}
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
    // </div>
    // </div>
  );
}

export default Factures;
