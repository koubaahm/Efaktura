import React, { useState, useEffect } from "react";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { getProductById, updateProduct } from "../../repository/ProductsRepository";
import { saveListLigneAchat, updateListLigneAchat, deleteLigneAchat, getLignesAchatsByProduit } from "../../repository/LigneAchatsRepository";
import { getTvaBySociete } from "../../repository/TvasRepository";
import { useParams } from "react-router-dom";
import { getFournisseurBySociete } from "../../repository/FournisseursRepository";
import { useNavigate } from "react-router-dom";
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";


function ModifierProduit() {

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

  const navigate = useNavigate();

  const [produit, setProduit] = useState({
    societeId: societeIdStored,
    label: '',
    description: "",
    marge: 0,
    tvaId: 0,
  });

  const [tvas, setTvas] = useState([]);

  const [stateLigneAchatWithId, setStateLigneAchatWithId] = useState(false);

  const [stateLigneAchatwithOutId, setStateLigneAchatwithOutId] = useState(false);

  const [totalQuantite, setTotalQuantite] = useState([]);

  const [ligneAchats, setLigneAchat] = useState([{
    produitId: 0,
    fournisseurId: 0,
    quantiteAchat: 0,
    prixUnitaire: 0,
    societeId: societeIdStored
  }]);

  const [fournisseurs, setFournisseurs] = useState([]);


  useEffect(() => {
    if (stateLigneAchatWithId && stateLigneAchatwithOutId) {
      setTimeout(() => {
        navigate("/Produits");
      }, 1000);
    }

  }, [stateLigneAchatWithId, stateLigneAchatwithOutId]);


  useEffect(() => {
    handleGetTvas();
    handleGetFournisseurs();
    handleGetProduit();
    handleGetLigneAchat();
  }, []);

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
  const updateIdLigneAchats = (ligneAchatsWithOutId) => {
    const updatedIdigneAchats = ligneAchatsWithOutId.map((ligneAchat) => {
      return {
        ...ligneAchat,
        produitId: id
      };
    });
    saveListLigneAchats(updatedIdigneAchats)
  };

  // ************ get request
  const handleGetProduit = async () => {
    try {
      const resp = await getProductById(id);
      setProduit(resp.data);
    } catch (error) {
      console.error("Error retrieving produits:", error);
    }
  };
  const handleGetTvas = async () => {
    try {
      const resp = await getTvaBySociete(1);
      setTvas(resp.data);
    } catch (error) {
      console.error("Error retrieving produits:", error);
    }
  };

  const handleGetLigneAchat = async () => {
    try {
      const resp = await getLignesAchatsByProduit(id);
      setLigneAchat(resp.data);
    } catch (error) {
      console.error("Error retrieving LigneAchat:", error);
    }
  };
  const handleGetFournisseurs = async () => {
    try {
      const resp = await getFournisseurBySociete(1);
      setFournisseurs(resp.data);
    } catch (error) {
      console.error("Error retrieving Fournisseurs:", error);
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
    // deleteLigneAchat(id)


    if (ligneAchats[index].id) {
      deleteLigneAchat(ligneAchats[index]).then((resp) => {
        setLigneAchat(prevLigneAchats => {
          const updatedLigneAchats = [...prevLigneAchats];
          updatedLigneAchats.splice(index, 1);
          return updatedLigneAchats;
        });
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
        const errorMessage = error.response?.data?.message || "Erreur lors de delete la ligne d'achat";
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
      })
    } else {
      setLigneAchat(prevLigneAchats => {
        const updatedLigneAchats = [...prevLigneAchats];
        updatedLigneAchats.splice(index, 1);
        return updatedLigneAchats;
      });
    }

  };

  // ******************* update

  const saveListLigneAchats = (updatedIdigneAchats) => {
    saveListLigneAchat(updatedIdigneAchats).then((resp) => {
      setStateLigneAchatwithOutId(true)
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

  const updateListLigneAchats = (ligneAchatswithId) => {
    updateListLigneAchat(ligneAchatswithId).then((resp) => {
      setStateLigneAchatWithId(true)
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



  const handleUpdateProduit = (event) => {
    event.preventDefault();
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


    updateProduct(produit).then((resp) => {

      const ligneAchatswithId = [];
      const ligneAchatsWithOutId = [];

      ligneAchats.forEach(item => {
        if (item.id) {
          ligneAchatswithId.push(item);
        } else {
          ligneAchatsWithOutId.push(item);
        }
      });

      updateListLigneAchats(ligneAchatswithId);
      updateIdLigneAchats(ligneAchatsWithOutId);
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
          <h2>Modifier un Produit</h2>
        </div>
        <div className="card-body">
          <div className="row">
            <form onSubmit={handleUpdateProduit} method="post">
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
                    value={produit.tvaId}
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

export default ModifierProduit;
