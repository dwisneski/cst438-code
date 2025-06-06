import './App.css'
import { BrowserRouter, Routes, Route, Link, Outlet } from "react-router-dom";
import Login from './components/Login';
import Register from './components/Register';
import Order from './components/Order';
import OrderHistory from './components/OrderHistory';

// display nav bar with links to components

function App() {

  return (
    <>
      <div className="App">
        <h1>Customer Order Management</h1>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<AppLayout />} >
              <Route index element={<Login />} />
              <Route path="register" element={<Register />} />
              <Route path="order" element={<Order />} />
              <Route path="history" element={<OrderHistory />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </div>
    </>
  )
}

export const AppLayout = () => {
  return (
    <>
      <nav>
        <Link id="home" to="/">Home</Link> &nbsp;|&nbsp;
        <Link id="register" to="/register">Register</Link>&nbsp;|&nbsp;
        <Link id="order" to="/order">Order</Link>&nbsp;|&nbsp;
        <Link id="history" to="/history">Order History</Link>
      </nav>
      <Outlet />
    </>
  )
}

export default App;