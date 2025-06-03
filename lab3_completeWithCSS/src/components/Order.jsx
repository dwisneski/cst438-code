import { useState, useContext } from "react";
import { UserContext } from "../App";

function Order() {
  const [order, setOrder] = useState({ item: "", quantity: 0, price: 0.0 });
  const [message, setMessage] = useState([]);

  const [user, setUser] = useContext(UserContext);

  const onChange = (event) => {
    setOrder({ ...order, [event.target.name]: event.target.value });
  };

  const placeOrder = async () => {
    const response = await fetch(`http://localhost:8080/customers/${user.customerId}/orders`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: user.jwtToken,
      },
      body: JSON.stringify(order),
    });
    const data = await response.json();
    if (response.status === 200) {
      alert("Order successful order id " + data.orderId);
      setMessage([]);
    } else if (response.status === 400 && "errors" in data) {
      setMessage(data.errors.map((error) => error.defaultMessage));
    } else {
      alert("Order failed");
    }
  };

  return (
    <div>
      <h1>Order</h1>
      {
        (message.length > 0)
          ? (<div className="errorMessage">
            <ul> {message.map((msg) => (<li key={msg}> {msg} </li>))} </ul>
          </div>
          )
          : null
      }
      <div className="singleCol" >
        <input id="item" type="text" label="item" name="item" placeholder="item" onChange={onChange} />
        <input id="quantity" type="number" label="quanity" name="quantity" placeholder="quantity 0" onChange={onChange} />
        <input id="price" type="number" label="price" name="price" placeholder="price 0.00" onChange={onChange} />
        <button id="order_button" type="submit" onClick={placeOrder}> order </button>
      </div>
    </div>
  );
}

export default Order;
