import { useState } from 'react';

function Register() {

    const [customer, setCustomer] = useState({ name: '', email: '', password: '' });

    const changeValue = (e) => {
        setCustomer({ ...customer, [e.target.name]: e.target.value });
    }

    const onClick = async () => {
        try {
            const response = await fetch("http://localhost:8080/register",
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(customer)

                }
            );
            if (response.ok) {
                const data = await response.json();
                alert("You are now registered as customer id " + data.customerId);
            } else {
                alert("Registration failed. " + response.status);
            }
        } catch (err) {
            alert("request failed " + err)
        }
    }

    return (
        <>
            <h2>Register as new customer</h2>
            <input id="name" type="text" name="name" placeholder="name" value={customer.name} onChange={changeValue} />
            <input id="email" type="email" name="email" placeholder="email" value={customer.email} onChange={changeValue} />
            <input id="password" type="password" name="password" placeholder="password" value={customer.password} onChange={changeValue} />
            <button id="registerButton" onClick={onClick}>Register</button>
        </>
    )

}

export default Register;