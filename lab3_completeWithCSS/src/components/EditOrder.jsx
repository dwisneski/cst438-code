import React, { useState, useContext } from "react";
import { UserContext } from "../App";

function EditOrder({ data, updateOrders }) {
  const [order, setOrder] = useState({});
  const [message, setMessage] = useState([]);
  const [isOpen, setOpen] = useState(false);
  const [user, setUser] = useContext(UserContext);

  const openDialog = () => {
    setMessage('');
    setOrder(data);
    setOpen(true);
  };

  const closeDialog = () => {
    setOpen(false)
  };

  const onChange = (event) => {
    setOrder({ ...order, [event.target.name]: event.target.value });
  };

  const update = async () => {
    const response = await fetch(`http://localhost:8080/orders`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: user.jwtToken,
      },
      body: JSON.stringify(order),
    });
    const data = await response.json();
    if (response.status === 200) {
      alert("Order update successful");
      updateOrders();
      closeDialog();
    } else if (response.status === 400 && "errors" in data) {
      setMessage(data.errors.map((error) => error.defaultMessage));
    } else {
      alert("Order update failed");
    }
  };

  return (
    <div>
      <button onClick={openDialog}>
        Edit
      </button>
      {(isOpen) ? (
        <div >
          <dialog open>
            <div>
              <h1>Edit Order</h1>
              {
                (message.length > 0)
                  ? (<div className="errorMessage">
                    <ul> {message.map((msg) => (<li key={msg}> {msg} </li>))} </ul>
                  </div>
                  )
                  : null
              }
              <div className="singleCol">
                <span className="readonly">{order.orderId}</span>
                <span className="readonly">{order.orderDate}</span>

                <input type="text" label="item" name="item" value={order.item} onChange={onChange} />
                <input type="number" label="quantity" name="quantity" value={order.quantity} onChange={onChange} />
                <input type="number" label="price" name="price" value={order.price} onChange={onChange} />

                <button type="submit" onClick={update}> update </button>
                <button type="submit" onClick={closeDialog} > close </button>
              </div>
            </div>
          </dialog >
        </div >
      ) : null}
    </div >
  );
}

export default EditOrder;
