import React from "react";
import { useNavigate } from "react-router-dom";
import { FaSignOutAlt } from "react-icons/fa";
import "../styles/styles.scss";
import logo from "../assets/logo.svg";

const Header = () => {
  const navigate = useNavigate();
  const isLoggedIn = !!localStorage.getItem("token");

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  const handleLogoClick = () => {
    navigate("/dashboard");
  };

  return (
    <header>
      <div
        className="logo-title"
        onClick={handleLogoClick}
        style={{ cursor: "pointer" }}
      >
        <img src={logo} alt="Critical Blunder Logo" className="logo" />
        <h1>Critical Blunder</h1>
      </div>
      {isLoggedIn && (
        <button className="logout-btn" onClick={handleLogout}>
          <FaSignOutAlt size={12} />
          <span>Salir</span>
        </button>
      )}
    </header>
  );
};

export default Header;