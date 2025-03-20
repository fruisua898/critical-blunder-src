import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/styles.scss";

const MobileBackButton = () => {
  const navigate = useNavigate();
  return (
    <button className="back-mobile" onClick={() => navigate(-1)}>
      Volver
    </button>
  );
};

export default MobileBackButton;