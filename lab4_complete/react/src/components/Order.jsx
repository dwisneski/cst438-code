import { useState } from "react";

// POST new order.  Display any validation error messages

function Order() {

    const [order, setOrder] = useState({ item: "", quantity: 0, price: 0.0 });

    const [message, setMessage] = useState([]);  // validation messages

    const onChange = (event) => {
        setOrder({ ...order, [event.target.name]: event.target.value });
    };

    const onClick = async () => {
        const response = await fetch(`http://localhost:8080/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem("jwt"),
            },
            body: JSON.stringify(order),
        });

        if (response.ok) {
            const data = await response.json();
            alert("Order successful order id " + data.orderId);
            setMessage([]);
        } else if (response.status === 400) {
            const data = await response.json();
            if ('errors' in data) {
                setMessage(data.errors.map((error) => <p key={error.defaultMessage}>{error.defaultMessage}</p>));
            }
        } else {
            alert("Order failed");
        }
    };

    return (
        <>
            <h2>Order</h2>
            {message}
            <input id="item" type="text" name="item" placeholder="item" onChange={onChange} />
            <input id="quantity" type="number" name="quantity" placeholder="quantity" onChange={onChange} />
            <input id="price" type="number" name="price" placeholder="price" onChange={onChange} />
            <button id="orderButton" onClick={onClick}>order</button>
        </>
    );
}

export default Order;
