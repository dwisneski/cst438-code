import { useState } from 'react';

// prompt user for email and password
// on login success, store jwtToken in localStorage 

function Login() {

    const [customer, setCustomer] = useState({ email: '', password: '' });

    const onChange = (event) => {
        setCustomer({ ...customer, [event.target.name]: event.target.value });
    }

    const submit = async () => {
        const response = await fetch('http://localhost:8080/login', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Basic ' + btoa(customer.email + ':' + customer.password),
            },
        });

        if (response.ok) {
            const data = await response.json();
            alert("Login successful " + data.customerId);
            localStorage.setItem("jwt", 'Bearer ' + data.token);
        } else {
            alert("Login failed");
        }
    }

    return (
        <div >
            <h2>Login</h2>
            <div>
                <input id="email" type="email" name="email" placeholder="email" onChange={onChange} />
                <input id="password" type="password" name="password" placeholder="password" onChange={onChange} />
                <button id="loginButton" onClick={submit}>Login</button>
            </div>
        </div>
    )

}

export default Login;