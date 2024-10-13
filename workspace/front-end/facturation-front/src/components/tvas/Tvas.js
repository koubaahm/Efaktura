import { faEdit, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import $ from 'jquery';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'datatables.net-dt/js/dataTables.dataTables';
import { deleteTva, getTvas, getTvaBySociete } from "../../repository/TvasRepository";
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function Tvas() {

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

  const [tvas, setTvas] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    handleGetTvas();
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
  }, [tvas]);

  const handleGetTvas = async () => {
    try {
      // const resp = await getTvas();
      const resp = await getTvaBySociete(societeIdStored);
      setTvas(resp.data);
    } catch (error) {
      console.error("Error retrieving tvas:", error);
    }
  };

  const handleDeleteTvas = (tva) => {
    const confirmed = window.confirm("Voulez-vous vraiment supprimer ce tva?");
    if (confirmed) {
      deleteTva(tva)
        .then((res) => {
          let newTva = tvas.filter((p) => p !== tva);
          setTvas(newTva);
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
          <h2>List des Tva</h2>
        </div>
        <div className="card-body">
          <div className="row">
            <div className="col-6">
              <button
                onClick={() => navigate(`/AjouterTva`)}
                className="btn btn-outline-success"
              >
                <FontAwesomeIcon icon={faPlus}></FontAwesomeIcon>
                <span style={{ marginLeft: '0.5rem' }}>Ajouter Tva</span>
              </button>
              <br />
              <br />
              <table id="table" ref={tableRef} className="display">
                <thead>
                  <tr>
                    <th>Label</th>
                    <th>Supprimer</th>
                    <th>Modifier</th>
                  </tr>
                </thead>
                <tbody>
                  {tvas.map((tva) => (
                    <tr key={tva.id} id={tva.id}>
                      <td>{tva.label}</td>
                      <td>
                        <button
                          onClick={() => handleDeleteTvas(tva)}
                          className="btn btn-outline-danger"
                        >
                          <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                        </button>
                      </td>
                      <td>
                        <button
                          onClick={() => navigate(`/ModifierTva/${tva.id}`)}
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

export default Tvas;
