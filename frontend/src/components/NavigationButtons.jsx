import React from "react";
import { useNavigate } from "react-router-dom";
import PropTypes from "prop-types";
import "../styles/styles.scss";

const NavigationButtons = ({
  buttons,
  containerClass = "navigation-buttons",
}) => {
  const navigate = useNavigate();

  return (
    <div className={containerClass}>
      {buttons.map(({ label, path }, index) => (
        <button key={index} onClick={() => navigate(path)}>
          {label}
        </button>
      ))}
    </div>
  );
};

NavigationButtons.propTypes = {
  buttons: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string.isRequired,
      path: PropTypes.string.isRequired,
    })
  ).isRequired,
  containerClass: PropTypes.string,
};

export default NavigationButtons;