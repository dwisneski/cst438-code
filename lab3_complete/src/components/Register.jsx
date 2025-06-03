import React, { useState } from 'react';

function Register() {

    const [customer, setCustomer] = useState({ name: '', email: '', password: '' });

    const onChange = (event) => {
        setCustomer({ ...customer, [event.target.name]: event.target.value });
    }

    const submit = async () => {
        const response = await fetch('http://localhost:8080/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(customer)
        });
        if (response.ok) {
            const data = await response.json();
            alert("Registration successful customer id " + data.customerId);
        } else {
            alert("Registration failed " + response.status);
        }
    }

    return (
        <div>
            <h1>Register</h1>
            <div>
                <input id="name" type="text" label="name" name="name" placeholder="Username" onChange={onChange} />
                <input id="email" type="email" label="email" name="email" placeholder="Email" onChange={onChange} />
                <input id="password" type="text" label="password" name="password" placeholder="Password" onChange={onChange} />
                <button id="registerButton" type="submit" onClick={submit}>Register</button>
            </div>
        </div>
    )

}

export default Register;