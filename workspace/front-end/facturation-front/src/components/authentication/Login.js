import React, { useState } from "react";
import emailValidator from '../helper/EmailValidator'
// import nameValidator from '../helper/NameValidator'
import passwordValidator from '../helper/PasswordValidator'
import loginUser from './authentication'
import { useNavigate } from "react-router-dom";
import { Link, } from "react-router-dom";
import { activeCnx } from "../sharedDataLocalStorage/cnxStateStorage";
import { saveToken } from "../sharedDataLocalStorage/TokenStorage";
import { saveUserData } from "../sharedDataLocalStorage/UserStorage";
import { getAbonnementById } from "../../repository/AbonnementsRepository";
import { getSocieteById } from "../../repository/societeRepository";
import { saveAbonnementData } from "../sharedDataLocalStorage/AbonnementStorage";
import { saveSocieteData } from "../sharedDataLocalStorage/societeStorage";


function Login() {
    const [email, setEmail] = useState({
        value: '',
        error: ''
    });
    const [password, setPassword] = useState({
        value: '',
        error: ''
    });
    // const [name, setName] = useState({
    //     value: '',
    //     error: ''
    // });

    const [error, setError] = useState();
    // const [loading, setLoading] = useState(true);
    // const [validate, setValidate] = useState(false);

    const navigate = useNavigate();

    const redirect = async (roleAraay, data) => {
        let role = "";
        if (roleAraay) {
            role = roleAraay.includes("SUPER ADMIN") ? "SUPER ADMIN" :
                roleAraay.includes("ADMIN") ? "ADMIN" :
                    roleAraay.includes("OPERATEUR") ? "OPERATEUR" : "";
        }

        if (role === "ADMIN") {
            const idAbonnement = data[1].abonnementId
            const idSociete = data[1].societeId
            const abonnement = await getAbonnementById(idAbonnement);
            const societe = await getSocieteById(idSociete);
            console.log("societe ", societe.data)
            saveAbonnementData(abonnement.data);
            saveSocieteData(societe.data);
            setTimeout(() => {
                navigate("/produits");
            }, 100)
        } else if (role === "OPERATEUR") {
            const idSociete = data[1].societeId
            const societe = await getSocieteById(idSociete);
            saveSocieteData(societe.data);
            setTimeout(() => {
                navigate("/produits");
            }, 100)
        }

        else if (role === "SUPER ADMIN") {
            setTimeout(() => {
                navigate("/Abonnement");
            }, 100)
        }

        else {
            setTimeout(() => {
                navigate("/");
            }, 100)
        }

    }


    const onLoginPrest = async (event) => {
        event.preventDefault();
        const emailError = emailValidator(email.value);
        const passwordError = passwordValidator(password.value);
        // if (emailError || passwordError) {
        //     setEmail({ ...email, error: emailError })
        //     setPassword({ ...password, error: passwordError })
        //     return
        // }

        try {
            console.log(email.value)
            console.log(password.value)
            const response = await loginUser(email.value, password.value);
            const data = response.data;

            const token = data[0].Bearer;
            const userData = data[1];
            console.log("userData ", userData);

            saveToken(token);
            activeCnx();
            saveUserData(userData);



            const roleAraay = userData.rolesNom;
            redirect(roleAraay, data);

        } catch (error) {
            console.error(error);
            const message = error.response.data.message;
            if (message === "Abonnement expire") {
                console.log(error.response.data.abonnementId)
                console.log(error.response.data.message)
                setError(message)
                const abonnementId = error.response.data.abonnementId;
                navigate(`/AjouterAbonnement/${abonnementId}`);
                return
            }
            setError("vérifier votre e-mail et votre mot de passe")
        }
    };

    return (
        <div className="container">
            <br />

            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-8">
                        <img src={process.env.PUBLIC_URL + '/images/cnx.png'} alt="Image Description" />
                    </div>
                    <div className="col-4 d-flex flex-column align-items-center justify-content-center">
                        <div className="card mb-3">
                            <div className="card-body">
                                <div className="pt-4 pb-2">
                                    <h5 className="card-title text-center pb-0 fs-4">Connectez-vous à votre compte</h5>
                                    <p className="text-center small">Entrez votre email et votre mot de passe pour vous connecter</p>
                                </div>
                                <p style={{ color: 'red' }}>
                                    {error}
                                </p>
                                <form className="row g-3 needs-validation" onSubmit={onLoginPrest}>
                                    <div className="col-12">
                                        <label htmlFor="yourEmail" className="form-label"> Email </label>
                                        <input type="email"
                                            name="email"
                                            className="form-control"
                                            id="yourEmail"
                                            value={email.value}
                                            onChange={(e) => setEmail({ value: e.target.value, error: '' })}
                                            required />
                                    </div>
                                    <p>
                                        {email.error}
                                    </p>
                                    <div className="col-12">
                                        <label htmlFor="yourPassword" className="form-label">Mot de passe</label>
                                        <input type="password" name="password" className="form-control" id="yourPassword" required
                                            value={password.value}
                                            onChange={(e) => setPassword({ value: e.target.value, error: '' })}

                                        />
                                    </div>
                                    <p>
                                        {password.error}
                                    </p>
                                    <div className="col-12">
                                        <button className="btn btn-primary w-100" >Login</button>
                                    </div>
                                </form>
                                <br />
                                <div className="d-flex justify-content-center align-items-center">
                                    Vous n'avez pas de compte?
                                    <Link to="/Inscrire">
                                        <a> Inscrire</a>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>



        </div>
    )
}

export default Login