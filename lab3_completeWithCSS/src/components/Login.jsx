import { useState, useContext } from 'react';
import { UserContext } from '../App';

function Login() {

    const [customer, setCustomer] = useState({ email: '', password: '' });
    const [user, setUser] = useContext(UserContext);

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

        if (response.status === 200) {
            const data = await response.json();
            alert("Login successful " + data.customerId);
            setUser({ customerId: data.customerId, jwtToken: 'Bearer ' + data.jwtToken });

        } else {
            alert("Login failed");
        }
    }

    return (
        <div >
            <h1>Login</h1>
            <div className="singleCol">
                <input id="email" type="email" label="email" name="email" placeholder="Email" onChange={onChange} />
                <input id="password" type="text" label="password" name="password" placeholder="Password" onChange={onChange} />
                <button id="loginButton" type="submit" onClick={submit}>Login</button>
            </div>
        </div>
    )

}

export default Login;