import './App.css';
import { BrowserRouter, Link, Route, Routes, } from "react-router-dom";
import { useEffect, useState } from "react";
import ModifierProduit from "./components/produits/ModifierProduit";
import AjouterProduit from "./components/produits/AjouterProduit";
import Accueil from "./components/Accueil";
import Produits from "./components/produits/Produits";
import "bootstrap/dist/css/bootstrap.min.css";
import Fournisseurs from './components/fournisseur/Fournisseurs';
import ModifierFournisseur from './components/fournisseur/ModifierFournisseur';
import AjouterFournisseur from './components/fournisseur/AjouterFournisseur';
import Factures from './components/factures/Factures';
import AjouterFacture from './components/factures/AjouterFacture';
import ModifierFacture from './components/factures/ModifierFacture';
import Login from './components/authentication/Login';
import Inscrire from './components/authentication/Inscrire';
import Operateurs from './components/operateurs/Operateurs';
import AjouterOperateur from './components/operateurs/AjouterOperateur';
import ModifierOperateur from './components/operateurs/ModifierOperateur';
import Clients from './components/clients/Clients';
import AjouterClient from './components/clients/AjouterClient';
import ModifierClient from './components/clients/ModifierClient';
import Tvas from './components/tvas/Tvas';
import AjouterTva from './components/tvas/AjouterTva';
import ModifierTva from './components/tvas/ModifierTva';
import AjouterSociete from './components/societe/AjouterSociete';
import AjouterAbonnement from './components/abonnement/AjouterAbonnement';
import Abonnement from './components/abonnement/Abonnement';
import { userRolesStored } from './components/sharedDataLocalStorage/UserStorage';
import Logout from './components/authentication/Logout';
import { disableCnx, getCnxData } from './components/sharedDataLocalStorage/cnxStateStorage';
import ModifierSocie from './components/societe/ModifierSocie';


// import { useNavigate } from "react-router-dom";

function App() {


  let role = "";
  let actions = [];
  let accueils = [];
  let logout = [];
  let logins = [];

  if (userRolesStored) {
    role = userRolesStored.includes("SUPER ADMIN") ? "SUPER ADMIN" :
      userRolesStored.includes("ADMIN") ? "ADMIN" :
        userRolesStored.includes("OPERATEUR") ? "OPERATEUR" : "";
  }

  if (role === "ADMIN") {
    actions = ["Produits", "Fournisseurs", "Factures", "Clients", "Operateurs"];
    logout = ["Compte", "Société", "Déconnexion"];
  }
  if (role === "SUPER ADMIN") {
    actions = ["Abonnement"];
    logout = ["Compte", "Déconnexion"];
  }

  if (role === "OPERATEUR") {
    actions = ["Produits", "Fournisseurs", "Factures", "Clients"];
    logout = ["Compte", "Déconnexion"];
  }





  const [currentAction, setCurrentAction] = useState();
  useEffect(() => {
    let currentAction = window.location.pathname;
    currentAction = currentAction.slice(1, currentAction.length);
    setCurrentAction(currentAction);
  }, []);
  return (
    // <ContextProvider>
    <BrowserRouter>
      {/* <nav className="navbar navbar-expand-lg navbar-light bg-light"> */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container-fluid">
          <ul style={{ marginRight: '20px', marginBottom: '9px' }}>
            <img src={process.env.PUBLIC_URL + '/images/logo2.png'} alt="Image Description" width={110} />
          </ul>
          <ul className="nav nav-pills">
            {accueils.map((accueil) => (
              <li key={accueil}>
                <Link
                  onClick={() => setCurrentAction(accueil)}
                  className={
                    currentAction === accueil || ((currentAction === "Accueil" || currentAction === "") && (accueil === "Accueil"))
                      ? "btn btn-warning ms-1"
                      : "btn btn-primary ms-1"
                  }
                  to={"/" + accueil}
                >
                  {accueil}
                </Link>
              </li>
            ))}
          </ul>
          <ul className="nav nav-pills">
            {actions.map((action) => (
              <li key={action}>
                <Link
                  onClick={() => setCurrentAction(action)}
                  className={
                    currentAction === action
                      ? "btn btn-warning ms-1"
                      : "btn btn-primary ms-1"
                  }
                  to={"/" + action}
                >
                  {action}
                </Link>
              </li>
            ))}
          </ul>
          <ul className="nav nav-pills ms-auto">
            {logins.map((login) => (
              <li key={login}>
                <Link
                  onClick={() => setCurrentAction(login)}
                  className={
                    currentAction === login
                      ? "btn btn-warning ms-1"
                      : "btn btn-primary ms-1"
                  }
                  to={"/" + login}
                >
                  {login}
                </Link>
              </li>
            ))}
          </ul>
          <ul className="nav nav-pills ms-auto">
            {logout.map((logout) => (
              <li key={logout}>
                <Link
                  onClick={() => setCurrentAction(logout)}
                  className={
                    currentAction === logout
                      ? "btn btn-warning ms-1"
                      : "btn btn-primary ms-1"
                  }
                  to={"/" + logout}
                >
                  {logout}
                </Link>
              </li>
            ))}
          </ul>
        </div>
      </nav>
      <Routes>
        <Route path="/" element={<Login />}></Route>
        <Route path="/Accueil" element={<Accueil />}></Route>
        <Route path="/Produits" element={<Produits />}></Route>
        <Route path="/AjouterProduit" element={<AjouterProduit />}></Route>
        <Route path="/ModifierProduit/:id" element={<ModifierProduit />}></Route>
        <Route path="/Fournisseurs" element={<Fournisseurs />}></Route>
        <Route path="/AjouterFournisseur" element={<AjouterFournisseur />}></Route>
        <Route path="/ModifierFournisseur/:id" element={<ModifierFournisseur />}></Route>
        <Route path="/Factures" element={<Factures />}></Route>
        <Route path="/AjouterFacture" element={<AjouterFacture />}></Route>
        <Route path="/ModifierFacture/:id" element={<ModifierFacture />}></Route>
        <Route path="/Operateurs" element={<Operateurs />}></Route>
        <Route path="/AjouterOperateur" element={<AjouterOperateur />}></Route>
        <Route path="/ModifierOperateur/:id" element={<ModifierOperateur />}></Route>
        <Route path="/Compte" element={<ModifierOperateur />}></Route>
        <Route path="/Clients" element={<Clients />}></Route>
        <Route path="/AjouterClient" element={<AjouterClient />}></Route>
        <Route path="/ModifierClient/:id" element={<ModifierClient />}></Route>
        <Route path="/Tva" element={<Tvas />}></Route>
        <Route path="/AjouterTva" element={<AjouterTva />}></Route>
        <Route path="/ModifierTva/:id" element={<ModifierTva />}></Route>
        <Route path="/AjouterSociete" element={<AjouterSociete />}></Route>
        <Route path="/Société" element={<ModifierSocie />}></Route>
        <Route path="/Abonnement" element={<Abonnement />}></Route>
        <Route path="/AjouterAbonnement/:id" element={<AjouterAbonnement />}></Route>
        <Route path="/Connexion" element={<Login />}></Route>
        <Route path="/Inscrire" element={<Inscrire />}></Route>
        <Route path="/Déconnexion" element={<Logout />}></Route>
      </Routes>
    </BrowserRouter>
    // </ContextProvider>
  );
}

export default App;
