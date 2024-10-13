import React, { useEffect, useState } from "react";
import { getFactureById, updateFacture } from "../../repository/FacturesRepository";
import { useParams } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify';
import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import 'react-toastify/dist/ReactToastify.css';
import { saveListLigneFacture, getLigneFactureIdFacture, updateListLigneFacture, deleteLigneFacture } from "../../repository/LigneFacturesRepository";
import { getClientBySociete, getClients } from "../../repository/ClientsRepository";
import { getProductBySociete, getProducts } from "../../repository/ProductsRepository";
import { getAllByProduit } from "../../repository/LigneAchatsRepository";
import { getTvaById } from "../../repository/TvasRepository";
import 'react-toastify/dist/ReactToastify.css';
import { disableCnx, getCnxData } from "../sharedDataLocalStorage/cnxStateStorage";
import { societeIdStored } from "../sharedDataLocalStorage/societeStorage";

function ModifierFacture() {

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

  const [facture, setFacture] = useState({
    societeId: societeIdStored,
    numeroFacture: '',
    date: "",
    remises: 0,
    modePaiement: '',
    statut: '',
    commentaires: '',
    clientId: 0,
  });

  const [clients, setClients] = useState([]);

  const [totalNetHT, setTotalNetHT] = useState();

  const [totalNetTTC, setTotalNetTTC] = useState();

  const [initialRender, setInitialRender] = useState(true);

  const [initialLigneAchats, setInitialLigneAchats] = useState(true);

  const [ligneFactures, setLigneFactures] = useState([{

    idLigneAchat: 0,
    quantite: 1,
    prixUnitaire: 0,
    factureId: 0,
    produitId: 0,
    tauxRemise: 0
  }]);

  const [saveLigneFactures, setSaveLigneFactures] = useState([]);

  const [updatedLignesFacture, setUpdatedLignesFacture] = useState([]);

  const [stateUpdatedLignesFacture, setStateUpdatedLignesFactur] = useState(false);

  const [addLignesFacture, setAddLignesFacture] = useState([]);

  const [stateAddLignesFacture, setStateAddLignesFacture] = useState(false);

  const [ligneAchats, setLigneAchats] = useState([[{
    id: 0,
    date: "",
    "produitId": 0,
    "fournisseurId": 0,
    "prixUnitaire": 0,
    "prixTotal": 0,
    "quantiteAchat": 0,
    "quantiteStock": 0,
    "societeId": societeIdStored
  }]]);

  const [produits, setProduits] = useState([]);

  const [idProduits, setIdProduits] = useState([]);

  const [prixUnitaireHTNet, setPrixUnitaireHTNet] = useState([0]);

  const [prixTotalNetTTC, setPrixTotalNetTTC] = useState([0]);

  const [prixTotalNetHT, setPrixTotalNetHT] = useState([0]);

  const [tvas, setTvas] = useState([]);

  const [quantityError, setQuantityError] = useState('');

  const [tvasInitialized, setTvasInitialized] = useState(true);

  const [prixUnitaireInitialized, setPrixUnitaireInitialized] = useState(true);

  const [idProdInitialized, setIdprodInitialized] = useState(true);

  const [countTva, setCountTva] = useState(0);

  const [countPrixUnitaire, setCountPrixUnitaire] = useState(0);

  const [countidProd, setCountidProd] = useState(0);

  const [handleGetTvaCompleted, setHandleGetTvaCompleted] = useState(false);




  useEffect(() => {
    handleGetFactureById(id);
    handleGetLigneFactureIdFacture(id);
  }, [id]);


  useEffect(() => {
    let totalNetHTCalcul = 0;
    for (let i = 0; i < prixTotalNetHT.length; i++) {
      totalNetHTCalcul += parseFloat(prixTotalNetHT[i]);
    }
    if (initialRender) {
      setTotalNetHT(0);
    } else {
      const formattedTotalNetHTCalcul = totalNetHTCalcul.toFixed(3);
      setTotalNetHT(formattedTotalNetHTCalcul);
    }
    setInitialRender(false);
  }, [prixTotalNetHT, ligneFactures]);

  useEffect(() => {
    let totalNetTTCCalcul = 0;
    for (let i = 0; i < prixTotalNetTTC.length; i++) {
      totalNetTTCCalcul += parseFloat(prixTotalNetTTC[i]);
    }
    if (initialRender) {
      setTotalNetTTC(0);
    } else {
      const formattedTotalNetTTCCalcul = totalNetTTCCalcul.toFixed(3);
      setTotalNetTTC(formattedTotalNetTTCCalcul);
    }
    setInitialRender(false);
  }, [prixTotalNetTTC, ligneFactures]);

  useEffect(() => {
    if (initialLigneAchats) {
      for (let i = 0; i < ligneFactures.length; i++) {
        if (parseInt(ligneFactures[i].produitId) !== 0) {
          let idProd = ligneFactures[i].produitId;
          handleGetLigneAchats(i, idProd);
          setInitialLigneAchats(false)
        }
      }
    }
  }, [ligneFactures]);

  useEffect(() => {
    if (idProdInitialized) {
      if (produits.length > 0 && ligneFactures.length > 0) {
        for (let i = 0; i < ligneFactures.length; i++) {
          let idProd = ligneFactures[i].produitId;
          if (parseInt(idProd) !== 0) {
            setIdProduits((prevIdProduits) => {
              const updatedIdProduits = [...prevIdProduits];
              updatedIdProduits[i] = idProd;
              return updatedIdProduits;
            });
            setIdprodInitialized(false);
          }
        }
      }
    }
  }, [ligneFactures]);


  useEffect(() => {
    const handleAsyncOperations = async () => {
      if (tvasInitialized) {
        let isHandleGetTvaFinished = true;
        for (let i = 0; i < ligneFactures.length; i++) {
          if (parseInt(ligneFactures[i].produitId) !== 0) {
            let idProd = ligneFactures[i].produitId;
            await handleGetTva(i, idProd);
          } else {
            isHandleGetTvaFinished = false;
          }
        }
        if (isHandleGetTvaFinished) {
          setHandleGetTvaCompleted(true);
        }
      }
    };

    handleAsyncOperations();
  }, [ligneFactures]);

  useEffect(() => {
    if (prixUnitaireInitialized && handleGetTvaCompleted) {
      if (produits.length > 0 && ligneAchats.length > 0) {
        for (let i = 0; i < ligneFactures.length; i++) {
          if (
            parseInt(ligneFactures[i].produitId) !== 0 &&
            ligneAchats[i] !== undefined &&
            ligneAchats[0][0].id !== 0
          ) {
            console.log(ligneFactures);
            let idLigneAchat = ligneFactures[i].idLigneAchat;
            // calculerPrixUnitaire(i, idLigneAchat);

            calculerTousChamps(i, idLigneAchat)
            // handleRemisesChange(i, ligneFactures[i].tauxRemise)
          }
        }
        setCountPrixUnitaire(countPrixUnitaire + 1);
      }
      if (countPrixUnitaire >= ligneFactures.length) {
        setPrixUnitaireInitialized(false);
      }
    }
  }, [prixUnitaireInitialized, handleGetTvaCompleted]);


  //loula lasliya
  // useEffect(() => {
  //   if (prixUnitaireInitialized) {
  //     if (produits.length > 0 && ligneAchats.length > 0) {
  //       for (let i = 0; i < ligneFactures.length; i++) {
  //         if (parseInt(ligneFactures[i].produitId) !== 0 && ligneAchats[i] !== undefined && ligneAchats[0][0].id !== 0) {
  //           let idLigneAchat = ligneFactures[i].idLigneAchat;
  //           calculerPrixUnitaire(i, idLigneAchat);
  //         }
  //       }
  //       setCountPrixUnitaire(countPrixUnitaire + 1)
  //     }
  //     if (countPrixUnitaire >= ligneFactures.length) {
  //       setPrixUnitaireInitialized(false);
  //     }
  //   }
  // }, [idProduits, ligneAchats]);

  //loula lasliya
  // useEffect(() => {

  //   if (tvasInitialized) {
  //     if (countTva < ligneFactures.length) {
  //       for (let i = 0; i < ligneFactures.length; i++) {
  //         if (parseInt(ligneFactures[i].produitId) !== 0) {
  //           let idProd = ligneFactures[i].produitId;
  //           handleGetTva(i, idProd);
  //         }
  //       }
  //       setCountTva(countTva + 1)
  //     }
  //     if (countTva >= ligneFactures.length) {
  //       setTvasInitialized(false);
  //     }
  //   }
  // }, [ligneFactures]);


  useEffect(() => {
    if (saveLigneFactures.length > 0) {
      saveListLigneFactures();
    }
  }, [saveLigneFactures]);

  useEffect(() => {
    if (updatedLignesFacture.length > 0) {
      updateListLignesFactures();
    }
  }, [updatedLignesFacture]);


  useEffect(() => {
    if (addLignesFacture.length > 0) {
      const factureId = facture.id;
      updateSaveLigneFactures(factureId)
    }
  }, [addLignesFacture]);

  useEffect(() => {
    if (stateAddLignesFacture && stateUpdatedLignesFacture) {
      setTimeout(() => {
        navigate("/Factures");
      }, 1000);
    }

  }, [stateAddLignesFacture, stateUpdatedLignesFacture]);

  useEffect(() => {
    handleGetClients();
    handleGetProduits();
  }, []);

  const handleProduitIdChange = (index, id) => {
    if (id === "0") {
      const initialLigneAchats = [
        [
          {
            id: 0,
            date: "",
            produitId: 0,
            fournisseurId: 0,
            prixUnitaire: 0,
            prixTotal: 0,
            quantiteAchat: 0,
            quantiteStock: 0,
            societeId: societeIdStored
          }
        ]
      ];
      setLigneAchats(initialLigneAchats);
    }

    const updatedLigneFactures = [...ligneFactures];
    updatedLigneFactures[index] = {
      ...updatedLigneFactures[index],
      idLigneAchat: 0,
      quantite: 1,
      prixUnitaire: 0,
      factureId: 0,
      produitId: parseInt(id),
      tauxRemise: 0
    };
    setLigneFactures(updatedLigneFactures);
    const updatedIdProduits = [...idProduits];
    updatedIdProduits[index] = parseInt(id);
    setIdProduits(updatedIdProduits);
  };

  const initializeObject = (index) => {
    setPrixUnitaireHTNet(prevPrixUnitaireHTNet => {
      const updatedPrixUnitaireHTNet = [...prevPrixUnitaireHTNet];
      updatedPrixUnitaireHTNet[index] = 0;
      return updatedPrixUnitaireHTNet;
    });

    setPrixTotalNetTTC(prevPrixTotalNetTTC => {
      const updatedPrixTotalNetTTC = [...prevPrixTotalNetTTC];
      updatedPrixTotalNetTTC[index] = 0;
      return updatedPrixTotalNetTTC;
    });

    setPrixTotalNetHT(prevPrixTotalNetHT => {
      const updatedPrixTotalNetHT = [...prevPrixTotalNetHT];
      updatedPrixTotalNetHT[index] = 0;
      return updatedPrixTotalNetHT;
    });

    setTvas(prevTvas => {
      const updatedTvas = [...prevTvas];
      updatedTvas[index] = 0;
      return updatedTvas;
    });
  };


  const calculerTousChamps = async (index, idLigneAchat) => {

    if (idLigneAchat !== "0") {

      const idProduit = idProduits[index];
      const produit = produits.find(produit => produit.id === idProduit);
      const ligneAchat = ligneAchats[index].find(ligneAchat => parseInt(ligneAchat.id) === parseInt(idLigneAchat));
      let valuePrixUnitaireHt = (produit.marge * ligneAchat.prixUnitaire) + ligneAchat.prixUnitaire;
      let valuePrixUnitaireHtFormated = valuePrixUnitaireHt.toFixed(3);

      // calculer Prix Unitaire HT
      setLigneFactures(() => {
        const updatedLigneFactures = [...ligneFactures];
        updatedLigneFactures[index] = {
          ...updatedLigneFactures[index],
          idLigneAchat: ligneAchat.id,
          prixUnitaire: valuePrixUnitaireHtFormated,
        };
        return updatedLigneFactures;
      });

      console.log(ligneFactures[index].tauxRemise)

      // calculer Prix Unitaire HT Net
      let prixUnitaireHtNet = parseFloat(valuePrixUnitaireHt - (valuePrixUnitaireHt * (ligneFactures[index].tauxRemise / 100))).toFixed(3);
      setPrixUnitaireHTNet(prevPrixUnitaireHTNet => {
        const updatedPrixUnitaireHTNet = [...prevPrixUnitaireHTNet];
        updatedPrixUnitaireHTNet[index] = prixUnitaireHtNet;
        return updatedPrixUnitaireHTNet;
      });

      // calculer Prix total Net HT
      let prixTotalNetHt = ligneFactures[index].quantite * prixUnitaireHtNet
      let prixTotalNetHtFormated = prixTotalNetHt.toFixed(3);
      setPrixTotalNetHT(prevPrixTotalNetHT => {
        const updatedPrixTotalNetHT = [...prevPrixTotalNetHT];
        updatedPrixTotalNetHT[index] = prixTotalNetHtFormated;
        return updatedPrixTotalNetHT;
      });

      if (tvas.length > index) {
        //calculer Prix total Net TTC
        const prixTotalNetTttc = ((tvas[index] / 100) * prixTotalNetHt) + prixTotalNetHt;
        const prixTotalNetTttcFormated = prixTotalNetTttc.toFixed(3);

        setPrixTotalNetTTC(prevPrixTotalNetTTC => {
          const updatedPrixTotalNetTTC = [...prevPrixTotalNetTTC];
          updatedPrixTotalNetTTC[index] = prixTotalNetTttcFormated;
          return updatedPrixTotalNetTTC;
        });
      }

    } else {
      const updatedLigneFactures = [...ligneFactures];
      updatedLigneFactures[index] = {
        ...updatedLigneFactures[index],
        prixUnitaire: 0,
      };
      setLigneFactures(updatedLigneFactures);
      initializeObject(index);
    }
  };



  const calculerPrixUnitaire = async (index, idLigneAchat) => {

    if (idLigneAchat !== "0") {

      const idProduit = idProduits[index];
      const produit = produits.find(produit => produit.id === idProduit);
      const ligneAchat = ligneAchats[index].find(ligneAchat => parseInt(ligneAchat.id) === parseInt(idLigneAchat));
      let pu = (produit.marge * ligneAchat.prixUnitaire) + ligneAchat.prixUnitaire;
      let puFormated = pu.toFixed(3);

      setLigneFactures(() => {
        const updatedLigneFactures = [...ligneFactures];
        updatedLigneFactures[index] = {
          ...updatedLigneFactures[index],
          idLigneAchat: ligneAchat.id,
          prixUnitaire: puFormated,
        };
        return updatedLigneFactures;
      });


      // calculer Prix Unitaire HT Net
      setPrixUnitaireHTNet(prevPrixUnitaireHTNet => {
        const updatedPrixUnitaireHTNet = [...prevPrixUnitaireHTNet];
        updatedPrixUnitaireHTNet[index] = puFormated;
        return updatedPrixUnitaireHTNet;
      });

      let prixTotalNetHt = ligneFactures[index].quantite * pu

      let prixTotalNetHtFormated = prixTotalNetHt.toFixed(3);

      setPrixTotalNetHT(prevPrixTotalNetHT => {
        const updatedPrixTotalNetHT = [...prevPrixTotalNetHT];
        updatedPrixTotalNetHT[index] = prixTotalNetHtFormated;
        return updatedPrixTotalNetHT;
      });

      if (tvas.length > index) {

        const prixTotalNetTttc = ((tvas[index] / 100) * prixTotalNetHt) + prixTotalNetHt;
        const prixTotalNetTttcFormated = prixTotalNetTttc.toFixed(3);

        setPrixTotalNetTTC(prevPrixTotalNetTTC => {
          const updatedPrixTotalNetTTC = [...prevPrixTotalNetTTC];
          updatedPrixTotalNetTTC[index] = prixTotalNetTttcFormated;
          return updatedPrixTotalNetTTC;
        });
      }


    } else {
      const updatedLigneFactures = [...ligneFactures];
      updatedLigneFactures[index] = {
        ...updatedLigneFactures[index],
        prixUnitaire: 0,
      };
      setLigneFactures(updatedLigneFactures);
      initializeObject(index);

    }

  };


  const calculerPrixTotalNetHT = (index, quantiteValue) => {
    const updatedLigneFactures = [...ligneFactures];
    updatedLigneFactures[index] = {
      ...updatedLigneFactures[index],
      quantite: quantiteValue,
    };

    const selectedLigneAchat = ligneAchats[index].find(
      (ligneAchat) => ligneAchat.id === updatedLigneFactures[index].idLigneAchat
    );

    if (quantiteValue <= selectedLigneAchat.quantiteStock) {
      setLigneFactures(updatedLigneFactures);

      let prixTotalNetHt = parseFloat(quantiteValue) * prixUnitaireHTNet[index]
      const prixTotalNetHtFormated = prixTotalNetHt.toFixed(3)

      const updatedPrixTotalNetHt = [...prixTotalNetHT];

      updatedPrixTotalNetHt[index] = prixTotalNetHtFormated;

      setPrixTotalNetHT(updatedPrixTotalNetHt)

      calculPrixTotalNetTTC(index, prixTotalNetHt);
      setQuantityError('');
    } else {
      setQuantityError(`La quantité ne peut pas dépasser ${selectedLigneAchat.quantiteStock}`);
    }

  };

  const calculPrixTotalNetTTC = (index, prixTotalNetHt) => {

    const updatedPrixTotalNetTTC = [...prixTotalNetTTC];

    const prixTotalNetTttc = ((tvas[index] / 100) * prixTotalNetHt) + prixTotalNetHt;
    const prixTotalNetTttcFormated = prixTotalNetTttc.toFixed(3);

    updatedPrixTotalNetTTC[index] = prixTotalNetTttcFormated;

    setPrixTotalNetTTC(updatedPrixTotalNetTTC);

  };

  const handleRemisesChange = (index, tauxRemise) => {

    const updatedPrixUnitaireHTNet = [...prixUnitaireHTNet];
    let valuePrixUnitaireHt = ligneFactures[index].prixUnitaire;
    let prixUnitaireHtNet = parseFloat(valuePrixUnitaireHt - (valuePrixUnitaireHt * (tauxRemise / 100))).toFixed(3);
    updatedPrixUnitaireHTNet[index] = prixUnitaireHtNet;
    setPrixUnitaireHTNet(updatedPrixUnitaireHTNet);

    const updatedLigneFactures = [...ligneFactures];
    updatedLigneFactures[index] = {
      ...updatedLigneFactures[index],
      tauxRemise: parseInt(tauxRemise),
    };
    setLigneFactures(updatedLigneFactures);


    // Calculate Prix total Net HT
    const prixTotalNetHt = ligneFactures[index].quantite * prixUnitaireHtNet;
    const formattedPrixTotalNetHt = prixTotalNetHt.toFixed(3);

    const updatedPrixTotalNetHt = [...prixTotalNetHT];
    updatedPrixTotalNetHt[index] = formattedPrixTotalNetHt;
    setPrixTotalNetHT(updatedPrixTotalNetHt);

    // Calculate Prix total Net TTC
    const prixTotalNetTttc = ((parseInt(tvas[index]) / 100) * prixTotalNetHt) + prixTotalNetHt;
    const formattedPrixTotalNetTttc = prixTotalNetTttc.toFixed(3);

    const updatedPrixTotalNetTTC = [...prixTotalNetTTC];
    updatedPrixTotalNetTTC[index] = formattedPrixTotalNetTttc;
    setPrixTotalNetTTC(updatedPrixTotalNetTTC);

  };
  // -------------
  const updateSaveLigneFactures = newFactureId => {
    const updatedSaveLigneFactures = addLignesFacture.map((addLignesFacture) => {
      return {
        ...addLignesFacture,
        factureId: newFactureId
      };
    });
    console.log("addLignesFacture zzzz", addLignesFacture)
    setSaveLigneFactures(updatedSaveLigneFactures);
  };

  // ************ get request
  const handleGetFactureById = async (id) => {
    try {
      const resp = await getFactureById(id);
      setFacture(resp.data);

    } catch (error) {
      console.error("Error retrieving factures:", error);
    }
  };

  const handleGetLigneFactureIdFacture = async (id) => {
    try {
      const resp = await getLigneFactureIdFacture(id);
      setLigneFactures(resp.data);
    } catch (error) {
      console.error("Error retrieving factures:", error);
    }
  };

  const handleGetTva = async (index, id) => {

    if (id !== "0") {
      try {
        const prod = produits.find((produit) => parseInt(produit.id) === parseInt(id));
        const idTva = prod.tvaId;
        const respTva = await getTvaById(idTva);
        const convertTva = respTva.data.valeur * 10;
        setTvas((prevTvas) => {
          const updatedTvas = [...prevTvas];
          updatedTvas[index] = convertTva;
          return updatedTvas;
        });
      } catch (error) {
        console.error("Error retrieving Tva:", error);
      }
    }
  };

  const handleGetProduits = async () => {
    try {
      const resp = await getProductBySociete(societeIdStored);
      setProduits(resp.data);
    } catch (error) {
      console.error("Error retrieving produits:", error);
    }
  };

  const handleGetLigneAchats = async (index, id) => {
    if (id !== "0") {
      try {
        const respLigneAchats = await getAllByProduit(id);
        // const updatedLigneAchats = respLigneAchats.data.filter(
        //   (ligneAchat) => ligneAchat.quantiteStock > 0
        // );

        setLigneAchats((prevLigneAchat) => {
          const updatedLigneAchatsArray = [...prevLigneAchat];
          updatedLigneAchatsArray[index] = respLigneAchats.data;
          return updatedLigneAchatsArray
        }
        )

      } catch (error) {
        console.error("Error retrieving LigneAchats:", error);
      }
    }
  };

  const handleGetClients = async () => {
    try {
      const resp = await getClientBySociete(societeIdStored);
      setClients(resp.data);
    } catch (error) {
      console.error("Error retrieving produits:", error);
    }
  };



  // ********** buttun
  const handleAddLigneFacture = () => {
    const newLigneFacture = {

      idLigneAchat: 0,
      quantite: 1,
      prixUnitaire: 0,
      factureId: 0,
      produitId: 0,
      tauxRemise: 0
    };
    const newLigneAchats = [...ligneAchats, []];
    const newtvas = [...tvas, 0]
    const newPrixUnitaireHTNet = [...prixUnitaireHTNet, 0]
    const newPrixTotalNetHT = [...prixTotalNetHT, 0]
    const newPrixTotalNetTTC = [...prixTotalNetTTC, 0]

    setPrixUnitaireHTNet(newPrixUnitaireHTNet);
    setPrixTotalNetHT(newPrixTotalNetHT);
    setPrixTotalNetTTC(newPrixTotalNetTTC)

    setTvas(newtvas);
    setLigneAchats(newLigneAchats);
    setLigneFactures(prevLigneFactures => [...prevLigneFactures, newLigneFacture]);
  };

  const handleDeleteLigneFacture = (index) => {



    if (ligneFactures[index].id) {
      deleteLigneFacture(ligneFactures[index]).then((resp) => {
        setLigneFactures(prevLigneFactures => {
          const updatedLigneFactures = [...prevLigneFactures];
          updatedLigneFactures.splice(index, 1);
          return updatedLigneFactures;
        });

        setPrixTotalNetTTC((prevPrixTotalNetTTC) => {
          const updatePrixTotalNetTTC = [...prevPrixTotalNetTTC];
          updatePrixTotalNetTTC.splice(index, 1);
          return updatePrixTotalNetTTC;
        });

        setPrixTotalNetHT((prevPrixTotalNetHT) => {
          const updatePrixTotalNetHT = [...prevPrixTotalNetHT];
          updatePrixTotalNetHT.splice(index, 1);
          return updatePrixTotalNetHT;
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
      setLigneFactures(prevLigneFactures => {
        const updatedLigneFactures = [...prevLigneFactures];
        updatedLigneFactures.splice(index, 1);
        return updatedLigneFactures;
      });

      setPrixTotalNetTTC((prevPrixTotalNetTTC) => {
        const updatePrixTotalNetTTC = [...prevPrixTotalNetTTC];
        updatePrixTotalNetTTC.splice(index, 1);
        return updatePrixTotalNetTTC;
      });

      setPrixTotalNetHT((prevPrixTotalNetHT) => {
        const updatePrixTotalNetHT = [...prevPrixTotalNetHT];
        updatePrixTotalNetHT.splice(index, 1);
        return updatePrixTotalNetHT;
      });
    }
  };



  // ******************* update
  const handleUpdateFacture = (event) => {
    event.preventDefault();
    const isValid = ligneFactures.every((ligneFacture) => {
      return (
        parseFloat(ligneFacture.quantite) > 0 &&
        parseFloat(ligneFacture.prixUnitaire) > 0 &&
        parseInt(ligneFacture.produitId) > 0 &&
        parseInt(ligneFacture.idLigneAchat) > 0 &&
        parseInt(ligneFacture.tauxRemise) >= 0
      );
    });

    if (!isValid) {
      toast.error("Invalid ligneFactures data", {
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

    updateFacture(facture).then((resp) => {
      console.log("facture.id", facture.id)
      const updatedLigneFactures = [];
      const addLigneFactures = [];

      ligneFactures.forEach(item => {
        if (item.id) {
          updatedLigneFactures.push(item);
        } else {
          addLigneFactures.push(item);
        }
      });

      setAddLignesFacture(addLigneFactures)
      setUpdatedLignesFacture(updatedLigneFactures)
      setStateAddLignesFacture(true)
      toast.success("Mise à jour terminée avec succès", {
        position: "top-center",
        autoClose: 400,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        icon: <FontAwesomeIcon icon={faCheckCircle} style={{ color: 'green' }} />,
      });

      // setTimeout(() => {
      //   navigate("/Factures");
      // }, 1000);

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



  const updateListLignesFactures = () => {

    updateListLigneFacture(updatedLignesFacture).then((resp) => {
      setStateUpdatedLignesFactur(true)
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

      setLigneFactures([{
        quantite: 1,
        prixUnitaire: 0,
        factureId: 0,
        produitId: 0,
        tauxRemise: 0,
        idLigneAchat: 0
      }]);

    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement du facture";
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
  const saveListLigneFactures = () => {
    console.log("aaaaaaaaaaaaaa saveLigneFactures", saveLigneFactures)
    saveListLigneFacture(saveLigneFactures).then((resp) => {

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

      setLigneFactures([{
        quantite: 1,
        prixUnitaire: 0,
        factureId: 0,
        produitId: 0,
        tauxRemise: 0,
        idLigneAchat: 0
      }]);

    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "Erreur lors de l'enregistrement du facture";
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
          <h2>Modifier Facture</h2>
        </div>
        <div className="card-body">
          <div className="row">
            <form onSubmit={handleUpdateFacture} method="post">
              <div className="row">
                <div className="col-auto">
                  <label htmlFor="label" className="form-label">
                    Numéro de facture
                  </label>
                  <input
                    id="numeroFacture"
                    type="text"
                    className="form-control"
                    value={facture.numeroFacture}
                    required
                    readOnly
                  />
                </div>
                <div className="col-auto">
                  <label htmlFor="date" className="form-label">
                    Date
                  </label>
                  <input
                    id="date"
                    type="date"
                    className="form-control"
                    value={facture.date}
                    onChange={(e) => setFacture((prevFacture) => ({ ...prevFacture, date: e.target.value }))}
                    required
                  />
                </div>
                <div className="col-auto">
                  <label htmlFor="statut" className="form-label">
                    Statut
                  </label>
                  <select
                    id="statut"
                    className="form-select"
                    value={facture.statut}
                    onChange={(e) => setFacture((prevFacture) => ({ ...prevFacture, statut: e.target.value }))}
                    required
                  >
                    <option value="">Select a statut</option>
                    <option value="paye">Payé</option>
                    <option value="nonPayer">Non Payé</option>
                    <option value="enours">En cours</option>
                  </select>
                </div>
                <div className="col-auto">
                  <label htmlFor="modePaiement" className="form-label">
                    Mode de paiement
                  </label>
                  <select
                    id="modePaiement"
                    className="form-select"
                    value={facture.modePaiement || "aDefinir"}
                    onChange={(e) => setFacture((prevFacture) => ({ ...prevFacture, modePaiement: e.target.value }))}
                    required
                  >
                    <option value="">Sélectionnez le mode de paiement</option>
                    {facture.statut === "paye" && (
                      <>
                        <option value="carteCredit">Carte crédit</option>
                        <option value="cheque">Chèque</option>
                        <option value="espece">Espèce</option>
                        <option value="traite">Traite</option>
                        <option value="virement">Virement</option>
                      </>
                    )}
                    {(facture.statut === "nonPayer" || facture.statut === "enours") && (
                      <option value="aDefinir">A définir</option>
                    )}
                  </select>
                </div>
                <div className="col-auto">
                  <label htmlFor="clientId" className="form-label">
                    Client
                  </label>
                  <select
                    id="clientId"
                    className="form-select"
                    value={facture.clientId}
                    onChange={(e) =>
                      setFacture((prevFacture) => ({
                        ...prevFacture,
                        clientId: parseInt(e.target.value),
                      }))
                    }
                    required
                  >
                    <option value="">Select a client</option>
                    {clients.map((client) => (
                      <option key={client.id} value={client.id}>
                        {client.nom}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="col-auto">
                  <label htmlFor="totalNetHT" className="form-label">
                    Total Net HT
                  </label>
                  <input
                    id="totalNetHT"
                    type="number"
                    className="form-control"
                    value={totalNetHT}
                    readOnly
                    required
                  />
                </div>
                <div className="col-auto">
                  <label htmlFor="totalNetTTC" className="form-label">
                    Total Net TTC
                  </label>
                  <input
                    id="totalNetTTC"
                    type="number"
                    className="form-control"
                    value={totalNetTTC}
                    readOnly
                    required
                  />
                </div>

                <div className="col-12">
                  <label htmlFor="commentaires" className="form-label">
                    Commentaires
                  </label>
                  <textarea
                    id="commentaires"
                    className="form-control"
                    value={facture.commentaires}
                    onChange={(e) => setFacture((prevFacture) => ({ ...prevFacture, commentaires: e.target.value }))}
                  ></textarea>
                </div>
              </div>
              {/* ************************************************************* */}
              <hr />

              <div>
                <div className="d-flex justify-content-end">
                  <button id="addLigneFacture" className="btn btn-success" type="button" onClick={handleAddLigneFacture}>+</button>
                </div>

                {ligneFactures.map((ligneFacture, index) => (
                  <div className="row" id={`ligneFacture-${index + 1}`} key={`ligneFacture-${index + 1}`}>
                    <hr />

                    <div className="col-auto">
                      <label htmlFor={`produitId-${index + 1}`} className="form-label">
                        Produits
                      </label>
                      <select
                        id={`produitId-${index + 1}`}
                        className="form-select"
                        value={ligneFacture.produitId}
                        onChange={(e) => {
                          initializeObject(index);
                          handleProduitIdChange(index, e.target.value);
                          handleGetLigneAchats(index, e.target.value);
                          handleGetTva(index, e.target.value);
                        }}
                        required
                      >
                        <option value="0">Select a produit</option>
                        {produits.map((produit) => (
                          <option key={produit.id} value={produit.id}>
                            {produit.label} - {produit.description} - QteStock: {produit.quantite} - marge: {produit.marge * 100}%
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="col-auto">
                      <label htmlFor={`ligneAchatPrixUnitaire-${index + 1}`} className="form-label">
                        Prix d'achat par fournisseur
                      </label>
                      <select
                        id={`ligneAchatPrixUnitaire-${index + 1}`}
                        className="form-select"
                        value={ligneFacture.idLigneAchat}
                        onChange={(e) => {
                          calculerPrixUnitaire(index, e.target.value)
                        }}
                        required
                      >
                        <option value="0">Select Prix</option>
                        {ligneAchats[index] && ligneAchats[index].map((ligneAchat) => (
                          <option key={ligneAchat.id} value={ligneAchat.id}>
                            {ligneAchat.fournisseurNom} - prix: {ligneAchat.prixUnitaire} Dt - Stq: {ligneAchat.quantiteStock}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="col-2">
                      <label htmlFor={`prixUnitaire-${index + 1}`} className="form-label">
                        Prix Unitaire HT
                      </label>
                      <input
                        id={`prixUnitaire-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={ligneFacture.prixUnitaire}
                        readOnly
                        required
                      />
                    </div>

                    <div className="col-1">
                      <label htmlFor={`remise-${index + 1}`} className="form-label">
                        Remise %
                      </label>
                      <input
                        id={`remise-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={ligneFacture.tauxRemise}
                        onChange={(e) => handleRemisesChange(index, e.target.value)}
                        required
                        min={0}
                      />
                    </div>

                    <div className="col-auto">
                      <label htmlFor={`prixNet-${index + 1}`} className="form-label">
                        Prix Unitaire HT Net
                      </label>
                      <input
                        id={`prixNet-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={prixUnitaireHTNet[index]}
                        readOnly
                        required
                      />
                    </div>

                    <div className="col-auto">
                      <label htmlFor={`quantite-${index + 1}`} className="form-label">
                        Quantité
                      </label>
                      <input
                        id={`quantite-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={ligneFacture.quantite}
                        onChange={(e) => calculerPrixTotalNetHT(index, e.target.value)}
                        min={0}
                        required
                      />
                      {quantityError && <p className="text-danger">{quantityError}</p>}
                    </div>

                    <div className="col-auto">
                      <label htmlFor={`prixNet-${index + 1}`} className="form-label">
                        Prix total Net HT
                      </label>
                      <input
                        id={`prixNet-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={prixTotalNetHT[index]}
                        readOnly
                        required
                      />
                    </div>

                    <div className="col-1">
                      <label htmlFor={`prixNet-${index + 1}`} className="form-label">
                        TVA %
                      </label>
                      <input
                        id={`prixNet-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={tvas[index] || ""}
                        readOnly
                        required
                      />
                    </div>

                    <div className="col-auto">
                      <label htmlFor={`prixNet-${index + 1}`} className="form-label">
                        Prix total Net TTC
                      </label>
                      <input
                        id={`prixNet-${index + 1}`}
                        type="number"
                        className="form-control"
                        value={prixTotalNetTTC[index]}
                        readOnly
                        required
                      />
                    </div>

                    <div className="col-auto align-self-end">
                      <button id="deleteLigneFacture" className="btn btn-danger" type="button" onClick={() => handleDeleteLigneFacture(index)}>-</button>
                    </div>
                  </div>
                ))}
              </div>

              <hr />
              <button className="btn btn-primary">Mise à jour</button>
            </form>
          </div>
        </div>
      </div >
    </div >
  );
}
export default ModifierFacture;
