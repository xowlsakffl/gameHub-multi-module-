import { useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login.jsx'
import Signup from './pages/Signup.jsx'
import ForgotPassword from './pages/ForgotPassword.jsx'
import Home from './pages/Home.jsx'

function PrivateRoute({ children }) {
    const token = localStorage.getItem("token") || sessionStorage.getItem("token");
    return token ? children : <Navigate to="/login" />;
}

function App() {
  return (
    <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/home" element={
            <PrivateRoute>
                <Home />
            </PrivateRoute>
        } />
        <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  )
}

export default App
