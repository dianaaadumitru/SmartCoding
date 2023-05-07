import React, { useState } from "react"

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function sendLoginRequest() {
        console.log("sending a request")
    }


    return (
        <>
            <div>
                <label htmlFor="username">Username or email</label>
                <input 
                    type="email" 
                    id="username" 
                    value={username} 
                    onChange={(event) => setUsername(event.target.value)}
                />
            </div>
            <div>
                <label htmlFor="password">Password</label>
                <input 
                    type="password" 
                    id="password" 
                    value={password} 
                    onChange={(event) => setPassword(event.target.value)}
                />
            </div>
            <div>
                <button id="submit" type="button" onClick={() => sendLoginRequest()}>Login</button>
            </div>
        </>
    );
}

export default Login;



