import React, { useState, useEffect } from "react";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { saveProduct, } from "../../repository/ProductsRepository";
import { saveListLigneAchat } from "../../repository/LigneAchatsRepository";
import { getTvaById, getTvaBySociete } from "../../repository/TvasRepository";
import { getFournisseurBySociete } from "../../repository/FournisseursRepository";
import { useNavigate } from "react-router-dom";
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";



function AjouterProduit() {

  useEffect(() => {
    const cnx = getCnxData();
    if (!cnx) {
      disableCnx()
      setTimeout(() => {
        window.location.href = "/";
      }, 100);
    }
  }, []);


  const navigate = useNavigate();

  const [produit, setProduit] = useState({
    societeId: societeIdStored,
    label: '',
    description: "",
    marge: 0,
    tvaId: 0,
  });

  const [tvas, setTvas] = useState([]);

  const [totalQuantite, setTotalQuantite] = useState([]);

  const [ligneAchats, setLigneAchat] = useState([{
    produitId: 0,
    fournisseurId: 0,
    quantiteAchat: 0,
    prixUnitaire: 0,
    societeId: societeIdStored
  }]);

  const [updateListLigneAchat, setSaveLigneAchat] = useState([]);

  const [fournisseurs, setFournisseurs] = useState([]);

  useEffect(() => {
    handleGetTvas();
    handleGetFournisseurs();
  }, []);
  useEffect(
    () => {
      console.log(produit)
    }, [produit]);
  useEffect(
    () => {
      console.log(tvas)
    }, [tvas]);
  useEffect(
    () => {
      console.log(ligneAchats)
    }, [ligneAchats]);

  useEffect(() => {
    if (updateListLigneAchat.length > 0) {
      saveListLigneAchats();
    }
  }, [updateListLigneAchat]);

  useEffect(() => {
    let total = 0;
    for (let i = 0; i < ligneAchats.length; i++) {
      total += parseFloat(ligneAchats[i].quantiteAchat);
    }
    setTotalQuantite(total)
  }, [ligneAchats]);


  const handleFournisseurIdChange = (index, id) => {
    if (id !== "0") {
      const updatedLigneAchats = [...ligneAchats];
      updatedLigneAchats[index] = {
        ...updatedLigneAchats[index],
        produitId: 0,
        fournisseurId: id,
        quantiteAchat: 0,
        prixUnitaire: 0,
        societeId: societeIdStored
      };
      setLigneAchat(updatedLigneAchats);
    } else {
      const updatedLigneAchats = [...ligneAchats];
      updatedLigneAchats[index] = {
        ...updatedLigneAchats[index],
        produitId: 0,
        fournisseurId: 0,
        quantiteAchat: 0,
        prixUnitaire: 0,
        societeId: societeIdStored
      };
      setLigneAchat(updatedLigneAchats);
    }
  }

  const handlePrixUnitaireChange = (index, prixAchat) => {
    const updatedLigneAchats = [...ligneAchats];
    updatedLigneAchats[index] = {
      ...updatedLigneAchats[index],
      prixUnitaire: prixAchat,
    };
    setLigneAchat(updatedLigneAchats);
  }

  const handleQuantiteAchatChange = (index, quantite) => {
    const updatedLigneAchats = [...ligneAchats];
    updatedLigneAchats[index] = {
      ...updatedLigneAchats[index],
      quantiteAchat: quantite,
    };
    console.log(updatedLigneAchats)
    setLigneAchat(updatedLigneAchats);
  }
  // -------------
  const updateSaveLigneAchats = newProduitId => {
    const updatedSaveLigneAchats = ligneAchats.map((ligneAchat) => {
      return {
        ...ligneAchat,
        produitId: newProduitId
      };
    });
    setSaveLigneAchat(updatedSaveLigneAchats);
  };

  // ************ get request
  const handleGetTvas = async () => {
    try {
      const resp = await getTvaBySociete(societeIdStored);
      setTvas(resp.data);
    } catch (error) {
      console.error("Error retrieving produits:", error);
    }
  };
  const handleGetFournisseurs = async () => {
    try {
      const resp = await getFournisseurBySociete(societeIdStored);
      setFournisseurs(resp.data);
    } catch (error) {
      console.error("Error retrieving produits:", error);
    }
  };

  // ********** buttun
  const handleAddLigneAchat = () => {
    const newLigneAchat = {
      produitId: 0,
      fournisseurId: 0,
      quantiteAchat: 0,
      prixUnitaire: 0,
      societeId: societeIdStored
    };

    setLigneAchat(prevLigneAchats => [...prevLigneAchats, newLigneAchat]);
  };

  const handleDeleteLigneAchat = (index) => {

    setLigneAchat(prevLigneAchats => {
      const updatedLigneAchats = [...prevLigneAchats];
      updatedLigneAchats.splice(index, 1);
      return updatedLigneAchats;
    });

  };

  // ******************* save

  const saveListLigneAchats = () => {
    saveListLigneAchat(updateListLigneAchat).then((resp) => {
      toast.success(JSON.stringify(resp.data), {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
      });

      setLigneAchat([{
        produitId: 0,
        fournisseurId: 0,
        quantiteAchat: 0,
        prixUnitaire: 0,
        societeId: societeIdStored
      }]);
      setTimeout(() => {
        navigate("/Produits");
      }, 1000);

    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement du produit";
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



  const handleSubmit = (event) => {
    event.preventDefault();
    // Perform data validation checks
    const isValid = ligneAchats.every((ligneAchat) => {
      return (
        parseFloat(ligneAchat.fournisseurId) > 0 &&
        parseFloat(ligneAchat.prixUnitaire) >= 0 &&
        parseInt(ligneAchat.quantiteAchat) >= 0
      );
    });
    console.log("isValid ", isValid)

    if (!isValid) {
      toast.error("Invalid ligneAchats data", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
      });
      return;
    }


    saveProduct(produit).then((resp) => {
      const newProduitId = resp.data.id;
      updateSaveLigneAchats(newProduitId)

      toast.success(JSON.stringify(resp.data), {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
      });

    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement du produit";
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
      <div className="card">
        <div className="card-header">
          <h2>Ajouter un Produit</h2>
        </div>
        <div className="card-body">
          <div className="row">
            <form onSubmit={handleSubmit} method="post">
              <div className="row">
                <div className="col-auto">
                  <label htmlFor="label" className="form-label">
                    Label
                  </label>
                  <input
                    id="label"
                    type="text"
                    className="form-control"
                    value={produit.label}
                    onChange={(e) => setProduit((prevProduit) => ({ ...prevProduit, label: e.target.value }))}
                    required
                  />
                </div>
                <div className="col-auto">
                  <label htmlFor="description" className="form-label">
                    Description
                  </label>
                  <input
                    id="description"
                    type="text"
                    className="form-control"
                    value={produit.description}
                    onChange={(e) => setProduit((prevProduit) => ({ ...prevProduit, description: e.target.value }))}
                    required
                  />
                </div>
                <div className="col-auto">
                  <label htmlFor="quantite" className="form-label">
                    Quantite
                  </label>
                  <input
                    id="quantite"
                    type="number"
                    className="form-control"
                    value={totalQuantite}
                    readOnly
                    required
                  />
                </div>
                <div className="col-auto">
                  <label htmlFor="marge" className="form-label">
                    Marge bénéficiaire %
                  </label>
                  <input
                    id="marge"
                    type="number"
                    className="form-control"
                    value={produit.marge * 100}
                    onChange={(e) => setProduit((prevProduit) => ({ ...prevProduit, marge: e.target.value / 100 }))}
                    required
                  />
                </div>
                <div className="col-auto">
                  <label htmlFor="tvaId" className="form-label">
                    TVA
                  </label>
                  <select
                    id="tvaId"
                    className="form-select"
                    onChange={(e) => setProduit((prevProduit) => ({ ...prevProduit, tvaId: e.target.value }))}
                    required
                  >
                    <option value="">Select a TVA</option>
                    {tvas.map((tva) => (
                      <option key={tva.id} value={tva.id}>
                        {tva.label}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
              {/* ************************************************************* */}
              <hr />

              <div>
                <div className="d-flex justify-content-end">
                  <button id="addLigneAchat" className="btn btn-success" type="button" onClick={handleAddLigneAchat}>+</button>
                </div>

                {ligneAchats.map((ligneAchat, index) => (
                  <div className="row" id={`ligneAchat-${index + 1}`} key={`ligneAchat-${index + 1}`}>
                    <hr />
                    <div className="col-auto">
                      <label htmlFor={`fournisseurId-${index + 1}`} className="form-label">
                        Fournisseur
                      </label>
                      <select
                        id={`fournisseurId-${index + 1}`}
                        className="form-select"
                        onChange={(e) => { handleFournisseurIdChange(index, e.target.value) }}
                        required
                      >
                        <option value="0">Select a Fournisseur</option>
                        {fournisseurs.map((fournisseur) => (
                          <option key={fournisseur.id} value={fournisseur.id}>
                            {fournisseur.nom} - {fournisseur.adresse}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="col-auto">
                      <label htmlFor={`prixUnitaire-${index + 1}`} className="form-label">
                        Prix d'achat
                      </label>
                      <input
                        id={`prixUnitaire-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={ligneAchat.prixUnitaire}
                        onChange={(e) => { handlePrixUnitaireChange(index, e.target.value) }}
                        required
                      />
                    </div>
                    <div className="col-auto">
                      <label htmlFor={`quantiteAchat-${index + 1}`} className="form-label">
                        Quantite d'achat
                      </label>
                      <input
                        id={`quantiteAchat-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={ligneAchat.quantiteAchat}
                        onChange={(e) => { handleQuantiteAchatChange(index, e.target.value) }}
                        required
                        min={0}
                      />
                    </div>
                    <div className="col-auto align-self-end">
                      <button id="deleteLigneAchat" className="btn btn-danger" type="button" onClick={() => handleDeleteLigneAchat(index)}>-</button>
                    </div>
                  </div>
                ))}
              </div>

              <hr />
              <button className="btn btn-primary">Enregistrer</button>
            </form>
          </div>
        </div>
      </div >
    </div >
  );
}

export default AjouterProduit;
