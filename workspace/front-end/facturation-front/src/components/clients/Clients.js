import { faEdit, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import $ from 'jquery';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'datatables.net-dt/js/dataTables.dataTables';
import { deleteClient, getClients, getClientBySociete } from "../../repository/ClientsRepository";

import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";
function Clients() {

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

  const [clients, setClients] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    handleGetClients();
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
    }, 1000);
    return () => {
      if (dataTableInstance !== null) {
        dataTableInstance.destroy();
      }
    };

  }, [clients]);

  const handleGetClients = async () => {
    try {
      const resp = await getClientBySociete(societeIdStored);
      // const resp = await getClients();
      setClients(resp.data);
    } catch (error) {
      console.error("Error retrieving clients:", error);
    }
  };

  const handleDeleteProducts = (client) => {
    const confirmed = window.confirm("Voulez-vous vraiment supprimer ce client?");
    if (confirmed) {
      deleteClient(client)
        .then((res) => {
          let newClient = clients.filter((p) => p !== client);
          setClients(newClient);
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
          <h2>List des Clients</h2>
        </div>
        <div className="card-body">
          <div className="row">
            <div className="col12">
              <button
                onClick={() => navigate(`/AjouterClient`)}
                className="btn btn-outline-success"
              >
                <FontAwesomeIcon icon={faPlus}></FontAwesomeIcon>
                <span style={{ marginLeft: '0.5rem' }}>Ajouter un Client</span>
              </button>
              <br />
              <br />
              <table id="table" ref={tableRef} className="display">
                <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Adresse</th>
                    <th>Telephone</th>
                    <th>Email</th>
                    <th>Supprimer</th>
                    <th>Modifier</th>
                  </tr>
                </thead>
                <tbody>
                  {clients.map((client) => (
                    <tr key={client.id} id={client.id}>
                      <td>{client.nom}</td>
                      <td>{client.adresse}</td>
                      <td>{client.telephone}</td>
                      <td>{client.email}</td>
                      <td>
                        <button
                          onClick={() => handleDeleteProducts(client)}
                          className="btn btn-outline-danger"
                        >
                          <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                        </button>
                      </td>
                      <td>
                        <button
                          onClick={() => navigate(`/ModifierClient/${client.id}`)}
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

export default Clients;
