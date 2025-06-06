import { useState, useEffect } from "react";
import { confirmAlert } from 'react-confirm-alert'; // Import
import 'react-confirm-alert/src/react-confirm-alert.css'; // Import css
import EditOrder from "./EditOrder";

// display table of orders for logged in user
// allow delete and edit of an order

function OrderHistory() {
    const [orders, setOrder] = useState([]);

    const getOrders = async () => {
        const response = await fetch(`http://localhost:8080/orders`, {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Authorization': localStorage.getItem("jwt"),
            },
        });
        const data = await response.json();
        if (response.status === 200) {
            setOrder(data);
        } else {
            alert("Request failed");
        }
    };

    // call getOrders on first render
    useEffect(() => {
        getOrders();
    }, []);

    const confirmDelete = async (orderId) => {
        confirmAlert({
            title: 'Confirm to delete',
            message: 'Delete order ' + orderId + '?',
            buttons: [
                {
                    label: 'Yes',
                    onClick: () => doDelete(orderId)
                },
                {
                    label: 'No',
                }
            ]
        });
    };

    const doDelete = async (orderId) => {

        const response = await fetch(
            `http://localhost:8080/orders/${orderId}`, {
            method: "DELETE",
            headers: {
                'Authorization': localStorage.getItem("jwt"),
            },
        });
        if (response.ok) {
            alert("Order deleted");
            getOrders();
        } else {
            alert("Order delete failed");
        }
    };

    return (
        <div>
            <h2>Order History</h2>
            <table>
                <thead>
                    <tr>
                        <th>Order Id</th>
                        <th>Date</th>
                        <th>Item</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map((order) => (
                        <tr key={order.orderId}>
                            <td>{order.orderId}</td>
                            <td>{order.orderDate}</td>
                            <td>{order.item}</td>
                            <td>{order.quantity}</td>
                            <td>{order.price}</td>
                            <td><EditOrder editOrder={order} refresh={getOrders} /></td>
                            <td><button onClick={() => confirmDelete(order.orderId)}>Delete</button></td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default OrderHistory;
