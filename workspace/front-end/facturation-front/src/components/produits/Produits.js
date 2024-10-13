import { faEdit, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState, useRef } from "react";
import { deleteProduct, getProductBySociete } from "../../repository/ProductsRepository";
import { useNavigate } from "react-router-dom";
import $ from 'jquery';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'datatables.net-dt/js/dataTables.dataTables';

import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { getReload, saveReload } from "../sharedDataLocalStorage/ReloadStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function Produits() {

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

  const [Produits, setProduits] = useState([]);

  const navigate = useNavigate();

  const handleGetProduits = async () => {
    try {
      // const resp = await getProducts();
      const resp = await getProductBySociete(societeIdStored);
      setProduits(resp.data);
    } catch (error) {
      console.error("Error retrieving Produits:", error);
    }
  };

  useEffect(() => {
    handleGetProduits();
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
  }, [Produits]);




  const handleDeleteProduits = (Produit) => {
    const confirmed = window.confirm("Voulez-vous vraiment supprimer ce produit?");
    if (confirmed) {
      deleteProduct(Produit)
        .then((res) => {
          let newProduits = Produits.filter((p) => p !== Produit);
          setProduits(newProduits);
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
          <h2>List des Produits</h2>
        </div>
        <div className="card-body">
          <div className="row">
            <div className="col12">
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <button
                    onClick={() => navigate(`/AjouterProduit`)}
                    className="btn btn-outline-success"
                  >
                    <FontAwesomeIcon icon={faPlus} />
                    <span style={{ marginLeft: '0.5rem' }}>Ajouter un Produit</span>
                  </button>
                </div>
                <div>
                  <button
                    onClick={() => navigate(`/Tva`)}
                    className="btn btn-outline-success"
                  >
                    <FontAwesomeIcon icon={faEdit} />
                    <span style={{ marginLeft: '0.5rem' }}>TVA</span>
                  </button>
                </div>
              </div>
              <br />
              <br />
              <table id="table" ref={tableRef} className="display">
                <thead>
                  <tr>
                    <th>Lable</th>
                    <th>Description</th>
                    <th>Qantitté en stock</th>
                    <th>Supprimer</th>
                    <th>Modifier</th>
                  </tr>
                </thead>
                <tbody>
                  {Produits.map((Produit) => (
                    <tr key={Produit.id} id={Produit.id}>
                      <td>{Produit.label}</td>
                      <td>{Produit.description}</td>
                      <td>{Produit.quantite}</td>
                      <td>
                        <button
                          onClick={() => handleDeleteProduits(Produit)}
                          className="btn btn-outline-danger"
                        >
                          <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                        </button>
                      </td>
                      <td>
                        <button
                          onClick={() => navigate(`/ModifierProduit/${Produit.id}`)}
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

export default Produits;
