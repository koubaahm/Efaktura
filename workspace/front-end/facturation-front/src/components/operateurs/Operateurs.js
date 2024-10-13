import { faEdit, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState, useRef } from "react";
import { deleteUser, getUsers, getUserbySociete } from "../../repository/UsersRepository";
import { useNavigate } from "react-router-dom";
import $ from 'jquery';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'datatables.net-dt/js/dataTables.dataTables';
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function Operateurs() {

  useEffect(() => {
    const cnx = getCnxData();
    if (!cnx) {
      disableCnx()
      setTimeout(() => {
        window.location.href = "/";
      }, 100);
    }
  }, []);

  console.log(societeIdStored)
  const tableRef = useRef(null);

  const [users, setUsers] = useState([]);

  const navigate = useNavigate();

  const handleGetUsers = async () => {
    try {
      const resp = await getUserbySociete(societeIdStored);
      // const resp = await getUsers();
      console.log(resp.data)
      setUsers(resp.data);
    } catch (error) {
      console.error("Error retrieving users:", error);
    }
  };

  useEffect(() => {
    handleGetUsers();
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
  }, [users]);

  const handleDeleteUsers = (user) => {
    const confirmed = window.confirm("Voulez-vous vraiment supprimer ce User?");
    if (confirmed) {
      deleteUser(user)
        .then((res) => {
          let newUsers = users.filter((p) => p !== user);
          setUsers(newUsers);
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
          <h2>List des Opérateurs</h2>
        </div>
        <div className="card-body">
          <div className="row">
            <div className="col-12">
              <button
                onClick={() => navigate(`/AjouterOperateur`)}
                className="btn btn-outline-success"
              >
                <FontAwesomeIcon icon={faPlus}></FontAwesomeIcon>
                <span style={{ marginLeft: '0.5rem' }}>Ajouter un Opérateur</span>
              </button>
              <br />
              <br />
              <table id="table" ref={tableRef} className="display">
                <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Rôle</th>
                    <th>Téléphone</th>
                    <th>Adresse</th>
                    <th>Supprimer</th>
                    <th>Modifier</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.id} id={user.id}>
                      <td>{user.nom}</td>
                      <td>{user.email}</td>
                      <td>{user.rolesNom.includes("ADMIN") ? "ADMIN" : user.rolesNom.includes("OPERATEUR") ? "OPERATEUR" : ""}</td>
                      <td>{user.telephone}</td>
                      <td>{user.adresse}</td>
                      <td>
                        <button
                          onClick={() => handleDeleteUsers(user)}
                          className="btn btn-outline-danger"
                        >
                          <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                        </button>
                      </td>
                      <td>
                        <button
                          onClick={() => navigate(`/ModifierOperateur/${user.id}`)}
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

export default Operateurs;
