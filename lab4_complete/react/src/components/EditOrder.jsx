import { useState, useRef } from 'react';

// display order and allow user to edit item, quantity and price

function EditOrder({ editOrder, refresh }) {

    const [message, setMessage] = useState([]);
    const dialogRef = useRef();
    const [order, setOrder] = useState({ orderId: '', orderDate: '', item: '', quantity: '', price: '' });

    const onChange = (event) => {
        setOrder({ ...order, [event.target.name]: event.target.value });
    };

    const onOpen = () => {
        setMessage([]);
        setOrder(editOrder);
        dialogRef.current.showModal();
    }

    const onClose = () => {
        dialogRef.current.close();
    }

    const onSave = async () => {
        const response = await fetch(`http://localhost:8080/orders`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem("jwt"),
            },
            body: JSON.stringify(order),
        });

        if (response.ok) {
            const data = await response.json();
            setMessage([]);
            refresh();
            onClose();
        } else if (response.status === 400) {
            const data = await response.json();
            if ('errors' in data) {
                setMessage(data.errors.map((error) => <p key={error.defaultMessage}>{error.defaultMessage}</p>));
            }
        } else {
            alert("Order failed");
        }
    }

    return (
        <>
            <button onClick={onOpen}>Edit</button>
            <dialog ref={dialogRef}>
                <h2>Edit Order</h2>
                {message}
                <input type="text" name="orderId" value={order.orderId} readOnly />
                <input type="text" name="orderDate" value={order.orderDate} readOnly />
                <input id="item" type="text" name="item" placeholder="item" value={order.item} onChange={onChange} />
                <input id="quantity" type="number" name="quantity" placeholder="quantity" value={order.quantity} onChange={onChange} />
                <input id="price" type="number" name="price" placeholder="price" value={order.price} onChange={onChange} />
                <button id="saveButton" onClick={onSave}>save</button>
                <button id="closeButton" onClick={onClose}>close</button>
            </dialog>
        </>
    );
}

export default EditOrder;