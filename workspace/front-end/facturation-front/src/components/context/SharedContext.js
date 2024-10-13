import React, { createContext, useState, useEffect } from 'react';

export const UserContext = createContext();
export const SubscriptionContext = createContext();

export const ContextProvider = ({ children }) => {
    const [userData, setUserData] = useState(null);
    const [subscriptionData, setSubscriptionData] = useState(null);

    useEffect(() => {
        const storedUserData = localStorage.getItem("userData");
        if (storedUserData) {
            setUserData(JSON.parse(storedUserData));
        }
    }, [localStorage]);

    return (
        <UserContext.Provider value={{ userData, setUserData }}>
            <SubscriptionContext.Provider value={{ subscriptionData, setSubscriptionData }}>
                {children}
            </SubscriptionContext.Provider>
        </UserContext.Provider>
    );
};
