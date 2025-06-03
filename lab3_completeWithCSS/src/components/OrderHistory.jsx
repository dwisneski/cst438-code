import { useState, useEffect, useContext } from "react";
import { confirmAlert } from 'react-confirm-alert'; // Import
import 'react-confirm-alert/src/react-confirm-alert.css'; // Import css
import EditOrder from "./EditOrder";
import { UserContext } from "../App";

function OrderHistory() {
    const [orders, setOrder] = useState([]);
    const [user, setUser] = useContext(UserContext);

    const getOrders = async () => {
        const response = await fetch(`http://localhost:8080/customers/${user.customerId}/orders`, {
            method: "GET",
            headers: {
                Accept: "application/json",
                Authorization: user.jwtToken,
            },
        });
        const data = await response.json();
        if (response.status === 200) {
            setOrder(data);
        } else {
            alert("Request failed");
        }
    };

    // get order history on first render
    useEffect(() => {
        getOrders();
    }, []);

    const deleteOrder = async (event) => {
        // get the order id from the row on which the delete button was clicked
        // subtract 1 because the first row is the header
        const row_idx = event.target.parentNode.parentNode.rowIndex - 1;
        const orderId = orders[row_idx].orderId;
        console.log("deleteOrder " + orderId)
        confirmAlert({
            title: 'Confirm to delete',
            message: 'Delete order ' + orderId + '?',
            buttons: [
                { label: 'Yes', onClick: () => doDelete(orderId) },
                { label: 'No', }
            ]
        });
    };

    const doDelete = async (orderId) => {

        const response = await fetch(
            `http://localhost:8080/orders/${orderId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": user.jwtToken,
            },
        }
        );
        if (response.ok) {
            alert("Order deleted");
            getOrders();
        } else {
            alert("Order delete failed");
        }
    };

    return (
        <div>
            <h1>Order History</h1>
            <table>
                <thead>
                    <tr>
                        <th>Order Id</th>
                        <th>Date</th>
                        <th className="hidden md:table-cell">Item</th>
                        <th className="hidden md:table-cell">Quantity</th>
                        <th className="hidden md:table-cell">Price</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody >
                    {orders.map((order) => (
                        <tr key={order.orderId}>
                            <td>{order.orderId}</td>
                            <td >{order.orderDate}</td>
                            <td className="hidden md:table-cell">{order.item}</td>
                            <td className="hidden md:table-cell">{order.quantity}</td>
                            <td className="hidden md:table-cell">{order.price}</td>
                            <td> <EditOrder data={order} updateOrders={getOrders} /> </td>
                            <td> <button onClick={deleteOrder} > Delete </button> </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default OrderHistory;
